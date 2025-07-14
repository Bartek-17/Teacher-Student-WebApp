package bartek.backend.controllers;

import bartek.backend.model.Student;
import bartek.backend.model.Subject;
import bartek.backend.model.Teacher;
import bartek.backend.model.User;
import bartek.backend.repository.StudentRepository;
import bartek.backend.repository.SubjectRepository;
import bartek.backend.repository.TeacherRepository;
import bartek.backend.repository.UserRepository;
import bartek.backend.security.services.UserPrinciple;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "http://localhost:4200")
public class SubjectRESTController {

    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public SubjectRESTController(SubjectRepository subjectRepository,
                                 TeacherRepository teacherRepository,
                                 StudentRepository studentRepository,
                                 UserRepository userRepository) {
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    // get subjects of logged in user
    @GetMapping("/my-subjects")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Subject>> getMySubjects() {
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(userPrinciple.getUsername())
                .orElseThrow(() -> new RuntimeException("Did not find the user"));

        Optional<Student> studentOptional = studentRepository.findByUserId(user.getId());
        if (!studentOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ArrayList<>());
        }

        Student student = studentOptional.get();
        return ResponseEntity.ok(student.getSubjects());
    }

    @GetMapping("/my-teaching-subjects")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Subject>> getMyTeachingSubjects() {
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(userPrinciple.getUsername())
                .orElseThrow(() -> new RuntimeException("Did not find the user"));

        Optional<Teacher> teacherOptional = teacherRepository.findByUserId(user.getId());
        if (!teacherOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ArrayList<>());
        }

        Teacher teacher = teacherOptional.get();
        List<Subject> subjects = subjectRepository.findByTeacherId(teacher.getId());
        return ResponseEntity.ok(subjects);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject) {
        // get logged in user
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(userPrinciple.getUsername())
                .orElseThrow(() -> new RuntimeException("Could not find the user"));

        // find teacher related logged in user
        Optional<Teacher> teacherOptional = teacherRepository.findByUserId(user.getId());
        if (teacherOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // assing teacher to the subject
        subject.setTeacher(teacherOptional.get());

        Subject savedSubject = subjectRepository.save(subject);
        return ResponseEntity.ok(savedSubject);
    }

    @PostMapping("/{subjectId}/students/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> enrollStudentToSubject(@PathVariable Long subjectId, @PathVariable Long studentId) {
        Optional<Subject> subjectOptional = subjectRepository.findById(subjectId);
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if (!subjectOptional.isPresent() || !studentOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Subject subject = subjectOptional.get();
        Student student = studentOptional.get();

        // check if student is already enrolled
        if (subject.getStudents() != null && subject.getStudents().contains(student)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Student is already enrolled on this subject");
        }

        // add student to the subject
        if (subject.getStudents() == null) {
            subject.setStudents(new ArrayList<>());
        }
        subject.getStudents().add(student);

        // add subject for the student
        if (student.getSubjects() == null) {
            student.setSubjects(new ArrayList<>());
        }
        student.getSubjects().add(subject);

        subjectRepository.save(subject);
        studentRepository.save(student);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{subjectId}/students/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeStudentFromSubject(@PathVariable Long subjectId, @PathVariable Long studentId) {
        Optional<Subject> subjectOptional = subjectRepository.findById(subjectId);
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if (!subjectOptional.isPresent() || !studentOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Subject subject = subjectOptional.get();
        Student student = studentOptional.get();

        if (subject.getStudents() == null || !subject.getStudents().contains(student)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Student not enrolled on this subject");
        }

        // remove student from the subject
        subject.getStudents().remove(student);

        // remove subject from the student
        if (student.getSubjects() != null) {
            student.getSubjects().remove(subject);
        }

        subjectRepository.save(subject);
        studentRepository.save(student);

        return ResponseEntity.ok().build();
    }
}