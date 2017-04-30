
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Coordinate;

@Repository
public interface CoordinateRepository extends JpaRepository<Coordinate, Integer> {

}
