package ticket_online.ticket_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.event.EventHomeResDto;
import ticket_online.ticket_online.model.Checker;
import ticket_online.ticket_online.model.User;
import ticket_online.ticket_online.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired(required = true)
    private UserService userService;


//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<User>> findUserById(@PathVariable Long id){
//        try {
//            User response =  userService.findByUserId(id);
//            return ResponseEntity.ok(new ApiResponse<User>(true, "user retrived ", response));
//        }catch (RuntimeException e){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
//        }
//    }



    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> findUserById(@PathVariable Long id){
        try {
            User response =  userService.findByUserId(id);
            return ResponseEntity.ok(new ApiResponse<User>(true, "user retrived ", response));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUserProfile(@PathVariable Long id, @RequestBody User user){
        try {
            User response =  userService.updateUser(id, user);
            return ResponseEntity.ok(new ApiResponse<User>(true, "user is updated ", response));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }



//    @PostMapping
//    public ApiResponse<User> createUser(@RequestBody User user){
//       User response =  userService.createUser(user);
//       return ApiResponse.<User>builder().data(response).build();
//    }

//
//    @GetMapping("/{id}")
//    public ApiResponse<User> findUserById(@PathVariable Long id){
//        User response = userService.findByUserId(id);
//        return ApiResponse.<User>builder().data(response).build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ApiResponse<Boolean> deleteUser(@PathVariable Long id){
//       Boolean isDeleted = userService.removeUser(id);
//       return ApiResponse.<Boolean>builder().data(isDeleted).build();
//    }

}
