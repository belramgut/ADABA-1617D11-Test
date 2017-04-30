
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {

	@Query("select m from Manager m where m.userAccount.id = ?1")
	Manager findByUserAccountId(int userAccountId);

	// Dashboard 2.0
	@Query("select m from Manager m order by m.events.size DESC")
	Collection<Manager> listingManagersSortedByNumbeOfEventsTheyOrganise();

	@Query("select m.name, m.totalChargedFee from Manager m")
	Collection<Object[]> listingManagersAndAmountTheyDueInFees();

}
