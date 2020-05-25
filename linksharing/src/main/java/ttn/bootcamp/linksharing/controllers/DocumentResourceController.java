package ttn.bootcamp.linksharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ttn.bootcamp.linksharing.entities.Resource;
import ttn.bootcamp.linksharing.entities.Topic;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.entities.UserResourceStatus;
import ttn.bootcamp.linksharing.repositories.UserResourceStatusRepository;
import ttn.bootcamp.linksharing.services.ResourceService;
import ttn.bootcamp.linksharing.services.SubscriptionService;
import ttn.bootcamp.linksharing.services.TopicService;
import ttn.bootcamp.linksharing.services.UserService;

import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.List;

@Controller
public class DocumentResourceController {

    @Autowired
    ResourceService resourceService;

    @Autowired
    UserResourceStatusRepository userResourceStatusRepository;

    @Autowired
    UserService userService;

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    TopicService topicService;


    //UserResourceStatus userResourceStatus = new UserResourceStatus();
    //List<User> userList;

    @PostMapping("/createDocumentResource")
    public ModelAndView createDocument(@ModelAttribute("resource") Resource documentResource, @RequestParam("path") MultipartFile file, @RequestParam("resourcetopic") Integer topicId, HttpSession session, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) session.getAttribute("user");
        Resource resource = resourceService.createDocumentResource(documentResource, user, topicId, file);
        saveResourceStatus(redirectAttributes, resource, user, topicId);

        modelAndView.setViewName("redirect:/dashboard");
        return modelAndView;
    }

    @PostMapping("/createDocumentResourceFromPostPage")
    public ModelAndView createDocumentFromPostPage(@ModelAttribute("resource") Resource documentResource, @RequestParam("path") MultipartFile file, @RequestParam("resourcetopic") Integer topicId, HttpSession session, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) session.getAttribute("user");
        Resource resource = resourceService.createDocumentResource(documentResource, user, topicId, file);

        saveResourceStatus(redirectAttributes, resource, user, topicId);

        modelAndView.setViewName("redirect:/post");
        return modelAndView;
    }

    @PostMapping("/createDocumentResourceFromTopicPage/{id}")
    public ModelAndView createDocumentFromTopicPage(@PathVariable("id") Integer id, @ModelAttribute("resource") Resource documentResource, @RequestParam("path") MultipartFile file, @RequestParam("resourcetopic") Integer topicId, HttpSession session, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) session.getAttribute("user");
        Resource resource = resourceService.createDocumentResource(documentResource, user, topicId, file);

        saveResourceStatus(redirectAttributes, resource, user, topicId);

        modelAndView.setViewName("redirect:/topicPage/" + id);
        return modelAndView;
    }

    @PostMapping("/createDocumentResourceFromAllTopicsPage")
    public ModelAndView createDocumentFromAllTopicsPage(@ModelAttribute("resource") Resource documentResource, @RequestParam("path") MultipartFile file, @RequestParam("resourcetopic") Integer topicId, HttpSession session, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) session.getAttribute("user");
        Resource resource = resourceService.createDocumentResource(documentResource, user, topicId, file);

        saveResourceStatus(redirectAttributes, resource, user, topicId);

        modelAndView.setViewName("redirect:/alltopicsPage");
        return modelAndView;
    }

    private void saveResourceStatus(RedirectAttributes redirectAttributes, Resource resource, User user, Integer topicId) {
        if (resource != null) {
            redirectAttributes.addFlashAttribute("postmsg", "document created");
            Topic topic = topicService.getTopicById(topicId);
            List<User> userList = subscriptionService.getAllUsersByTopic(topic);
            Iterator<User> iterator = userList.iterator();
            while (iterator.hasNext()){
                User user1 = iterator.next();
                if(user1.getUserId()!=user.getUserId()) {
                    UserResourceStatus userResourceStatus = new UserResourceStatus();
                    userResourceStatus.setUser(user1);
                    userResourceStatus.setResource(resource);
                    userResourceStatusRepository.save(userResourceStatus);
                }
            }

        } else
            redirectAttributes.addFlashAttribute("postmsg", "document not created");
    }
}
