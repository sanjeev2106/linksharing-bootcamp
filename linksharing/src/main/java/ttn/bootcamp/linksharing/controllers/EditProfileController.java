package ttn.bootcamp.linksharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.services.SubscriptionService;
import ttn.bootcamp.linksharing.services.TopicService;
import ttn.bootcamp.linksharing.services.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class EditProfileController {

    @Autowired
    UserService userService;

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    TopicService topicService;

    @RequestMapping("/editProfile")
    public String editProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        session.setAttribute("updatePwdMsg", null);
        session.setAttribute("updateProfileMsg", null);
        model.addAttribute("createdTopics", topicService.getTopicsCreatedByUser(user));

        session.setAttribute("topicSubscribed", subscriptionService.getSubscriptionCountOfUser((User) session.getAttribute("user")));
        session.setAttribute("topicsCreated", topicService.getTopicsCreatedCountOfUser((User) session.getAttribute("user")));
        return "editprofile";
    }

    @PostMapping("/changePassword")
    public ModelAndView changePassword(@RequestParam("password") String password, HttpSession session,RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) session.getAttribute("user");
        String email = user.getEmail();
        int status = userService.resetPassword(password, email);

        if (status > 0)
            redirectAttributes.addFlashAttribute("updatePwdMsg", "Password Changed..!");
        else
            redirectAttributes.addFlashAttribute("updatePwdMsg", "Password not changed..! Please try again..");

        modelAndView.setViewName("redirect:/editProfile");
        return modelAndView;
    }

    @PostMapping("/editUserInfo")
    public ModelAndView editUserDetails(@RequestParam("firstname") String firstname,
                                        @RequestParam("lastname") String lastname,
                                        @RequestParam("username") String username,
                                        @RequestParam("pic") MultipartFile file, HttpSession session, RedirectAttributes redirectAttributes) {

        ModelAndView modelAndView = new ModelAndView();
        String previousUsername = null;
        User user = (User) session.getAttribute("user");

        if (!firstname.isEmpty())
            user.setFirstName(firstname);
        if (!lastname.isEmpty())
            user.setLastName(lastname);
        if (!username.isEmpty()) {
            previousUsername = user.getUsername();
            user.setUsername(username);
        }

        int status = userService.updateUser(session, user, file, previousUsername);

        if (status == 1)
            redirectAttributes.addFlashAttribute("updateProfileMsg", "Changes Updated..!");
        else
            redirectAttributes.addFlashAttribute("updateProfileMsg", "Changes not Updated..! Please try again..");

        modelAndView.setViewName("redirect:/editProfile");
        return modelAndView;
    }

    @GetMapping("/checkUsernameAvailabilityToEdit")
    @ResponseBody
    public Boolean checkUsernameAvailability(@RequestParam String uname, HttpSession session) {

        Boolean result;

        User user = (User) session.getAttribute("user");
        result = userService.doesUsernameExistsExceptThis(user, uname);

        return result;
    }
}
