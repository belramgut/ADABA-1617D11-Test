
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.TemplateRepository;
import domain.Chorbi;
import domain.Coordinate;
import domain.Template;

@Service
@Transactional
public class TemplateService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private TemplateRepository	templateRepository;

	@Autowired
	private ChorbiService		chorbiService;


	// Constructors -----------------------------------------------------------

	public TemplateService() {
		super();
	}

	//Creates -----------------------------------------------------------------
	public Template create() {
		Template res;
		res = new Template();
		final Collection<Chorbi> coll = this.chorbiService.findAllNotBannedChorbies2();
		res.setChorbies(coll);

		return res;
	}

	//Simple CRUD -------------------------------------------------------------

	public Template save(final Template t) {
		Assert.notNull(t);
		return this.templateRepository.save(t);

	}

	public Template saveEdit(final Template t) {
		Assert.notNull(t);

		t.setMoment(null);
		return this.templateRepository.save(t);
	}

	public void flush() {
		this.templateRepository.flush();
	}

	public Template saveAndFlush(final Template t) {
		Assert.notNull(t);
		return this.templateRepository.saveAndFlush(t);

	}

	// Other business methods ----------------------------------------------------

	public Collection<Chorbi> findChorbiesByMyTemplate(final Template t) {

		final Chorbi b = this.chorbiService.findByPrincipal();

		final Collection<Chorbi> todosLosChorbies = this.chorbiService.findAllNotBannedChorbies();
		todosLosChorbies.remove(b);
		final Collection<Chorbi> todosLosChorbies2 = this.chorbiService.findAllNotBannedChorbies();
		todosLosChorbies2.remove(b);

		if (t.getGenre() != null)
			for (final Chorbi c : todosLosChorbies)
				if (!t.getGenre().equals(c.getGenre()))
					todosLosChorbies2.remove(c);

		if (t.getRelationShip() != null)
			for (final Chorbi c : todosLosChorbies)
				if (!t.getRelationShip().equals(c.getRelationship()))
					todosLosChorbies2.remove(c);

		if (t.getApproximatedAge() != null)
			for (final Chorbi c : todosLosChorbies) {
				final Integer r = this.chorbiService.getEdad(c.getBirthDate());
				if ((t.getApproximatedAge() > this.chorbiService.getEdad(c.getBirthDate()) + 5))
					todosLosChorbies2.remove(c);
				if ((t.getApproximatedAge() < this.chorbiService.getEdad(c.getBirthDate()) - 5))
					todosLosChorbies2.remove(c);
			}
		if (t.getCoordinate() != null) {
			final Coordinate c = t.getCoordinate();

			if ((!c.getCity().equals("")) && (!c.getCountry().equals("")) && (!c.getProvince().equals("")) && (!c.getState().equals(""))) {
				final Collection<Chorbi> c1 = this.chorbiService.findByAllCoordinate(c.getCountry(), c.getState(), c.getProvince(), c.getCity());
				c1.remove(b);
				for (final Chorbi chorbi : c1)
					if (!todosLosChorbies2.contains(chorbi))
						todosLosChorbies2.remove(chorbi);
			}

			if ((!c.getCity().equals("")) && (!c.getCountry().equals("")) && (!c.getProvince().equals(""))) {
				final Collection<Chorbi> c2 = this.chorbiService.findByCountryProvinceCity(c.getCountry(), c.getProvince(), c.getCity());
				c2.remove(b);
				for (final Chorbi chorbi : c2)
					if (!todosLosChorbies2.contains(chorbi))
						todosLosChorbies2.remove(chorbi);

			}
			if ((!c.getCity().equals("")) && (!c.getCountry().equals("")) && (!c.getState().equals(""))) {
				final Collection<Chorbi> c3 = this.chorbiService.findByCountryStateCity(c.getCountry(), c.getState(), c.getCity());
				c3.remove(b);
				for (final Chorbi chorbi : c3)
					if (!todosLosChorbies2.contains(chorbi))
						todosLosChorbies2.remove(chorbi);
			}
			if ((!c.getCity().equals("")) && (!c.getCountry().equals(""))) {
				final Collection<Chorbi> c4 = this.chorbiService.findByCountryCity(c.getCountry(), c.getCity());
				c4.remove(b);
				for (final Chorbi chorbi : c4)
					if (!todosLosChorbies2.contains(chorbi))
						todosLosChorbies2.remove(chorbi);
			}
		}

		return todosLosChorbies2;
	}

	public Template reconstruct(final Template template) {
		Template result;

		//result = new Template();
		final Chorbi c = this.chorbiService.findByPrincipal();
		result = c.getTemplate();

		result.setGenre(template.getGenre());
		result.setRelationShip(template.getRelationShip());
		result.setApproximatedAge(template.getApproximatedAge());
		result.setKeyword(template.getKeyword());

		return result;

	}

	public void checkPrincipal(final Template template) {
		final Chorbi chorbi = this.chorbiService.findByPrincipal();
		Assert.isTrue(chorbi.getTemplate().getId() == template.getId(), "This template belong to another chorbi");

	}

	public Template findOneToEdit(final int templateId) {
		final Template res = this.templateRepository.findOne(templateId);
		this.checkPrincipal(res);
		return res;
	}
}
