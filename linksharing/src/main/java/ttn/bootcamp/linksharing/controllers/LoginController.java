package ttn.bootcamp.linksharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ModelAndView getPageAfterLogin(HttpServletRequest request, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        String name = request.getParameter("username");
        String pwd = request.getParameter("password");
        User currentUser = userService.loginValidation(name, pwd);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("loginerror", "invalid user id password");
            modelAndView.setViewName("redirect:/");
        } else if (!currentUser.isActive()) {
            redirectAttributes.addFlashAttribute("loginerror", "account deactivated.. Cannot log in..");
            modelAndView.setViewName("redirect:/");
        } else {
            session.removeAttribute("loginStatus");
            session.setAttribute("user", currentUser);
            modelAndView.setViewName("redirect:/dashboard");
        }
        return modelAndView;
    }

}
