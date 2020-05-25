package ttn.bootcamp.linksharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
public class LinkResourceController {

    @Autowired
    ResourceService resourceService;

    @Autowired
    UserService userService;

    @Autowired
    UserResourceStatusRepository userResourceStatusRepository;

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    TopicService topicService;



    //List<User> userList;


    @PostMapping("/createLinkResource")
    public ModelAndView createLink(@ModelAttribute("resource") Resource linkResource, @RequestParam("resourcetopic") Integer topicId, HttpSession session, RedirectAttributes redirectAttributes) {

        ModelAndView modelAndView = new ModelAndView();
        User user = (User) session.getAttribute("user");
        Resource resource = resourceService.createLinkResource(linkResource, user, topicId);
        saveUserResourceStatus(redirectAttributes, user, resource, topicId);

        modelAndView.setViewName("redirect:/dashboard");
        return modelAndView;
    }

    @PostMapping("/createLinkResourceFromPostPage")
    public ModelAndView createLinkFromPostPage(@ModelAttribute("resource") Resource linkResource, @RequestParam("resourcetopic") Integer topicId, HttpSession session, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) session.getAttribute("user");
        Resource resource = resourceService.createLinkResource(linkResource, user, topicId);
        saveUserResourceStatus(redirectAttributes, user, resource, topicId);
        modelAndView.setViewName("redirect:/post");
        return modelAndView;
    }

    @PostMapping("/createLinkResourceFromTopicPage/{id}")
    public ModelAndView createLinkFromTopicPage(@PathVariable("id") Integer id, @ModelAttribute("resource") Resource linkResource, @RequestParam("resourcetopic") Integer topicId, HttpSession session, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) session.getAttribute("user");
        Resource resource = resourceService.createLinkResource(linkResource, user, topicId);

        saveUserResourceStatus(redirectAttributes, user, resource, topicId);

        modelAndView.setViewName("redirect:/topicPage/" + id);
        return modelAndView;
    }

    @PostMapping("/createLinkResourceFromAllTopicsPage")
    public ModelAndView createLinkFromAllTopicsPage(@ModelAttribute("resource") Resource linkResource, @RequestParam("resourcetopic") Integer topicId, HttpSession session, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) session.getAttribute("user");
        Resource resource = resourceService.createLinkResource(linkResource, user, topicId);

        saveUserResourceStatus(redirectAttributes, user, resource, topicId);

        modelAndView.setViewName("redirect:/alltopicsPage");
        return modelAndView;
    }

    public void saveUserResourceStatus(RedirectAttributes redirectAttributes, User user, Resource resource, Integer topicId) {
        if (resource != null) {
            redirectAttributes.addFlashAttribute("postmsg", "link created");
            Topic topic = topicService.getTopicById(topicId);

            List<User> userList = subscriptionService.getAllUsersByTopic(topic);
            Iterator<User> iterator = userList.iterator();
            while (iterator.hasNext()){
                User user1 = iterator.next();
                if(user1.getUserId() != user.getUserId()) {
                    UserResourceStatus userResourceStatus = new UserResourceStatus();
                    userResourceStatus.setUser(user1);
                    userResourceStatus.setResource(resource);
                    userResourceStatusRepository.save(userResourceStatus);
                }
            }

        } else
            redirectAttributes.addFlashAttribute("postmsg", "link not created");
    }


}
