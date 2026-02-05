package org.propertybiddingsystem.usermanagementsystem.Service;


import lombok.RequiredArgsConstructor;
import org.propertybiddingsystem.usermanagementsystem.Dto.AuthResponse;
import org.propertybiddingsystem.usermanagementsystem.Dto.LoginRequest;
import org.propertybiddingsystem.usermanagementsystem.Dto.RegisterRequest;
import org.propertybiddingsystem.usermanagementsystem.Exception.UserAlreadyExistsException;
import org.propertybiddingsystem.usermanagementsystem.Exception.UserNotFoundException;
import org.propertybiddingsystem.usermanagementsystem.Repository.UserRepository;
import org.propertybiddingsystem.usermanagementsystem.Utils.JwtUtils;
import org.propertybiddingsystem.usermanagementsystem.models.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service

@RequiredArgsConstructor
public class UserService {


    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // register the user
    //create a new user throws if an account with that email already exits
    public AuthResponse registerUser( RegisterRequest  request) {

        System.out.println(" user service file got executed");
        // guard the email



//        userRepository.findByEmail(request.email()).orElseThrow( ( ) -> new UserAlreadyExistsException("User already exists"));






        // check if the user already exists



        var user = new User();
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setRole(request.role());
        user.setCreated_at(LocalDateTime.now());
        user.setUpdated_at(LocalDateTime.now());

        user.setPassword(passwordEncoder.encode(request.password()));


        var savedUser = userRepository.save(user);


        System.out.println(
                " user is craeted " + savedUser.getUsername()
        );


         var accessToken = jwtUtils.generateAccessToken(savedUser.getEmail());
         var refreshToken = jwtUtils.generateRefreshToken(savedUser.getUsername())



;


         return new AuthResponse(accessToken, refreshToken);


    }


    public AuthResponse loginUser(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )


        );
        // load the user
        var user = userRepository.findByUsername(request.username()).orElseThrow(() -> new UsernameNotFoundException(" user not  found"));
        if( user ==  null) {

             throw  new UserNotFoundException("user not gound with username " + request.username() );

        }

            var accessToken = jwtUtils.generateAccessToken(user.getUsername());
;
        var refreshToken = jwtUtils.generateRefreshToken(user.getUsername());


        return new AuthResponse(accessToken, refreshToken);





    }





















}
