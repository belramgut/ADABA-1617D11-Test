
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

	@Query("select e from Event e where MONTH(e.moment) is MONTH(CURRENT_DATE) and YEAR(e.moment) is YEAR(CURRENT_DATE) and e.numberSeatsOffered >=1")
	Collection<Event> listEventMonthSeatsFree();
}
