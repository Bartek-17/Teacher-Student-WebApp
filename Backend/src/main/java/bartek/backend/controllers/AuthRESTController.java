package bartek.backend.controllers;

import bartek.backend.message.request.LoginForm;
import bartek.backend.message.request.SignUpForm;
import bartek.backend.message.response.JwtResponse;
import bartek.backend.message.response.ResponseMessage;
import bartek.backend.model.Role;
import bartek.backend.model.RoleName;
import bartek.backend.model.User;
import bartek.backend.repository.RoleRepository;
import bartek.backend.repository.UserRepository;
import bartek.backend.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/auth")
public class AuthRESTController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthRESTController(AuthenticationManager authenticationManager, UserRepository userRepository,
                              RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginForm loginRequest) {
        try {
            // Logging in incoming data
            System.out.println("Authentication attempt for username: " + loginRequest.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtProvider.generateJwtToken(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            System.out.println("Login successful! JWT Token: " + jwt);

            return ResponseEntity.ok(
                    new JwtResponse(
                            jwt,
                            userDetails.getUsername(),
                            userDetails.getAuthorities()
                    )
            );
        } catch (Exception e) {
            System.err.println("Authentication failed: " + e.getMessage());
            return ResponseEntity.status(401).body("Error: Unauthorized");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Validated @RequestBody SignUpForm signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ResponseMessage("Error: Username is already taken!"));
        }

        User user = new User(
                signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword()) // password encoding
        );

        // get roles
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            // if no role was given assign user role
            Role defaultRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role 'USER' is not found in the database."));
            roles.add(defaultRole);
        } else {
            // adding roles based on received list
            strRoles.forEach(role -> {
                RoleName roleName;
                try {
                    // convert string value to role name
                    roleName = RoleName.valueOf(role);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Error: Invalid role name -> " + role);
                }

                Role roleEntity = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Error: Role '" + role + "' is not found in the database."));
                roles.add(roleEntity);
            });
        }

        user.setRoles(roles);

        userRepository.save(user);

        return ResponseEntity.ok(new ResponseMessage("User registered successfully!"));
    }
}