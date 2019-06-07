package ttn.bootcamp.linksharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ttn.bootcamp.linksharing.entities.Topic;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.services.TopicService;
import ttn.bootcamp.linksharing.services.UserService;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpSession;
import java.util.Properties;

@Controller
public class InvitationController {

    @Autowired
    UserService userService;
    @Autowired
    TopicService topicService;

    @GetMapping("/sendInvitation")
    public ModelAndView sendEmail(HttpSession session, @RequestParam("receiverMail") String email,
                                  @RequestParam("topicId") Integer topicId, RedirectAttributes redirectAttributes) throws MessagingException {
        ModelAndView modelAndView = new ModelAndView();
        User currentUser = (User) session.getAttribute("user");
        User user = userService.getUserByEmail(email);
        Topic topic = topicService.getTopicById(topicId);
        String topicName = topic.getTopicName();
        Integer userId = user.getUserId();

        if (user != null) {
            final String sEmail = "linksharing.tothenew@gmail.com";
            final String sPass = "K96sanjeev@";
            final String rEmail = email;
            final String subject = "Invitation for Topic " + topicName + " Link Sharing";
            String link = "<html><head></head><body><form name='linkForm' method='get' action='http://localhost:8080/subscribeFromLink'><input type='hidden' name='topicId' value='" + topicId + "'/><input type='hidden' name='userId' value='" + userId + "'/> <button type='submit' style='background-color:gray;'>click to subscribe</a> </form></body> </html>";
            final String Body = "You are invited by " + currentUser.getFirstName() + " " + currentUser.getLastName() + " for topic\n" + topicName + " " + link;

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
                    return new PasswordAuthentication(sEmail, sPass);
                }
            });

            //For Message
            Multipart multiPart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            Message message = new MimeMessage(ses);
            message.setFrom(new InternetAddress(sEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rEmail));
            message.setSubject(subject);
            InternetHeaders headers = new InternetHeaders();
            headers.addHeader("Content-type", "text/html; charset=UTF-8");
            messageBodyPart.setText(Body, "UTF-8", "html");

            multiPart.addBodyPart(messageBodyPart);
            message.setContent(multiPart);
            Transport.send(message);
            redirectAttributes.addFlashAttribute("message", "Invitation sent Successfully");

        } else {
            redirectAttributes.addFlashAttribute("message", "Invitation not sent");

        }
        modelAndView.setViewName("redirect:/dashboard");
        return modelAndView;
    }

    @GetMapping("/checkEmailExistsInSystem")
    @ResponseBody
    public String emailExitsOrNot(@RequestParam String email,HttpSession session){
        System.out.println(email);
        String msg;
        User currUser = (User)session.getAttribute("user");
        User user = userService.getUserByEmail(email);
        if(user==null) {
            msg = "fail";
        }
        else {
            if(user.getUserId()==currUser.getUserId()){
                msg="yourself";
            }
            else
                msg="pass";
        }

        return msg;
    }

}
