package org.propertybiddingsystem.usermanagementsystem.Controller;


import lombok.RequiredArgsConstructor;
import org.propertybiddingsystem.usermanagementsystem.Dto.AuthResponse;
import org.propertybiddingsystem.usermanagementsystem.Dto.LoginRequest;
import org.propertybiddingsystem.usermanagementsystem.Dto.RegisterRequest;
import org.propertybiddingsystem.usermanagementsystem.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")


public class AuthController {

    private final UserService userService;


    public AuthController(UserService userService) {
        this.userService= userService;
    }
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse registerUser(@RequestBody RegisterRequest request) {

        System.out.println("register function got executed ");
        try {




          var resonse = userService.registerUser(request);
            System.out.println(" user controller hit ");
          return resonse
                  ;



        }catch(RuntimeException e ) {
            throw       new RuntimeException("error ");
        }
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(@RequestBody LoginRequest request) {
        try {
             return  userService.loginUser(request);
        } catch (Exception e) {
            return null;
        }
    }


}
