package ttn.bootcamp.linksharing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ttn.bootcamp.linksharing.entities.Rating;
import ttn.bootcamp.linksharing.entities.Resource;
import ttn.bootcamp.linksharing.entities.Topic;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.enums.ResourceType;
import ttn.bootcamp.linksharing.enums.Visibility;
import ttn.bootcamp.linksharing.repositories.ResourceRepository;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    TopicService topicService;

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    RatingService ratingService;

    public static final String UPLOAD_FILE = "/home/ttn/Desktop/linksharing/src/main/resources/static/downloadresourceFiles/";

    public Resource createLinkResource(Resource resource, User user, Integer topicId) {
        Topic topic = topicService.getTopicById(topicId);
        resource.setTopic(topic);
        resource.setUser(user);
        resource.setResourceType(ResourceType.LINK);
        Resource resource1 = resourceRepository.save(resource);
        return resource1;
    }

    public Resource createDocumentResource(Resource resource, User user, Integer topicId, MultipartFile file) {
        Topic topic = topicService.getTopicById(topicId);
        resource.setTopic(topic);
        resource.setUser(user);
        resource.setResourceType(ResourceType.DOCUMENT);
        //byte[] bytes = new byte[0];
        try {
            byte[] bytes = file.getBytes();
            BufferedOutputStream bout = new BufferedOutputStream(
                    new FileOutputStream(UPLOAD_FILE + user.getUsername() + "_" + file.getOriginalFilename()));
            bout.write(bytes);
            bout.flush();
            bout.close();
            resource.setUrlPath("downloadresourceFiles/" + user.getUsername() + "_" + file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //resource.setUrlPath("downloadresourceFiles/" + user.getUsername() + "_" + file.getOriginalFilename());
        Resource resource1 = resourceRepository.save(resource);

        return resource1;
    }


    /*byte[] bytes = file.getBytes();
            BufferedOutputStream bout = new BufferedOutputStream(
                    new FileOutputStream(UPLOAD_FILE + user.getUserName() + "_" + file.getOriginalFilename()));
            bout.write(bytes);
            bout.flush();
            bout.close();
            resource.setUrlPath("downloadedResourceFiles/" + user.getUserName() + "_" + file.getOriginalFilename());
            resource.setUser(user);
            resource.setTopic(topic);
            resource.setDescription(desc);
            resource.setResourceType(ResourceType.DOCUMENT);
            Resource resource1 = resourceRepository.save(resource);*/

    public List<Resource> getResourcesOfsubscribedTopics(HttpSession session,Integer page, User user) {
        List<Topic> topicsOfUser = subscriptionService.getAllTopicsByUser(user);
//        List<Resource> resourcesSubscribed = resourceRepository.findAllByTopicIn(topicsOfUser);
        Page<Resource> resourceSubsPage = resourceRepository.findAllByTopicIn(new PageRequest(page,5),topicsOfUser);
        session.setAttribute("resourcePage",resourceSubsPage.getTotalPages());
        List<Resource> resourcesSubscribed = resourceSubsPage.getContent();
        return resourcesSubscribed;
    }

    public List<Resource> getResourcesByTopic(Topic topic) {
        List<Resource> resources = resourceRepository.findAllByTopic(topic);
        return resources;
    }

    public List<Resource> getResourcesCreatedByUser(User user) {
        List<Resource> resources = resourceRepository.findAllByUser(user);
        return resources;
    }

    public Resource updateResourceDescription(Integer resourceId, String description) {
        Optional<Resource> resourceOptional = resourceRepository.findById(resourceId);
        Resource resource = resourceOptional.orElse(null);
        if (resource == null)
            return null;
        resource.setDescription(description);

        return resourceRepository.save(resource);

    }

    @Transactional
    public void deleteResourcesByTopic(Topic topic) {

        List<Resource> resourceList = resourceRepository.findAllByTopic(topic);
        if (!resourceList.isEmpty()) {
            Iterator<Resource> resourceIterator = resourceList.iterator();
            while (resourceIterator.hasNext()) {
                Resource resource = resourceIterator.next();
                if (resource.getResourceType().name() == "DOCUMENT") {
                    File fileToDelete = new File("/home/ttn/linksharing/src/main/resources/static/" + resource.getUrlPath());
                    fileToDelete.delete();
                }
            }

            resourceRepository.deleteAllByTopic(topic);
        }
    }

    public Resource getResourceById(Integer id) {
        return resourceRepository.findById(id).orElse(null);
    }

    public List<Resource> getAllResourcesByResourceSearch(String resourceName) {
        List<Resource> resourceDescList = resourceRepository.findAllByDescriptionLike('%' + resourceName + '%');
        List<Resource> resourceDescPublicList = new ArrayList<>();

        Iterator<Resource> resourceDescListResourceIterator = resourceDescList.iterator();
        while (resourceDescListResourceIterator.hasNext()) {
            Resource resource = resourceDescListResourceIterator.next();
            if (resource.getTopic().getVisibilty() == Visibility.PUBLIC) {
                resourceDescPublicList.add(resource);
            }
        }

        List<Topic> topicList = topicService.getPublicTopicsByTopicNameSearch(resourceName);
        List<Resource> resourceTopicList = resourceRepository.findAllByTopicIn(topicList);

        List<Resource> finalResourceList = new ArrayList<>();
        if(resourceDescPublicList.containsAll(resourceTopicList))
            finalResourceList.addAll(resourceDescPublicList);
        else if(resourceTopicList.containsAll(resourceDescPublicList))
            finalResourceList.addAll(resourceTopicList);

        return finalResourceList;
    }

    public List<Resource> getAllResourcesByDescAndTopicSearch(String value, Topic topic) {
        return resourceRepository.findAllByDescriptionLikeAndTopic('%' + value + '%', topic);
    }

    public List<Resource> getAllDownloadableResourceTopicSpecific(Topic topic) {
        ResourceType resourceType = ResourceType.DOCUMENT;
        return resourceRepository.findAllByResourceTypeAndTopic(resourceType, topic);
    }

    public List<Resource> getAllLinkResourceTopicSpecific(Topic topic) {
        ResourceType resourceType = ResourceType.LINK;
        return resourceRepository.findAllByResourceTypeAndTopic(resourceType, topic);
    }

    public List<Resource> getRecentShares() {
        List<Topic> topics = topicService.getAllPublicTopics();
        List<Resource> allResources = resourceRepository.findAllByTopicIn(topics);

        ListIterator<Resource> resourceListIterator = allResources.listIterator(allResources.size());

        List<Resource> recentResources = new ArrayList<>();
        while (resourceListIterator.hasPrevious() && recentResources.size() < 5) {
            recentResources.add(resourceListIterator.previous());
        }

        return recentResources;
    }

    public List<Resource> getTopPost() {
        Iterable<Resource> resources = resourceRepository.findAll();
        List<Resource> allResources = new ArrayList<>();
        resources.forEach(allResources::add);

        System.out.println("resources "+resources+" allResources "+allResources);
        List<Rating> ratings = ratingService.getAllRating();
        System.out.println("rating "+ratings);
        Resource resource;
        Rating rating;
        HashMap<Integer, Integer> resourceRating = new HashMap<>();

        Iterator<Resource> resourceIterator = allResources.iterator();

        while (resourceIterator.hasNext()) {
            resource = resourceIterator.next();
            Iterator<Rating> ratingIterator = ratings.iterator();
            while (ratingIterator.hasNext()) {
                rating = ratingIterator.next();
                if (rating.getResource().getResourceId() == resource.getResourceId()) {
                    if (resourceRating.containsKey(resource.getResourceId())) {
                        int points = resourceRating.get(resource.getResourceId()) + rating.getPoints();
                        resourceRating.put(resource.getResourceId(), points);
                    } else {
                        resourceRating.put(resource.getResourceId(), rating.getPoints());
                    }
                }

            }
        }

        List<Map.Entry<Integer, Integer>> list =
                new LinkedList<Map.Entry<Integer, Integer>>(resourceRating.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            public int compare(Map.Entry<Integer, Integer> e1, Map.Entry<Integer, Integer> e2) {
                int res = e1.getValue().compareTo(e2.getValue());
                return res != 0 ? res : 1; // Special fix to preserve items with equal values
            }
        });

        // put data from sorted list to hashmap
        HashMap<Integer, Integer> finalResourceRate = new LinkedHashMap<Integer, Integer>();
        for (Map.Entry<Integer, Integer> rr : list) {
            finalResourceRate.put(rr.getKey(), rr.getValue());
        }

        List<Integer> keys = finalResourceRate.entrySet().stream()
                .map(Map.Entry::getKey)
                .sorted()
                .limit(5)
                .collect(Collectors.toList());

        List<Resource> topPosts = resourceRepository.findAllByResourceIdIn(keys);
        System.out.println("topPost>>>>>>>>"+topPosts);
        return topPosts;
    }

    public Integer getAvgRatingByResourceId(Integer id) {
        Optional<Resource> resourceOptional = resourceRepository.findById(id);
        Resource resource = resourceOptional.orElse(null);
        if (resource!=null) {
            List<Rating> ratingList = ratingService.getRatingByResource(resource);
            if(ratingList.size()==0)
                return 0;
            else{
                Integer point=0;
                Integer count = 0;
                Iterator<Rating> ratingIterator = ratingList.iterator();
                while (ratingIterator.hasNext()){
                   point = point + ratingIterator.next().getPoints();
                   count++;
                }

                Integer finalpoints = point/count;
                return finalpoints;
            }

        }

        return 0;
    }

    public void deleteResourceById(Integer id){
        resourceRepository.deleteByResourceId(id);
    }
}
