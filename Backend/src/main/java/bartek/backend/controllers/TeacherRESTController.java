package bartek.backend.controllers;

import bartek.backend.model.Role;
import bartek.backend.model.RoleName;
import bartek.backend.model.Teacher;
import bartek.backend.model.User;
import bartek.backend.repository.RoleRepository;
import bartek.backend.repository.TeacherRepository;
import bartek.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "http://localhost:4200")
public class TeacherRESTController {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TeacherRESTController(TeacherRepository teacherRepository,
                                 UserRepository userRepository,
                                 RoleRepository roleRepository,
                                 PasswordEncoder passwordEncoder) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
        Optional<Teacher> teacher = teacherRepository.findById(id);
        if (teacher.isPresent()) {
            return ResponseEntity.ok(teacher.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Teacher> addTeacher(@RequestBody Teacher teacher) {
        // Check if teacher with such name exists
        if (teacherRepository.findByFirstNameAndLastName(teacher.getFirstName(), teacher.getLastName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Creating user account for teacher
        User user = new User();
        user.setUsername(teacher.getFirstName().toLowerCase() + "." + teacher.getLastName().toLowerCase());
        user.setPassword(passwordEncoder.encode("password")); // encode temporary password

        // Give role ADMIN
        Set<Role> roles = new HashSet<>();
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("could not find role: ROLE_ADMIN"));
        roles.add(adminRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        // Assign user to teacher
        teacher.setUser(savedUser);
        Teacher savedTeacher = teacherRepository.save(teacher);

        return ResponseEntity.ok(savedTeacher);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, @RequestBody Teacher teacherDetails) {
        Optional<Teacher> teacherOptional = teacherRepository.findById(id);

        if (teacherOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Teacher teacher = teacherOptional.get();
        teacher.setFirstName(teacherDetails.getFirstName());
        teacher.setLastName(teacherDetails.getLastName());

        // Update user if it exists
        if (teacher.getUser() != null) {
            User user = teacher.getUser();
            // If name or last name was changed update it
            user.setUsername(teacherDetails.getFirstName().toLowerCase() + "." +
                    teacherDetails.getLastName().toLowerCase());
            userRepository.save(user);
        }

        Teacher updatedTeacher = teacherRepository.save(teacher);
        return ResponseEntity.ok(updatedTeacher);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        Optional<Teacher> teacherOptional = teacherRepository.findById(id);

        if (teacherOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Teacher teacher = teacherOptional.get();

        // Delete if it exists
        if (teacher.getUser() != null) {
            userRepository.delete(teacher.getUser());
        }

        teacherRepository.delete(teacher);
        return ResponseEntity.ok().build();
    }
}