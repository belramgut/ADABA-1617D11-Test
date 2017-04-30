
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Taste;

@Repository
public interface TasteRepository extends JpaRepository<Taste, Integer> {

	@Query("select (select sum(t.stars) from Taste t where t.chorbi.id=c.id) as auxiliar from Chorbi c order by auxiliar ASC")
	Collection<Long> minMaxStars();

	@Query("select sum(t.stars)*1.0/(select count(c)*1.0 from Chorbi c) from Taste t")
	Double avgStars();

}
