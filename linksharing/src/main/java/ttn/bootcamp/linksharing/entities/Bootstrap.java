//package ttn.bootcamp.linksharing.entities;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationStartedEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Controller;
//import ttn.bootcamp.linksharing.repositories.UserRepository;
//
//import java.util.Date;
//import java.util.Iterator;
//
//@Controller
//public class Bootstrap {
//
//    @Autowired
//    UserRepository userRepository;
//
//    @EventListener(ApplicationStartedEvent.class)
//    public void init() {
//        Iterator<User> userIterator = userRepository.findAll().iterator();
//        if (!userIterator.hasNext()) {
//            for (int i = 1; i <= 10; i++) {
//                User user = new User("Firstname"+i, "Lastname"+i, "Email"+i, "Username"+i, "Password"+i, "ImagePath"+i,
//                        new Date(), new Date(), true);
//                userRepository.save(user);
//                System.out.println("\nUSer " + i + " inserted in table!");
//            }
//        }
//        System.out.println("\nYour Application is up and running");
//    }
//}
