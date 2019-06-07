package ttn.bootcamp.linksharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ttn.bootcamp.linksharing.entities.Resource;
import ttn.bootcamp.linksharing.entities.Topic;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.services.ResourceService;
import ttn.bootcamp.linksharing.services.TopicService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    TopicService topicService;

    @Autowired
    ResourceService resourceService;

//    @RequestMapping("/autocomplete")
//    public List searchBar(@RequestParam("pattern") String topicOrResource){
//        System.out.println("in autocomplete");
//        // System.out.println("!!!!!!!!!!!!!!1"+topicService.getPublicTopicsByTopicNameSearch(topicOrResource));
//        System.out.println("@@@@@@@@@@@@@@2"+resourceService.getAllResourcesByResourceSearch(topicOrResource));
//        return resourceService.getAllResourcesByResourceSearch(topicOrResource);
//    }

    @RequestMapping("/searchTopic")
    public ModelAndView searchForm(@RequestParam("topic") String topicResourceName, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) session.getAttribute("user");
        int flag = 1;
        if (topicResourceName.equals("") || topicResourceName.equals(" ")) {
            if (user.isAdmin())
                flag = 1;
            else
                flag = 0;
        }
        if (flag == 1) {
            Resource resource = new Resource();
            model.addAttribute("resource", resource);
            model.addAttribute("trendingTopics", topicService.getTrendingTopicsByTopicNameSearch(topicResourceName));
            model.addAttribute("resources", resourceService.getAllResourcesByResourceSearch(topicResourceName));
            modelAndView.setViewName("search");
        } else {
            redirectAttributes.addFlashAttribute("searcherror", "enter some text to search");
            modelAndView.setViewName("redirect:/dashboard");
        }
        return modelAndView;
    }

    @RequestMapping("/searchForResources")
    public ModelAndView searchForResources(@RequestParam("search") String value, @RequestParam("topicId") String topicId, Model model) {
        System.out.println("search topic - " + value + " : " + topicId);
        Topic topic = topicService.getTopicById(Integer.parseInt(topicId));
        List<Resource> resourceList = new ArrayList<>();
        if (value.equalsIgnoreCase("download")) {
            resourceList = resourceService.getAllDownloadableResourceTopicSpecific(topic);
        } else if (value.equalsIgnoreCase("link")) {
            resourceList = resourceService.getAllLinkResourceTopicSpecific(topic);
        } else {
            resourceList = resourceService.getAllResourcesByDescAndTopicSearch(value, topic);
        }

        model.addAttribute("searchResultResources", resourceList);
        return new ModelAndView("resourceSearchforTopic");
    }

}
