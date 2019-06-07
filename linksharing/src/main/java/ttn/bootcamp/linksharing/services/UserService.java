package ttn.bootcamp.linksharing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.repositories.UserRepository;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager") //for update pwd
public class UserService {

    private static String UPLOAD_PHOTOS_FOLDER = "/home/ttn/Desktop/linksharing/src/main/resources/static/photos/";

    @Autowired
    UserRepository userRepository;

    public void registerUser(User user, MultipartFile file) {
        user.setActive(true);
        user.setAdmin(false);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        if (file.isEmpty()) {
            user.setPhoto("/photos/default-user-image.png");
        } else {

            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOAD_PHOTOS_FOLDER + user.getUsername() + "_" + file.getOriginalFilename());
                Files.write(path, bytes);
                user.setPhoto("/photos/" + user.getUsername() + "_" + file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userRepository.save(user);
    }


    public int updateUser(HttpSession session, User user, MultipartFile file, String previousUsername) {

        user.setUpdatedAt(new Date());

        if (!file.isEmpty()) {

            try {
                File profileToDelete = new File("/home/ttn/Desktop/linksharing/src/main/resources/static/" + user.getPhoto());
                profileToDelete.delete();
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOAD_PHOTOS_FOLDER + user.getUsername() + file.getOriginalFilename());
                Files.write(path, bytes);
                user.setPhoto("/photos/" + user.getUsername() + file.getOriginalFilename());
            } catch (IOException e) {
                System.out.println("error in else if file not is not null");
                e.printStackTrace();
            }

        } else {

            if (previousUsername != null) {
                try {

                    String filePath = user.getPhoto();
                    String oldProfileName = filePath;
                    String newProfileName = filePath.replace(previousUsername, user.getUsername());

                    File oldProfile = new File("/home/ttn/Desktop/linksharing/src/main/resources/static/" + oldProfileName);
                    File newProfile = new File("/home/ttn/Desktop/linksharing/src/main/resources/static/" + newProfileName);
                    oldProfile.renameTo(newProfile);
                    user.setPhoto(newProfileName);

                } catch (Exception e) {
                    System.out.println("error in else if previousname is not null");
                }

            }

        }

        userRepository.save(user);

        Optional<User> user1 = userRepository.findByEmail(user.getEmail());
        User userCheck = user1.isPresent() ? user1.get() : null;
        if (userCheck.getFirstName().equals(user.getFirstName()) && userCheck.getLastName().equals(user.getLastName())
                && userCheck.getUsername().equals(user.getUsername()) && userCheck.getPhoto().equals(user.getPhoto())) {
            session.setAttribute("user", user);
            return 1;
        } else if (userCheck == null)
            return 0;
        else
            return 0;
    }


    public Boolean doesEmailExists(String email) {
        Boolean result;
        Optional<User> user = userRepository.findByEmail(email);
        result = user.isPresent() ? true : false;
        return result;
    }

    public Boolean doesUsernameExists(String uname) {
        Boolean result;
        Optional<User> user = userRepository.findByUsername(uname);
        result = user.isPresent() ? true : false;
        return result;
    }

    public Boolean doesUsernameExistsExceptThis(User sessionUser, String uname) {
        Optional<User> user = userRepository.findByUsername(uname);
        if (!user.isPresent())
            return false;
        else {
            if (sessionUser.getUserId() == user.get().getUserId())
                return false;
        }
        return true;
    }

    public User loginValidation(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsernameAndPassword(username, password);

        User user = userOptional.isPresent() ? userOptional.get() : null;

        return user;

    }

    public User getUserByUserId(Integer id) {
        Optional<User> userOptional = userRepository.findByUserId(id);

        User user = userOptional.isPresent() ? userOptional.get() : null;

        return user;

    }

    public User getUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        return userOptional.orElse(null);

    }

    public int resetPassword(String password, String email) {
        System.out.println(password + " " + email);
        return userRepository.updatePassword(password, email);
    }

    public User getUserByUsername() {
        Optional<User> userOptional = userRepository.findByUsername("admin");

        User user = userOptional.isPresent() ? userOptional.get() : null;

        return user;

    }


    //for admin page

    public List<User> getAllUsers(HttpSession session, Integer page) {
        Page<User> usersPage = userRepository.findAll(new PageRequest(page, 10));
        session.setAttribute("pages", usersPage.getTotalPages());
        List<User> users = usersPage.getContent();
        return users;
    }

    public List<User> getAllActiveUsers(HttpSession session, Integer page) {
        Page<User> usersPage = userRepository.findAllByIsActive(new PageRequest(page, 10), true);
        session.setAttribute("pages", usersPage.getTotalPages());
        List<User> users = usersPage.getContent();
        return users;
    }

    public List<User> getAllDeActiveUsers(HttpSession session, Integer page) {
        Page<User> usersPage = userRepository.findAllByIsActive(new PageRequest(page, 10), false);
        session.setAttribute("pages", usersPage.getTotalPages());
        List<User> users = usersPage.getContent();
        return users;
    }

    public void updateUserActive(Boolean isActive, Integer id) {
        Optional<User> user = userRepository.findByUserId(id);
        User currUser = user.get();
        currUser.setActive(isActive);
        userRepository.save(currUser);
    }

    /*public List<User> getUserByActive(Boolean active){
        return userRepository.findAllByIsActive(active);
    }

    public List<User> getUsersSortedByIdAscending(){
        return userRepository.findAllByOrderByUserIdAsc();

    }

    public List<User> getUsersSortedByIdDescending(){
        return userRepository.findAllByOrderByUserIdDesc();

    }*/

    public List<User> getAllUser() {
        List<User> userList = userRepository.findAll();
        if (userList.isEmpty()){
            //System.out.println("userList>>>>>>"+userList);
            //System.out.println("inside if");
            userList = new ArrayList<>();
            //return ;
        }
        //System.out.println("userList:    "+userList);
        return userList;
    }

    public List<User> sortById(HttpSession session, Integer order){
        ArrayList<User> users = new ArrayList<>((List<User>)session.getAttribute("userList"));//(List<User>)session.getAttribute("userList");
        //System.out.println("users>>>> "+users);
        Comparator<User> compareById = Comparator.comparing(User::getUserId);
        //System.out.println(compareById);
        if(order==1)
            Collections.sort(users, compareById);
        else
            Collections.sort(users, compareById.reversed());
        return users;
    }

}
