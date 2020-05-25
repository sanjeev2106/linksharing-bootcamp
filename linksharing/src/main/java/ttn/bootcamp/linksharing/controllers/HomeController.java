package ttn.bootcamp.linksharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ttn.bootcamp.linksharing.entities.Resource;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.enums.Seriousness;
import ttn.bootcamp.linksharing.services.ResourceService;
import ttn.bootcamp.linksharing.services.SubscriptionService;
import ttn.bootcamp.linksharing.services.TopicService;
import ttn.bootcamp.linksharing.services.UserResourceStatusService;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    TopicService topicService;

    @Autowired
    ResourceService resourceService;

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    UserResourceStatusService userResourceStatusService;

    @ModelAttribute
    public void myPostModel(@RequestParam(defaultValue = "0") Integer page, HttpSession session, Model model) {

        if (page != 0)
            page = page - 1;

       // User user = (User) session.getAttribute("user");
        model.addAttribute("resourcesSubscribed", userResourceStatusService.getUnreadResources(session,page));//resourceService.getResourcesOfsubscribedTopics(session,page,user));
//        model.addAttribute("trendingTopics", topicService.getTrendingTopics());
    }

    @RequestMapping("/")
    public String homePage(Model model, HttpSession session) {
        User user = new User();
        model.addAttribute("user", user);
        //session.setAttribute("loginStatus",false);
        model.addAttribute("recentShares",resourceService.getRecentShares());
        model.addAttribute("topPosts",resourceService.getTopPost());
        if(session.getAttribute("user")!=null)
            return "redirect:/dashboard";
        return "index";
    }

    @GetMapping("/dashboard")
    public ModelAndView getDashboard(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        Resource resource = new Resource();
        User user = (User) session.getAttribute("user");
        if(user==null){
            redirectAttributes.addFlashAttribute("notLogin","Login First");
            modelAndView.setViewName("redirect:/");
            return modelAndView;}
        model.addAttribute("resource", resource);
        model.addAttribute("privateTopics", topicService.getAllPrivateTopics(user));
        session.setAttribute("topicSubscribed", topicService.getSubscribedTopicsByUser(user));
        session.setAttribute("topicSubscribedCount", subscriptionService.getSubscriptionCountOfUser((User) session.getAttribute("user")));
        session.setAttribute("topicsCreatedCount", topicService.getTopicsCreatedCountOfUser((User) session.getAttribute("user")));
        session.setAttribute("seriousnessTypes", Seriousness.values());
        session.setAttribute("subscriptionDetails", subscriptionService.getAllSubscriptionDetailsByUser(user));
        session.setAttribute("trendingTopics", topicService.getTrendingTopics());
        modelAndView.setViewName("dashboard");
        return modelAndView;
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        session = null;
        return "redirect:/";
    }

}
