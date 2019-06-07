package ttn.bootcamp.linksharing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ttn.bootcamp.linksharing.entities.Resource;
import ttn.bootcamp.linksharing.entities.Topic;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.entities.UserResourceStatus;
import ttn.bootcamp.linksharing.repositories.UserResourceStatusRepository;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserResourceStatusService {
    @Autowired
    UserResourceStatusRepository userResourceStatusRepository;

    public void deleteResourceByResource(List<Resource> resources){
        userResourceStatusRepository.deleteByResourceIn(resources);
    }

    public void deleteByUserAndResource(User user, Resource resource){
        userResourceStatusRepository.deleteByUserAndResource(user,resource);
    }

    /*public List<Resource> getResourcesOfsubscribedTopics(HttpSession session, Integer page, User user) {
        List<Topic> topicsOfUser = subscriptionService.getAllTopicsByUser(user);
//        List<Resource> resourcesSubscribed = resourceRepository.findAllByTopicIn(topicsOfUser);
        Page<Resource> resourceSubsPage = resourceRepository.findAllByTopicIn(new PageRequest(page,5),topicsOfUser);
        session.setAttribute("resourcePage",resourceSubsPage.getTotalPages());
        List<Resource> resourcesSubscribed = resourceSubsPage.getContent();
        return resourcesSubscribed;
    }*/

    @Transactional
    public List<UserResourceStatus> getUnreadResources(HttpSession session, Integer page){
        User user = (User)session.getAttribute("user");
        List<UserResourceStatus> userResourceStatusList = userResourceStatusRepository.findAllByUser(user, new PageRequest(page, 5));
        return userResourceStatusList;
    }

    @Transactional
    public void deleteAllByResource(Resource resource){
        userResourceStatusRepository.deleteAllByResource(resource);
    }
}
