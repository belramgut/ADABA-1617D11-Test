
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chirp;

@Repository
public interface ChirpRepository extends JpaRepository<Chirp, Integer> {

	//chorbi 1 259
	//manager 1 250 
	@Query("select m from Actor c join c.chirpReceives m where m.copy=false and c.id=?1")
	Collection<Chirp> myRecivedMessages(int actorId);

	@Query("select m from Actor c join c.chirpWrites m where c.id=?1")
	Collection<Chirp> mySendedMessages(int actorId);

	//Dashboard

	@Query("select min(c.chirpReceives.size),avg(c.chirpReceives.size),max(c.chirpReceives.size) from Chorbi c")
	Collection<Object[]> minAvgMaxChirpsReceived();

	@Query("select min(c.chirpWrites.size),avg(c.chirpWrites.size),max(c.chirpWrites.size) from Chorbi c")
	Collection<Object[]> minAvgMaxChirpsSent();

}
