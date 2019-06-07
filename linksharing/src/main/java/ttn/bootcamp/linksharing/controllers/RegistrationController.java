package ttn.bootcamp.linksharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.exceptions.DuplicateEmailException;
import ttn.bootcamp.linksharing.exceptions.DuplicateUsernameException;
import ttn.bootcamp.linksharing.exceptions.PasswordsDontMatchException;
import ttn.bootcamp.linksharing.services.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    UserService userService;

    @PostMapping("/users/registration")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Errors errors,
                               @RequestParam("pic") MultipartFile file, HttpSession session) {

        try {
            if (userService.doesEmailExists(user.getEmail()))
                throw new DuplicateEmailException("email exists");

            if (userService.doesUsernameExists(user.getUsername()))
                throw new DuplicateUsernameException("username exists");

            if (!(user.getPassword().equals(user.getConfirmPassword())))
                throw new PasswordsDontMatchException("passwords don't match");

            if (result.hasErrors())
                return "index";

            userService.registerUser(user, file);
            session.removeAttribute("loginStatus");
            session.setAttribute("user", userService.getUserByUserId(user.getUserId()));

        } catch (DuplicateEmailException ex) {
            errors.rejectValue("email", "duplicate-email-error");
        } catch (DuplicateUsernameException ex) {
            errors.rejectValue("username", "duplicate-username-error");
        } catch (PasswordsDontMatchException ex) {
            errors.rejectValue("confirmPassword", "passwords-match-error");
        }

        return "redirect:/dashboard";
    }

    //used during if javascript validation
    @GetMapping("/checkEmailAvailability")
    @ResponseBody
    public Boolean checkEmailAvailability(@RequestParam String email) {
        System.out.println(email);
        Boolean result;
        result = userService.doesEmailExists(email);
        return result;
    }

    //used during if javascript validation
    @GetMapping("/checkUsernameAvailability")
    @ResponseBody
    public Boolean checkUsernameAvailability(@RequestParam String uname) {
        System.out.println("in checkUsernameAvailability: ");
        Boolean result;
        result = userService.doesUsernameExists(uname);
        System.out.println(result);
        return result;
    }

}
