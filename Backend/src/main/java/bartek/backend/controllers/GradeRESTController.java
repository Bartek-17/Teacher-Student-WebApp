package bartek.backend.controllers;

import bartek.backend.model.Grade;
import bartek.backend.model.Student;
import bartek.backend.model.Subject;
import bartek.backend.model.User;
import bartek.backend.repository.GradeRepository;
import bartek.backend.repository.StudentRepository;
import bartek.backend.repository.SubjectRepository;
import bartek.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import bartek.backend.security.services.UserPrinciple;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/grades")
@CrossOrigin(origins = "http://localhost:4200")
public class GradeRESTController {

    private final GradeRepository gradeRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public GradeRESTController(GradeRepository gradeRepository,
                               SubjectRepository subjectRepository,
                               StudentRepository studentRepository,
                               UserRepository userRepository) {
        this.gradeRepository = gradeRepository;
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    @GetMapping("/my-grades")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Grade>> getGradesForCurrentUser() {
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userPrinciple.getId();

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent() && userOptional.get().getStudent() != null) {
            Student student = userOptional.get().getStudent();
            List<Grade> grades = gradeRepository.findByStudentId(student.getId());
            return ResponseEntity.ok(grades);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/my-grades/subject/{subjectId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Grade>> getGradesForSubject(@PathVariable Long subjectId) {
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userPrinciple.getId();

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent() && userOptional.get().getStudent() != null) {
            Student student = userOptional.get().getStudent();
            List<Grade> grades = gradeRepository.findByStudentIdAndSubjectId(student.getId(), subjectId);
            return ResponseEntity.ok(grades);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Grade> getGradeById(@PathVariable Long id) {
        Optional<Grade> grade = gradeRepository.findById(id);

        if (grade.isPresent()) {
            UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId = userPrinciple.getId();

            // check if grade belongs to logged in user or role is admin
            if (userRepository.findById(userId).get().getRoles().stream().anyMatch(r -> r.getName().name().equals("ROLE_ADMIN")) ||
                    (userRepository.findById(userId).get().getStudent() != null &&
                            userRepository.findById(userId).get().getStudent().getId() == grade.get().getStudent().getId())) {
                return ResponseEntity.ok(grade.get());
            }
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Grade> addGrade(@RequestBody Grade grade) {
        Grade savedGrade = gradeRepository.save(grade);
        return ResponseEntity.ok(savedGrade);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/subjects/{subjectId}/students/{studentId}")
    public ResponseEntity<Grade> addGradeForStudent(
            @PathVariable Long subjectId,
            @PathVariable Long studentId,
            @RequestParam Double score) {

        // only allow full and half grades
        if (score < 1.0 || score > 6.0 || (score * 10) % 5 != 0) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Subject> subjectOptional = subjectRepository.findById(subjectId);
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if (subjectOptional.isPresent() && studentOptional.isPresent()) {
            Subject subject = subjectOptional.get();
            Student student = studentOptional.get();

            // enrol student to the subject
            if (!student.getSubjects().contains(subject)) {
                student.getSubjects().add(subject);
                studentRepository.save(student);
            }

            Grade grade = new Grade();
            grade.setScore(score);
            grade.setSubject(subject);
            grade.setStudent(student);

            Grade savedGrade = gradeRepository.save(grade);
            return ResponseEntity.ok(savedGrade);
        }

        return ResponseEntity.notFound().build();
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Grade> updateGrade(@PathVariable Long id, @RequestBody Grade gradeDetails) {
        Optional<Grade> gradeOptional = gradeRepository.findById(id);

        if (gradeOptional.isPresent()) {
            Grade grade = gradeOptional.get();
            grade.setScore(gradeDetails.getScore());

            Grade updatedGrade = gradeRepository.save(grade);
            return ResponseEntity.ok(updatedGrade);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        if (gradeRepository.existsById(id)) {
            gradeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}