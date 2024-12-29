package ticket_online.ticket_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.model.User;
import ticket_online.ticket_online.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired(required = true)
    private UserService userService;

    @PostMapping
    public ApiResponse<User> createUser(@RequestBody User user){
       User response =  userService.createUser(user);
       return ApiResponse.<User>builder().data(response).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<User> showUserById(@PathVariable Long id){
        User response = userService.findByUserId(id);
        return ApiResponse.<User>builder().data(response).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteUser(@PathVariable Long id){
       Boolean isDeleted = userService.removeUser(id);
       return ApiResponse.<Boolean>builder().data(isDeleted).build();
    }

}
