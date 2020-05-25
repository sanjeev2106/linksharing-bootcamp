package ttn.bootcamp.linksharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.services.ResourceService;
import ttn.bootcamp.linksharing.services.SubscriptionService;
import ttn.bootcamp.linksharing.services.TopicService;
import ttn.bootcamp.linksharing.services.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class UserProfileController {

    @Autowired
    UserService userService;

    @Autowired
    TopicService topicService;

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    ResourceService resourceService;

    @GetMapping("/userProfile/{userId}")
    public String getUserProfile(HttpSession session, @PathVariable Integer userId) {
        //User user = userService.getUserByUserId(userId);
        //model.addFlashAttribute("user",user);
        session.setAttribute("userId",userId);
        return "redirect:/userProfile";
    }

    @GetMapping("userProfile")
    public String userProfile(HttpSession session, Model model){

        User user = userService.getUserByUserId((Integer)session.getAttribute("userId"));

        model.addAttribute("topicSubscribedCount", subscriptionService.getSubscriptionCountOfUser(user));
        model.addAttribute("topicsCreatedCount", topicService.getTopicsCreatedCountOfUser(user));
        model.addAttribute("topicSubscribed", topicService.getSubscribedTopicsByUser(user));
        model.addAttribute("createdTopics", topicService.getTopicsCreatedByUser(user));
        model.addAttribute("resourcesCreated",resourceService.getResourcesCreatedByUser(user));
        model.addAttribute("user",user);
        return "userprofile";

    }

}
