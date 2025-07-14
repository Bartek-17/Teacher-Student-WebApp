package bartek.backend.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/exampleSecurity")
public class ExampleSecurityRESTController {

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userAccess(Authentication authentication) {
        String username = authentication.getName();
        String roles = authentication.getAuthorities().toString();

        return "Hello, " + username + "!\n" +
                "Username: " + username + "\n" +
                "Roles: " + roles + "\n" +
                "\n" +
                "You can:\n" +
                "- See all available subjects\n" +
                "- View your own grades\n";
    }


    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess(Authentication authentication) {
        String username = authentication.getName();
        String roles = authentication.getAuthorities().toString();

        return "Welcome, admin " + username + "!\n" +
                "Username: " + username + "\n" +
                "Roles: " + roles + "\n" +
                "\n" +
                "You can:\n" +
                "- View your profile information and user page\n" +
                "- View all grades in the system\n" +
                "- Add a grade for a student\n" +
                "- Search for a subject by name\n" +
                "- Add new subjects\n" +
                "- See all available subjects\n";
    }
}