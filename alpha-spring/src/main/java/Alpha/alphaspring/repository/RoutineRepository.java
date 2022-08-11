package Alpha.alphaspring.repository;

import Alpha.alphaspring.domain.Routine;
import Alpha.alphaspring.domain.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    Routine save(Routine routine);
    Optional<Routine> findById(String id);
    Optional<Routine> findByName(String name);
    List<Routine> findAll();
    Optional<Routine> findByUser_Id(Long userId);
}