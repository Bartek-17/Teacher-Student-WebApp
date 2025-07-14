package bartek.backend.repository;

import bartek.backend.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    // find teacher by user id
    Optional<Teacher> findByUserId(Long userId);

    Optional<Teacher> findByFirstNameAndLastName(String firstName, String lastName);
}