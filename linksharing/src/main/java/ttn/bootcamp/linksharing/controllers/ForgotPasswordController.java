package ttn.bootcamp.linksharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.services.ResourceService;
import ttn.bootcamp.linksharing.services.UserService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

@Controller
public class ForgotPasswordController {

    @Autowired
    UserService userService;

    @Autowired
    ResourceService resourceService;

    @RequestMapping("/forgotPasswordPage")
    public String forgotPasswordPage(HttpSession session) {
        session.setAttribute("emailerror", null);
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(HttpSession session, @RequestParam("email") String email) throws IOException, javax.mail.MessagingException {
        System.out.println("email : "+email);
        Boolean check = userService.doesEmailExists(email);
        Random random = new Random();
        System.out.println("check : "+check);
        int password = 100000 + random.nextInt(20000);
        if (check) {
            session.setAttribute("email", email);
            final String SEmail = "linksharing.tothenew@gmail.com";
            final String SPass = "K96sanjeev@";
            final String REmail = email;
            final String subject = "Your Password is here For Link Sharing";
            final String Body = "Your Email Id :" + email + " And OTP is : " + password;

            //code for mail sending
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.socketFactory.port", "465");
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.port", "465");
            Session ses = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SEmail, SPass);
                }
            });

            //For Message

            Message message = new MimeMessage(ses);
            message.setFrom(new InternetAddress(SEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(REmail));
            message.setSubject(subject);
            message.setContent(Body, "text/html");
            Transport.send(message);
            session.setAttribute("otp", String.valueOf(password));
            return "enterOtp";
        } else {
            session.setAttribute("emailerror", "Invalid Mail Id");
            return "forgotPassword";
        }
    }

    @PostMapping("/otpCheck")
    public String checkOTP(HttpSession session, HttpServletResponse response, HttpServletRequest request) throws IOException {
        String otpUser = String.valueOf(request.getParameter("otp"));
        String otp = (String) session.getAttribute("otp");
        if (otp.equals(otpUser)) {
            return "changePassword";
        } else {
            session.setAttribute("otpError", "Your Otp is Wrong match your otp");
            return "enterOtp";
        }
    }

    @PostMapping("/resetPassword")
    public String setPassword(@RequestParam("password") String password, Model model, HttpSession session) {
        int status = userService.resetPassword(password, session.getAttribute("email").toString());

        if (status > 0)
            model.addAttribute("msg", "Password Changed..!");
            //session.setAttribute("updatePwdMsg", "Password Changed..!");
        else
            model.addAttribute("msg", "Password not changed..! Please try again..");
            //session.setAttribute("updatePwdMsg", "Password not changed..! Please try again..");

        User user = new User();
        model.addAttribute("recentShares",resourceService.getRecentShares());
        model.addAttribute("topPosts",resourceService.getTopPost());
        model.addAttribute("user", user);
        return "index";
    }

    @GetMapping("/checkEmailExistsInSystemForgetPwd")
    @ResponseBody
    public String emailExitsOrNot(@RequestParam String email, HttpSession session) {
        System.out.println(email);
        String msg;
        User user = userService.getUserByEmail(email);
        if (user == null) {
            msg = "fail";
        } else {
            msg = "pass";
        }

        return msg;
    }
}
