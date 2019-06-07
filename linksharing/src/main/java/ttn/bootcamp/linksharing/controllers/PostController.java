package ttn.bootcamp.linksharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ttn.bootcamp.linksharing.entities.Rating;
import ttn.bootcamp.linksharing.entities.Resource;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.services.*;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {

    @Autowired
    TopicService topicService;

    @Autowired
    ResourceService resourceService;

    @Autowired
    RatingService ratingService;

    @Autowired
    UserService userService;

    @Autowired
    UserResourceStatusService userResourceStatusService;

    @ModelAttribute
    public void myPostModel(@RequestParam(defaultValue = "0") Integer page,HttpSession session, Model model) {

        if (page != 0)
            page = page - 1;

        User user = (User) session.getAttribute("user");
        model.addAttribute("resourcesSubscribed", resourceService.getResourcesOfsubscribedTopics(session,page,user));
        model.addAttribute("trendingTopics", topicService.getTrendingTopics());
    }

    @GetMapping("/post")
    public String getPostPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        session.setAttribute("topicSubscribed", topicService.getSubscribedTopicsByUser(user));
        Resource resource = new Resource();
        model.addAttribute("resource", resource);
        return "post";
    }

    @PostMapping("/editResource")
    public ModelAndView editResource(@RequestParam("resourceId") String resourceId, @RequestParam("description") String description, RedirectAttributes redirectAttributes) {
        System.out.println("inside controller");
        ModelAndView modelAndView = new ModelAndView();
        System.out.println("1233 " + resourceId);
        System.out.println("desc "+description);
        //Integer id = Integer.parseInt(resourceId);
        Resource resource = resourceService.updateResourceDescription(Integer.parseInt(resourceId), description);
        if (resource != null) {
            redirectAttributes.addFlashAttribute("updateDescMsg", "description updated");
        }
        else{
            redirectAttributes.addFlashAttribute("updateDescMsg", "description not updated");
        }
        modelAndView.setViewName("redirect:/post");
        return modelAndView;
    }

    @PostMapping("/editResource/postRating")
    public ModelAndView editResourceFromPostRating(@RequestParam("resourceId") String resourceId, @RequestParam("description") String description, RedirectAttributes redirectAttributes) {
        System.out.println("inside controller");
        ModelAndView modelAndView = new ModelAndView();
        //System.out.println("1233 " + resourceId);
        //System.out.println("desc "+description);
        //Integer id = Integer.parseInt(resourceId);
        Resource resource = resourceService.updateResourceDescription(Integer.parseInt(resourceId), description);
        if (resource != null) {
            redirectAttributes.addFlashAttribute("updateDescMsg", "description updated");
        }
        else{
            redirectAttributes.addFlashAttribute("updateDescMsg", "description not updated");
        }
        modelAndView.setViewName("redirect:/postRating");
        return modelAndView;
    }


    @PostMapping("/resourceRating")
    @ResponseBody
    public void resourceRating(@RequestParam("rating") Integer rating, @RequestParam("resourceId") Integer resourceId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        ratingService.saveRating(user, rating, resourceId);

    }

    @GetMapping("/postRating/{id}")
    public ModelAndView postRating(@PathVariable Integer id, HttpSession session){
        //redirectAttributes.addFlashAttribute("id", id);
        session.setAttribute("resourceId",id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/postRating");
        return modelAndView;
    }

    @GetMapping("/postRating")
    public String viewPost(Model model, HttpSession session) {
        User user = new User();
        Integer id = (Integer) session.getAttribute("resourceId");
        Resource resource = resourceService.getResourceById(id);
        Integer rating = resourceService.getAvgRatingByResourceId(id);
        Rating rating1 = ratingService.getRatingByUserAndResource((User) session.getAttribute("user"), resource);
        if(rating1 == null){
            rating1=new Rating();
            rating1.setPoints(0);
        }

        int leftRating = 0;
        List<Integer> ratingList=new ArrayList<>();
        List<Integer> leftRatingList=new ArrayList<>();
        if(rating==0){
            leftRating = 5;
        for(int i=1;i<=leftRating;i++)
            leftRatingList.add(i);
        }
        else{
            leftRating = 5-rating;
            for(int i=1;i<=rating;i++)
                ratingList.add(i);
            for(int i=1;i<=leftRating;i++)
                leftRatingList.add(i);
        }
        System.out.println(rating +"- : -"+leftRating);
        model.addAttribute("resource",resource);
        model.addAttribute("rate",ratingList);
        model.addAttribute("leftRate",leftRatingList);
        model.addAttribute("user",user);
        model.addAttribute("rating", rating1);
        return "postRate";
    }

    @Transactional
    @GetMapping("/markAsRead/{userId}/{resourceId}")
    public String deleteResourceFromUserResourceStatus(@PathVariable("userId") Integer userId, @PathVariable("resourceId") Integer resourceId){
        System.out.println("marks as read controller");
        User user = userService.getUserByUserId(userId);
        Resource resource = resourceService.getResourceById(resourceId);
        //deleteResourceFromUserResourceStatus()
        //System.out.println("<<<<<<<<<<before delete");
        userResourceStatusService.deleteByUserAndResource(user,resource);
        //System.out.println("<<<<<<<<<<after delete");

        return "redirect:/dashboard";
    }

    @Transactional
    @GetMapping("/deleteResource/topicPage/{id}")
    public ModelAndView deleteResourceByIdFromTopicPage(@PathVariable Integer id){
        ModelAndView modelAndView = new ModelAndView();
        userResourceStatusService.deleteAllByResource(resourceService.getResourceById(id));
        ratingService.deleteRatingByResource(resourceService.getResourceById(id));
        resourceService.deleteResourceById(id);
        modelAndView.setViewName("redirect:/topicPage");
        return modelAndView;
    }

    @Transactional
    @GetMapping("/deleteResource/{id}")
    public ModelAndView deleteResourceByIdFromPostRating(@PathVariable Integer id){
        ModelAndView modelAndView = new ModelAndView();
        userResourceStatusService.deleteAllByResource(resourceService.getResourceById(id));
        ratingService.deleteRatingByResource(resourceService.getResourceById(id));
        resourceService.deleteResourceById(id);
        modelAndView.setViewName("redirect:/dashboard");
        return modelAndView;
    }

    @Transactional
    @GetMapping("/deleteResource/post/{id}")
    public ModelAndView deleteResourceByIdFromPost(@PathVariable Integer id){
        ModelAndView modelAndView = new ModelAndView();
        Resource resource = resourceService.getResourceById(id);
        String resourcePath = resource.getUrlPath();
        File file = new File("/home/ttn/Desktop/linksharing/src/main/resources/static/"+resourcePath);
       /* try{
            file.delete();
        }catch (Exception e){}*/

        userResourceStatusService.deleteAllByResource(resourceService.getResourceById(id));
        ratingService.deleteRatingByResource(resourceService.getResourceById(id));
        resourceService.deleteResourceById(id);

        modelAndView.setViewName("redirect:/post");
        return modelAndView;
    }

}
