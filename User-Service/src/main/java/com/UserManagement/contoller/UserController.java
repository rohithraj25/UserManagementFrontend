package com.UserManagement.contoller;


import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.UserManagement.Service.UserService;
import com.UserManagement.dto.LoginRequest;
import com.UserManagement.model.Role;
import com.UserManagement.model.User;
import com.UserManagement.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private Environment env;

    @Value("${spring.application.name}")
    private String serviceId;

    // Removed the duplicate test method
    @GetMapping("/test")
    public ResponseEntity<String> testMethod() {
        return ResponseEntity.ok("test api");
    }

    @GetMapping("/service/port")
    public ResponseEntity<String> getPort() {
        return ResponseEntity.ok("Service port number: " + env.getProperty("local.server.port"));
    }

    @GetMapping("/service/instances")
    public ResponseEntity<?> getInstances() {
        return new ResponseEntity<>(discoveryClient.getInstances(serviceId), HttpStatus.OK);
    }

    @GetMapping("/service/services")
    public ResponseEntity<?> getServices() {
        return new ResponseEntity<>(discoveryClient.getServices(), HttpStatus.OK);
    }

    @PostMapping("/service/registration")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()) != null) {
            // Status Code: 409
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        // Default role = user
        user.setRole(Role.USER);
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @GetMapping("/service/signin")
    public ResponseEntity<?> getUser(Principal principal) {
        if (principal == null || principal.getName() == null) {
            // This means logout will be successful. login?logout
            return new ResponseEntity<>(HttpStatus.OK);
        }
        // username = principal.getName()
        return ResponseEntity.ok(userService.findByUsername(principal.getName()));
    }
    
    @PostMapping("/service/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,HttpServletRequest httpRequest) {
        // Use only the username and password for authentication
    	String username = httpRequest.getParameter("username");
        User user = userRepository.findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        System.out.println(loginRequest.getUsername()+" "+loginRequest.getPassword());
        System.out.println(user.getName());
        if (user != null) {
            // Exclude password from the response
            user.setPassword(null);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }


    // Changed to a POST request
    @GetMapping("/service/names")
    public ResponseEntity<?> getNamesOfUsers(@RequestParam List<Long> idList) {
        return ResponseEntity.ok(userService.findUsers(idList));
    }

    @GetMapping("/service/test")
    public ResponseEntity<?> testService() {
        return ResponseEntity.ok("It is working...");
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = (HttpSession) request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("Successfully logged out");
    }

}
