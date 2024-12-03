package ticket_online.ticket_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ticket_online.ticket_online.dto.WebResponse;
import ticket_online.ticket_online.model.User;
import ticket_online.ticket_online.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired(required = true)
    private UserService userService;

    @PostMapping
    public WebResponse<User> createUser(@RequestBody User user){
       User response =  userService.createUser(user);
       return WebResponse.<User>builder().data(response).build();
    }

    @GetMapping("/{id}")
    public WebResponse<User> showUserById(@PathVariable Long id){
        User response = userService.findByUserId(id);
        return WebResponse.<User>builder().data(response).build();
    }

    @DeleteMapping("/{id}")
    public WebResponse<Boolean> deleteUser(@PathVariable Long id){
       Boolean isDeleted = userService.removeUser(id);
       return WebResponse.<Boolean>builder().data(isDeleted).build();
    }

}
