package ttn.bootcamp.linksharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ttn.bootcamp.linksharing.entities.Topic;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.services.SubscriptionService;
import ttn.bootcamp.linksharing.services.TopicService;
import ttn.bootcamp.linksharing.services.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class SubscriptionController {

    @Autowired
    UserService userService;

    @Autowired
    TopicService topicService;

    @Autowired
    SubscriptionService subscriptionService;

    @PostMapping("/changeSubscription")
    public String changeSubscriptionType(@RequestParam("topicId") Integer topicId, @RequestParam("newseriousness") String seriousness, @RequestParam("btn") String btnType) {

        if (btnType.equals("subscribe"))
            return "redirect:/subscribeFromDashboard/" + topicId + "/" + seriousness;
        else
            return "redirect:/unsubscribeFromDashboard/" + topicId;
    }

    @RequestMapping("/subscribeFromDashboard/{topicId}/{seriousness}")
    public String subscribeFromDashboard(@PathVariable("topicId") Integer topicId, @PathVariable("seriousness") String seriousness, HttpSession session) {

        Topic topic = topicService.getTopicById(topicId);
        User user = (User) session.getAttribute("user");
        if (topic == null || topic.getUser().getUserId() == user.getUserId())
            return "redirect:/dashboard";
        else {

            topicService.addSubscriber(topic, user, seriousness);
            return "redirect:/dashboard";
        }
    }

    @RequestMapping("/unsubscribeFromDashboard/{topicId}")
    public String unsubscribeFromDashboard(@PathVariable("topicId") Integer topicId, HttpSession session) {
        Topic topic = topicService.getTopicById(topicId);
        User user = (User) session.getAttribute("user");
        if (topic == null || topic.getUser().getUserId() == user.getUserId())
            return "redirect:/dashboard";
        else {
            topicService.removeSubscriber(topic, user);
            return "redirect:/dashboard";
        }
    }

    @RequestMapping("/unsubscribeFromSubscribedTopics")
    public String unsubscribeFromSubscribedTopics(@RequestParam("topicId") String topicId, HttpSession session) {
        Topic topic = topicService.getTopicById(Integer.parseInt(topicId));
        User user = (User) session.getAttribute("user");
        if (topic == null || topic.getUser().getUserId() == user.getUserId())
            return "redirect:/dashboard";
        else {
            topicService.removeSubscriber(topic, user);
            return "redirect:/dashboard";
        }
    }

    @PostMapping("/changeSeriousness")
    @ResponseBody
    public String changeSeriousness(@RequestParam("newseriousness") String seriousness, @RequestParam("topicId") String topicId, HttpSession session) {
        Topic topic = topicService.getTopicById(Integer.parseInt(topicId));
        User user = (User) session.getAttribute("user");
        if (topic == null || topic.getUser().getUserId() == user.getUserId())
            return null;
        else {
            subscriptionService.changeSeriousness(topic, user, seriousness);
            return seriousness;
        }
    }

    @RequestMapping("/subscribeFromLink")
    public ModelAndView subscribeFromLink(@RequestParam("topicId") String topicId,
                                          @RequestParam("userId") Integer userId, RedirectAttributes redirectAttributes) {

        ModelAndView modelAndView = new ModelAndView();
        Topic topic = topicService.getTopicById(Integer.parseInt(topicId));
        User user = userService.getUserByUserId(userId);
        String msg = topicService.addSubscriber(topic, user, "SERIOUS");
        redirectAttributes.addFlashAttribute("message", msg);
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }


    @PostMapping("/changeSubscriptionFromAllTopics")
    public String changeSubscriptionTypeFromAllTopics(@RequestParam("topicId") Integer topicId, @RequestParam("newseriousness") String seriousness, @RequestParam("btn") String btnType) {

        if (btnType.equals("subscribe"))
            return "redirect:/subscribeFromAllTopics/" + topicId + "/" + seriousness;
        else
            return "redirect:/unsubscribeFromAllTopics/" + topicId;
    }

    @RequestMapping("/subscribeFromAllTopics/{topicId}/{seriousness}")
    public String subscribeFromAllTopics(@PathVariable("topicId") Integer topicId, @PathVariable("seriousness") String seriousness, HttpSession session) {

        Topic topic = topicService.getTopicById(topicId);
        User user = (User) session.getAttribute("user");
        if (topic == null || topic.getUser().getUserId() == user.getUserId())
            return "redirect:/alltopicsPage";
        else {

            topicService.addSubscriber(topic, user, seriousness);
            return "redirect:/alltopicsPage";
        }
    }

    @RequestMapping("/unsubscribeFromAllTopics/{topicId}")
    public String unsubscribeFromAllTopics(@PathVariable("topicId") Integer topicId, HttpSession session) {
        Topic topic = topicService.getTopicById(topicId);
        User user = (User) session.getAttribute("user");
        if (topic == null || topic.getUser().getUserId() == user.getUserId())
            return "redirect:/alltopicsPage";
        else {
            topicService.removeSubscriber(topic, user);
            return "redirect:/alltopicsPage";
        }
    }

    @PostMapping("/changeSubscriptionFromPost")
    public String changeSubscriptionTypeFromPosts(@RequestParam("topicId") Integer topicId, @RequestParam("newseriousness") String seriousness, @RequestParam("btn") String btnType) {

        if (btnType.equals("subscribe"))
            return "redirect:/subscribeFromPost/" + topicId + "/" + seriousness;
        else
            return "redirect:/unsubscribeFromPost/" + topicId;
    }

    @RequestMapping("/subscribeFromPost/{topicId}/{seriousness}")
    public String subscribeFromPosts(@PathVariable("topicId") Integer topicId, @PathVariable("seriousness") String seriousness, HttpSession session) {

        Topic topic = topicService.getTopicById(topicId);
        User user = (User) session.getAttribute("user");
        if (topic == null || topic.getUser().getUserId() == user.getUserId())
            return "redirect:/post";
        else {

            topicService.addSubscriber(topic, user, seriousness);
            return "redirect:/post";
        }
    }

    @RequestMapping("/unsubscribeFromPost/{topicId}")
    public String unsubscribeFromPosts(@PathVariable("topicId") Integer topicId, HttpSession session) {
        Topic topic = topicService.getTopicById(topicId);
        User user = (User) session.getAttribute("user");
        if (topic == null || topic.getUser().getUserId() == user.getUserId())
            return "redirect:/post";
        else {
            topicService.removeSubscriber(topic, user);
            return "redirect:/post";
        }
    }

}
