package ttn.bootcamp.linksharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ttn.bootcamp.linksharing.entities.Resource;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.services.UserService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    UserService userService;

    @GetMapping("/admin")
    public String getAdmin(Model model){
        Resource resource = new Resource();
        model.addAttribute("resource", resource);
        return "admin";
    }

    @RequestMapping("/userList")
    public String userListPageForAdmin(Model model, @RequestParam(defaultValue = "0") Integer page, HttpSession session) {

        if (page != 0)
            page = page - 1;
        List<User> usersList = userService.getAllUsers(session, page);
        session.setAttribute("userList", usersList);
        session.setAttribute("adminCurrentPage",page);
        return "redirect:/admin";
    }

    @PostMapping("/userSorting/active")
    public String activeUserList(Model model, @RequestParam(defaultValue = "0") Integer page, HttpSession session){
        if (page != 0)
            page = page - 1;
        List<User> usersList = userService.getAllActiveUsers(session,page);
        session.setAttribute("userList", usersList);
        session.setAttribute("adminCurrentPage",page);
        /*model.addAttribute("userList", usersList);
        model.addAttribute("currentPage", page);*/
        System.out.println("active "+usersList);
        return "redirect:/admin";
    }

    @PostMapping("/userSorting/deActive")
    public String DeActiveUserList(@RequestParam(defaultValue = "0") Integer page, HttpSession session){
        if (page != 0)
            page = page - 1;
        List<User> usersList = userService.getAllDeActiveUsers(session,page);
        session.setAttribute("userList", usersList);
        session.setAttribute("adminCurrentPage",page);
        System.out.println("deactive "+usersList);
        return "redirect:/admin";
    }

    @GetMapping("/activateUser/{id}")
    public String activateUser(@PathVariable Integer id) {
        userService.updateUserActive(true, id);
        return "redirect:/userList";
    }

    @GetMapping("/deactivateUser/{id}")
    public String deactivateUser(@PathVariable Integer id) {
        userService.updateUserActive(false, id);
        return "redirect:/userList";
    }

   /* @PostMapping("/showRecordsBySelected")
    public String getUsersBySelected(Model model, String value){
        //System.out.println(value);
        List<User> users = null;
        if(value.equals("true"))
             users = userService.getUserByActive(true);
        else if(value.equals("false"))
            users = userService.getUserByActive(false);
        else
            users = userService.getAllUser();
        //model.addAttribute("selectValue",value);
        model.addAttribute("users",users);
        //System.out.println(users);
        return "usersForAdmin";
    }

    @PostMapping("/sortIdByAscendingOrder")
    public String getUserSortIdByAscendingOrder(Model model){
        List<User> users = userService.getUsersSortedByIdAscending();
        model.addAttribute("users",users);
        return "usersForAdmin";
    }

    @PostMapping("/sortIdByDescendingOrder")
    public String getUserSortIdByDescendingOrder(Model model){
        List<User> users = userService.getUsersSortedByIdDescending();

        model.addAttribute("users",users);
        return "usersForAdmin";
    }*/

    @GetMapping("/sortById/{sortingOrder}")
    public String sortById(HttpSession session, @PathVariable Integer sortingOrder){
        session.setAttribute("userList", userService.sortById(session, sortingOrder));
        return "redirect:/admin";
    }

}
