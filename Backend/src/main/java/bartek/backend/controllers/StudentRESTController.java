package bartek.backend.controllers;

import bartek.backend.model.Role;
import bartek.backend.model.RoleName;
import bartek.backend.model.Student;
import bartek.backend.model.User;
import bartek.backend.repository.RoleRepository;
import bartek.backend.repository.StudentRepository;
import bartek.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/students")
public class StudentRESTController {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentRESTController(StudentRepository studentRepository,
                                 UserRepository userRepository,
                                 RoleRepository roleRepository,
                                 PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        return studentOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student savedStudent = studentRepository.save(student);

        User user = new User();
        user.setUsername(student.getFirstname().toLowerCase() + "." + student.getLastname().toLowerCase());
        user.setPassword(passwordEncoder.encode("default123"));

        // assign user role
        Optional<Role> userRole = roleRepository.findByName(RoleName.ROLE_USER);
        if (userRole.isPresent()) {
            Set<Role> roles = new HashSet<>();
            roles.add(userRole.get());
            user.setRoles(roles);

            User savedUser = userRepository.save(user);

            // assign user to student
            savedStudent.setUser(savedUser);
            studentRepository.save(savedStudent);
        }

        return ResponseEntity.ok(savedStudent);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        Optional<Student> studentOptional = studentRepository.findById(id);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            student.setFirstname(studentDetails.getFirstname());
            student.setLastname(studentDetails.getLastname());
            student.setEmail(studentDetails.getEmail());
            student.setTelephone(studentDetails.getTelephone());

            Student updatedStudent = studentRepository.save(student);
            return ResponseEntity.ok(updatedStudent);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (studentRepository.existsById(id)) {
            // get student to check if it has user
            Optional<Student> studentOptional = studentRepository.findById(id);
            if (studentOptional.isPresent() && studentOptional.get().getUser() != null) {
                // delete related user
                userRepository.deleteById(studentOptional.get().getUser().getId());
            }

            studentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}