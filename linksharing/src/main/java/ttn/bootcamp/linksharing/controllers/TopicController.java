package ttn.bootcamp.linksharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ttn.bootcamp.linksharing.entities.Resource;
import ttn.bootcamp.linksharing.entities.Topic;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.entities.UserResourceStatus;
import ttn.bootcamp.linksharing.services.ResourceService;
import ttn.bootcamp.linksharing.services.SubscriptionService;
import ttn.bootcamp.linksharing.services.TopicService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class TopicController {

    @Autowired
    TopicService topicService;

    @Autowired
    ResourceService resourceService;

    @Autowired
    SubscriptionService subscriptionService;

    @PostMapping(value = "/createtopic")
    public ModelAndView createTopic(@RequestParam("topicname") String topicname, @RequestParam("visibility") String visibility, HttpSession session, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) session.getAttribute("user");
        Topic topicCreated = topicService.addTopic(user, topicname, visibility);
        if (topicCreated != null) {
            session.setAttribute("topicSubscribed", topicService.getSubscribedTopicsByUser(user));
            redirectAttributes.addFlashAttribute("topicCreatedMsg", "Your Topic Has been Created");

        } else {
            redirectAttributes.addFlashAttribute("topicCreatedMsg", "some problem occurs in creating topic");
        }

        modelAndView.setViewName("redirect:/dashboard");
        return modelAndView;
    }

   /* @GetMapping("/getTopic")
    public void getTopic(HttpSession session,HttpServletResponse response) throws IOException {
        topicService.getNumberOfTopicsByUserId(session,response);
    }*/

    @GetMapping("/checkTopicNameUnique")
    @ResponseBody
    public boolean checkTopicNameUnique(@RequestParam String topicname, HttpSession session) {

        Boolean result;
        System.out.println("topic name : " + topicname);
        User user = (User) session.getAttribute("user");
        result = topicService.checkTopicNameUnique(user, topicname);

        return result;
    }

    @PostMapping("/changeTopicname")
    @ResponseBody
    public String changeTopicname(@RequestParam Integer topicId, @RequestParam String newname) {
        System.out.println(topicId + " : " + newname);
        String result = topicService.changeTopicName(topicId, newname);
        if (result == null)
            return "redirect:/dashboard";
        return result;
    }

    @PostMapping("/changeVisibility")
    @ResponseBody
    public String changeVisibility(@RequestParam("newvisibility") String visibility, @RequestParam("topicId") Integer topicId, HttpSession session) {

        Topic topic = topicService.getTopicById(topicId);

        User user = (User) session.getAttribute("user");

        if (topic == null)
            return null;
        else {
            topicService.changeVisibility(topic, visibility);

            return visibility;
        }
    }

    @GetMapping("/topicPage/{id}")
    public String topicpage( @PathVariable("id") Integer topicId, HttpSession session){
        session.setAttribute("topicId",topicId);
        //redirectAttributes.addFlashAttribute("topicId",topicId);

        return "redirect:/topicPage";
    }

    @GetMapping("/topicPage")
    public String getTopicPage(HttpSession session, Model model) {
        Integer topicId = (Integer) session.getAttribute("topicId");
        Resource resource = new Resource();
        Topic topic = topicService.getTopicById(topicId);
        User user = (User) session.getAttribute("user");
        List<User> userSubscribedToTopic = subscriptionService.getAllUsersByTopic(topic);
        List<Resource> resourceListOfTopic = resourceService.getResourcesByTopic(topic);
        HashMap<Integer, Integer> subscriptionCountOfUsersList = subscriptionService.getSubscriptionCountOfUsersList(userSubscribedToTopic);
        model.addAttribute("subscriptionDetails", subscriptionService.getAllSubscriptionDetailsByTopic(topic));
        model.addAttribute("topic", topic);
        model.addAttribute("usersSubscribed", userSubscribedToTopic);
        model.addAttribute("subscriptionCountOfUsers", subscriptionCountOfUsersList);
        model.addAttribute("resources", resourceListOfTopic);
        if (user.isAdmin())
            model.addAttribute("allTopics", topicService.getAllTopics());
        else
            model.addAttribute("allTopics", topicService.getAllPublicAndOwnPrivateTopics(user));
        model.addAttribute("resource", resource);
        return "topicpage";
    }

    @GetMapping("/deleteTopic/{id}")
    public String deleteTopic(@PathVariable("id") Integer topicId, Model model) {
        // Resource resource = new Resource();
        Topic topic = topicService.getTopicById(topicId);
        topicService.deleteTopic(topic);
        return "redirect:/dashboard";
    }

    @GetMapping("/deleteTopicFromAllTopic/{id}")
    public String deleteTopicFromAllTopic(@PathVariable("id") Integer topicId, Model model) {
        // Resource resource = new Resource();
        Topic topic = topicService.getTopicById(topicId);
        topicService.deleteTopic(topic);
        return "redirect:/alltopicsPage";
    }

    @GetMapping("/deleteTopicFromPost/{id}")
    public String deleteTopicFromPostPage(@PathVariable("id") Integer topicId, Model model) {
        // Resource resource = new Resource();
        Topic topic = topicService.getTopicById(topicId);
        topicService.deleteTopic(topic);
        return "redirect:/post";
    }

    @GetMapping("/alltopicsPage")
    public String showAllPublicTopics(@RequestParam(defaultValue = "0") Integer page,HttpSession session, Model model) {
        if (page != 0)
            page = page - 1;

        User user = (User) session.getAttribute("user");
        List<Topic> topics = new ArrayList<>();
        if (user.isAdmin()) {
            topics = topicService.getAllTopicsPagable(session, page);
        } else {
            topics = topicService.getAllPublicAndOwnPrivateTopicsPageable(session, page, user);
        }

        model.addAttribute("topics",topics);

        Resource resource = new Resource();
        model.addAttribute("resource", resource);

        //System.out.println("out");
        return "allTopicsPage";
    }

}
