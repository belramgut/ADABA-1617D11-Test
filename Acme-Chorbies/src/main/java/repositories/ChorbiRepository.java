
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chorbi;
import domain.Taste;

@Repository
public interface ChorbiRepository extends JpaRepository<Chorbi, Integer> {

	@Query("select c from Chorbi c where c.userAccount.id = ?1")
	Chorbi findByUserAccountId(int userAccountId);

	@Query("select c from Chorbi c where c.ban=false")
	Collection<Chorbi> findAllNotBannedChorbies();

	@Query("select c from Chorbi c join c.givenTastes t where t.chorbi.id=?1 and c.ban=false")
	Collection<Chorbi> findAllChorbiesWhoLikeThem(int chorbiId);

	@Query("select c2 from Chorbi c join c.givenTastes t join t.chorbi c2 where c.id=?1 and c2.ban=0")
	Collection<Chorbi> findAllChorbiesWhoLikedByThisUser(int chorbiId);

	@Query("select c2 from Chorbi c join c.givenTastes t join t.chorbi c2 where c.id=?1")
	Collection<Chorbi> findAllChorbiesWhoLikedByThisUserForNotDoubleLike(int chorbiId);

	@Query("select t from Chorbi c join c.givenTastes t where t.chorbi.ban=0 and c.id=?1")
	Collection<Taste> findAllMyTastesWithoutBannedChorbies(int chorbiId);

	@Query("select c from Chorbi c join c.givenTastes t where t.chorbi.id=?1 and c.ban=0")
	Collection<Chorbi> findAllTastesToMeWithoutBannedChorbies(int chorbiId);

	@Query("select c from Chorbi c where c.coordinate.country like %?1% and c.coordinate.state like %?2% and c.coordinate.province like %?3%  and c.coordinate.city like %?4%")
	Collection<Chorbi> findByAllCoordinate(String country, String state, String province, String city);

	@Query("select c from Chorbi c where c.coordinate.country like %?1% and c.coordinate.province like %?2%  and c.coordinate.city like %?3%")
	Collection<Chorbi> findByCountryProvinceCity(String country, String province, String city);

	@Query("select c from Chorbi c where c.coordinate.country like %?1% and c.coordinate.state like %?2%  and c.coordinate.city like %?3%")
	Collection<Chorbi> findByCountryStateCity(String country, String state, String city);

	@Query("select c from Chorbi c where c.coordinate.country like %?1% and c.coordinate.city like %?2%")
	Collection<Chorbi> findByCountryCity(String country, String city);

	//Dashboard

	@Query("select c.coordinate.country,c.coordinate.city, count(c) from Chorbi c group by c.coordinate.city,c.coordinate.country")
	Collection<Object[]> listingWithTheNumberOfChorbiesPerCountryAndCity();

	@Query("select min(YEAR(CURRENT_DATE)-YEAR(c.birthDate)),avg(YEAR(CURRENT_DATE)-YEAR(c.birthDate)),max(YEAR(CURRENT_DATE)-YEAR(c.birthDate)) from Chorbi c")
	Collection<Object[]> minimumMaximumAverageAgesOfTheChorbies();

	@Query("select count(c)*1.0/(select count(c)*1.0 from Chorbi c) from Chorbi c where c.id in (select c.id from Chorbi c where c.creditCard is null) or c.id in (select c.id from Chorbi c where c.creditCard.expirationYear < YEAR(CURRENT_DATE))")
	Double ratioChorbiesWhoHaveNoRegisteredACreditCardOrHaveRegisteredAnInvalidCreditCard();

	@Query("select count(c)*1.0/(select count(c)*1.0 from Chorbi c)from Chorbi c where c.relationship='ACTIVITIES'")
	Double ratiosOfChorbiesWhoSearchActivities();

	@Query("select count(c)*1.0/(select count(c)*1.0 from Chorbi c)from Chorbi c where c.relationship='LOVE'")
	Double ratiosOfChorbiesWhoSearchLove();

	@Query("select count(c)*1.0/(select count(c)*1.0 from Chorbi c)from Chorbi c where c.relationship='FRIENDSHIP'")
	Double ratiosOfChorbiesWhoSearchFriendship();

	@Query("select c.name, c.surName,(select count(t) from Taste t where t.chorbi.id=c.id) as cuenta from Chorbi c order by cuenta DESC")
	Collection<Object[]> listOfChorbiesCortedByTheNumberOfLikesTheyHaveGot();

	@Query("select count(t) from Taste t group by t.chorbi.id having count(t)<= ALL (select count(t1) from Taste t1 group by t1.chorbi.id)")
	Collection<Double> minOfLikesPerChorbie();

	@Query("select count(t) from Taste t group by t.chorbi.id having count(t)>= ALL (select count(t1) from Taste t1 group by t1.chorbi.id)")
	Collection<Double> maxOfLikesPerChorbie();

	@Query("select count(t)*1.0/(select count(c)*1.0 from Chorbi c) from Taste t")
	Double averageLikesPerChorbi();

	@Query("select c from Chorbi c where c.chirpReceives.size=(select max(c1.chirpReceives.size) from Chorbi c1)")
	Collection<Chorbi> theChorbiesWhoHaveGotMoreChirps();

	@Query("select c from Chorbi c where c.chirpWrites.size=(select max(c1.chirpWrites.size) from Chorbi c1)")
	Collection<Chorbi> theChorbiesWhoHaveSentMoreChirps();

	// Dashboard 2.0

	@Query("select c from Chorbi c order by c.events.size DESC")
	Collection<Chorbi> listingChorbiesSortedByNumberOfEventsRegistered();

	@Query("select c.name, c.totalChargedFee from Chorbi c")
	Collection<Object[]> listingChorbiesAndAmountTheyDueInFees();

	@Query("select c.name,(select avg(t.stars) from Taste t where t.chorbi.id=c.id)as auxiliar from Chorbi c order by auxiliar")
	Collection<Object[]> listChorbiesSortedByAverageNumberStarsThatTheyHaveeGot();
}
