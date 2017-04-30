
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ManagerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.CreditCard;
import domain.Manager;
import form.RegistrationFormManager;

@Service
@Transactional
public class ManagerService {

	// ---------- Repositories----------------------

	@Autowired
	private ManagerRepository	managerRepository;

	// Supporting services ------------------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private Md5PasswordEncoder	encoder;

	@Autowired
	private CreditCardService	creditCardService;


	// Simple CRUD methods ----------------------------------------------------

	public Manager create() {
		Manager result;
		result = new Manager();
		return result;
	}

	public Manager reconstruct(final RegistrationFormManager form) {
		Manager result;
		UserAccount userAccount;
		Authority authority;
		Collection<Authority> authorities;
		String pwdHash;

		result = this.create();
		authorities = new HashSet<Authority>();
		userAccount = new UserAccount();

		result.setName(form.getName());
		result.setSurName(form.getSurName());
		result.setPhone(form.getPhone());
		result.setEmail(form.getEmail());

		result.setCompany(form.getCompany());
		result.setVatNumber(form.getVatNumber());

		authority = new Authority();
		authority.setAuthority(Authority.MANAGER);
		authorities.add(authority);
		pwdHash = this.encoder.encodePassword(form.getPassword(), null);
		userAccount.setAuthorities(authorities);
		userAccount.setPassword(pwdHash);
		userAccount.setUsername(form.getUsername());
		result.setUserAccount(userAccount);

		return result;

	}

	public Manager findOne(final int managerId) {
		Manager res;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		res = this.managerRepository.findOne(managerId);
		Assert.notNull(res);

		return res;
	}

	public Collection<Manager> findAll() {
		Collection<Manager> res;
		res = this.managerRepository.findAll();
		return res;
	}

	public Manager save(final Manager manager) {
		Assert.notNull(manager);
		return this.managerRepository.save(manager);

	}

	public Manager saveAndFlush2(Manager manager, CreditCard c) {
		Assert.notNull(manager);
		Assert.notNull(c);

		if (manager.getId() != 0) {

			c = this.creditCardService.saveAndFlush(c);
			manager.setCreditCard(c);
			this.save(manager);
		} else
			manager = this.save(manager);
		return manager;

	}

	public Manager findOneToSent(final int managerId) {

		Manager result;

		result = this.managerRepository.findOne(managerId);
		Assert.notNull(result);

		return result;

	}

	// Other business methods ----------------------------------------------------

	public Manager findByPrincipal() {
		Manager result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		result = this.findByUserAccount(userAccount);

		return result;
	}

	public Manager findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		Manager result;

		result = this.managerRepository.findByUserAccountId(userAccount.getId());

		return result;
	}

	public void flush() {
		this.managerRepository.flush();
	}

	// Dashboard 2.0

	public Collection<Manager> listingManagersSortedByNumbeOfEventsTheyOrganise() {
		final Collection<Manager> res = this.managerRepository.listingManagersSortedByNumbeOfEventsTheyOrganise();
		return res;
	}

	public Collection<Object[]> listingManagersAndAmountTheyDueInFees() {
		final Collection<Object[]> res = this.managerRepository.listingManagersAndAmountTheyDueInFees();
		return res;
	}

}
