package ttn.bootcamp.linksharing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ttn.bootcamp.linksharing.entities.*;
import ttn.bootcamp.linksharing.enums.Visibility;
import ttn.bootcamp.linksharing.repositories.SubscriptionRepository;
import ttn.bootcamp.linksharing.repositories.TopicRepository;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class TopicService {

    @Autowired
    UserResourceStatusService userResourceStatusService;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    ResourceService resourceService;

    @Autowired
    RatingService ratingService;

    @Autowired
    SubscriptionService subscriptionService;

    public Topic addTopic(User user, String topicname, String visibility) {
        Topic topic = new Topic();

        Visibility visibility1;
        if (visibility.equals("PUBLIC")) {
            visibility1 = Visibility.PUBLIC;
        } else {
            visibility1 = Visibility.PRIVATE;
        }

        List<Integer> subscribers = new ArrayList<>();
        subscribers.add(user.getUserId());

        topic.setTopicName(topicname);
        topic.setUser(user);
        topic.setCreatedAt(new Date());
        topic.setUpdatedAt(new Date());
        topic.setVisibilty(visibility1);
        topic.setSubscribers(subscribers);

        Topic topicCreated = topicRepository.save(topic);

        subscriptionService.addCreatorSubscriber(user, topicCreated);
        return topic;
    }

    public Boolean checkTopicNameUnique(User user, String topicname) {
        Topic topic = topicRepository.findByTopicNameAndUser(topicname, user);
        if (topic == null)
            return true;
        else
            return false;
    }

    public Integer getTopicsCreatedCountOfUser(User user) {
        Integer topicsCreated = topicRepository.countAllByUser(user);
        return topicsCreated;
    }

    public List<Topic> getTopicsCreatedByUser(User user){
        List<Topic> topics = topicRepository.findAllByUser(user);
        return topics;
    }

    public List<Topic> getSubscribedTopicsByUser(User user) {
        Integer id = user.getUserId();
        List<Topic> topic = topicRepository.findBySubscribers(id);
        System.out.println("topic size : " + topic.size());
        return topic;
    }

    public Iterable<Topic> getTrendingTopics() {
        Iterable<Topic> allITopics = topicRepository.findAllByVisibilty(Visibility.PUBLIC);
        List<Topic> allTopics = new ArrayList<>();
        allITopics.forEach(allTopics::add);

        List<Topic> trendingTopics = new ArrayList<>();

        Collections.sort(allTopics, new Comparator<Topic>() {
            @Override
            public int compare(Topic t1, Topic t2) {
                return t1.getResources().size() - t2.getResources().size();
            }
        });

        ListIterator<Topic> topics = allTopics.listIterator(allTopics.size());
        int count = 1;
        while (topics.hasPrevious() && count < 6) {
            trendingTopics.add(topics.previous());
            count++;
        }
        return trendingTopics;
    }

    public Topic getTopicById(Integer id) {
        Optional<Topic> topic = topicRepository.findById(id);
        return topic.orElse(null);
    }

    public String addSubscriber(Topic topic, User user, String seriousness) {
        String message;
        List<Integer> subscriberList = topic.getSubscribers();
        if (subscriberList.contains(user.getUserId())) {
            message = "already subscribed.. login to edit..";
        } else {
            subscriberList.add(user.getUserId());
            topicRepository.save(topic);
            subscriptionService.addSubscriptionDetails(topic, user, seriousness);
            message = "Topic has been subscribed(with seriousness-serious)..! Please login to edit/see..!";
        }

        return message;
    }

    public void removeSubscriber(Topic topic, User user) {
        List<Integer> subscriberList = topic.getSubscribers();
        subscriberList.remove(user.getUserId());
        topicRepository.save(topic);
        subscriptionService.removeSubscriptionDetails(topic, user);
    }

    public String changeTopicName(Integer topicId, String newname) {
        Optional<Topic> topic = topicRepository.findById(topicId);
        Topic topic1 = topic.orElse(null);
        if (topic1 != null) {
            topic1.setTopicName(newname);
            topic1.setUpdatedAt(new Date());
            topicRepository.save(topic1);
            topic = topicRepository.findById(topicId);
            return topic.get().getTopicName();
        } else
            return null;
    }


    public void changeVisibility(Topic topic, String visibility) {
        System.out.println("in service");
        Visibility visibility1 = topic.getVisibilty();

        if (visibility.equals("PUBLIC"))
            visibility1 = Visibility.PUBLIC;
        else if (visibility.equals("PRIVATE"))
            visibility1 = Visibility.PRIVATE;

        System.out.println("visibility set");
        Topic topicToChange = topic;
        System.out.println("topic to change retrieved = old visibility : " + topicToChange.getVisibilty());

        topicToChange.setVisibilty(visibility1);
        topicToChange.setUpdatedAt(new Date());
        System.out.println("topic retrieved changed visibility : " + topicToChange.getVisibilty());

        topicRepository.save(topicToChange);
    }

    public List<Topic> getAllTopics() {
        Iterable<Topic> topicList = topicRepository.findAll();
        List<Topic> allTopics = new ArrayList<>();
        topicList.forEach(allTopics::add);
        return allTopics;
    }

    public List<Topic> getPublicTopicsByTopicNameSearch(String topicname) {
        List<Topic> topics = topicRepository.findAllByVisibiltyAndTopicNameLike(Visibility.PUBLIC,'%' + topicname + '%');
        return topics;
    }

    public List<Topic> getTrendingTopicsByTopicNameSearch(String topicname) {
        List<Topic> topics = topicRepository.findAllByVisibiltyAndTopicNameLike(Visibility.PUBLIC,'%' + topicname + '%');

        List<Topic> trendingTopics = new ArrayList<>();

        Collections.sort(topics, new Comparator<Topic>() {
            @Override
            public int compare(Topic t1, Topic t2) {
                return t1.getResources().size() - t2.getResources().size();
            }
        });

        ListIterator<Topic> topicListIterator = topics.listIterator(topics.size());
        int count = 1;
        while (topicListIterator.hasPrevious() && count < 6) {
            trendingTopics.add(topicListIterator.previous());
            count++;
        }
        return trendingTopics;
    }

    public List<Topic> getAllPrivateTopics(User user) {
        List<Topic> filteredPrivateTopics = new ArrayList<>();
        Visibility visibility = Visibility.PRIVATE;
        List<Topic> allPrivateTopics = topicRepository.findAllByVisibilty(visibility);
        Iterator iterator = allPrivateTopics.iterator();
        while (iterator.hasNext()){
            Topic topic = (Topic) iterator.next();
            Topic topic1 = topicRepository.findByTopicIdAndUser(topic.getTopicId(), user);
            Boolean inTopic = topic1 != null;
            Subscription subscription = subscriptionRepository.findByTopicAndUser(topic, user);
            Boolean inSubscription = subscription != null;
            System.out.println("inTopic: "+inTopic+"   inSubscription: "+inSubscription);
            if(inTopic || inSubscription)
                System.out.println(topic.toString());
                filteredPrivateTopics.add(topic);
        }


        return filteredPrivateTopics;
    }

    public List<Topic> getAllPublicTopics() {
        Visibility visibility = Visibility.PUBLIC;
        return topicRepository.findAllByVisibilty(visibility);
    }

    @Transactional
    public void deleteTopic(Topic topic) {
        List<Resource> resources = resourceService.getResourcesByTopic(topic);
        subscriptionService.deleteSubscribersByTopic(topic);
        ratingService.deleteRating(topic);
        resourceService.deleteResourcesByTopic(topic);
        userResourceStatusService.deleteResourceByResource(resources);
        topicRepository.delete(topic);
    }

    public List<Topic> getAllPublicAndOwnPrivateTopics(User user){
        List<Topic> allPublicTopics = topicRepository.findAllByVisibilty(Visibility.PUBLIC);
        List<Topic> allPrivateTopicOfUser = topicRepository.findAllByVisibiltyAndUser(Visibility.PRIVATE,user);

        List<Topic> topics = new ArrayList<>();
        topics.addAll(allPublicTopics);
        topics.addAll(allPrivateTopicOfUser);
        return topics;
    }

    public List<Topic> getAllTopicsPagable(HttpSession session, Integer page) {

        Page<Topic> topicsPage = topicRepository.findAll(new PageRequest(page,5));
        session.setAttribute("topicPages",topicsPage.getTotalPages());
        List<Topic> topics = topicsPage.getContent();
        return topics;
    }

    public List<Topic> getAllPublicAndOwnPrivateTopicsPageable(HttpSession session, Integer page, User user){
        Integer pages;
        Page<Topic> allPublicTopicsPage = topicRepository.findAllByVisibilty(Visibility.PUBLIC,new PageRequest(page,3));
        List<Topic> allPublicTopics = allPublicTopicsPage.getContent();
        pages=allPublicTopicsPage.getTotalPages();

        Page<Topic> allPrivateTopicOfUserPage = topicRepository.findAllByVisibiltyAndUser(Visibility.PRIVATE,user,new PageRequest(page,2));
        List<Topic> allPrivateTopicOfUser = allPrivateTopicOfUserPage.getContent();

        pages=pages+allPrivateTopicOfUserPage.getTotalPages()-1;

        session.setAttribute("topicPages",pages);

        List<Topic> topics = new ArrayList<>();
        topics.addAll(allPublicTopics);
        topics.addAll(allPrivateTopicOfUser);

        return topics;
    }
}
