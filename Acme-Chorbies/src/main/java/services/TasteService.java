
package services;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.TasteRepository;
import domain.Actor;
import domain.Chorbi;
import domain.Taste;

@Service
@Transactional
public class TasteService {

	// ---------- Repositories----------------------

	@Autowired
	private TasteRepository	tasteRepository;

	// ---------- Services ----------------------

	@Autowired
	private ChorbiService	chorbiService;


	// Simple CRUD methods ----------------------------------------------------

	public Taste create(final Chorbi chorbiToLike) {
		Assert.notNull(chorbiToLike);

		final Actor principal = this.chorbiService.findByPrincipal();
		Assert.notNull(principal);

		final Taste t = new Taste();
		t.setChorbi(chorbiToLike);

		return t;
	}

	public Taste save(final Taste t) {
		Taste result = null;
		Assert.notNull(t);

		final Chorbi principal = this.chorbiService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(t.getChorbi().getId() != principal.getId());
		final Collection<Chorbi> chorbiesWithMyLike = this.chorbiService.findAllChorbiesWhoLikedByThisUserForNotDoubleLike(principal);
		Assert.isTrue(!chorbiesWithMyLike.contains(t.getChorbi()));
		Assert.isTrue(t.getChorbi().isBan() == false);
		Assert.isTrue(t.getStars() <= 3);
		if (!(t.getComment() == null))
			t.setComment(this.checkContactInfo(t.getComment()));
		result = this.tasteRepository.saveAndFlush(t);

		principal.getGivenTastes().add(result);

		return result;

	}

	public Taste reconstruct(final Taste taste, final BindingResult binding, final Chorbi chorbiToLike) {

		Taste result;

		result = taste;
		final Date actual = new Date();
		result.setMoment(actual);
		result.setChorbi(chorbiToLike);

		return result;
	}

	public void delete(final Chorbi chorbi) {
		Assert.notNull(chorbi);
		final Chorbi principal = this.chorbiService.findByPrincipal();
		Assert.notNull(principal);
		Assert.notNull(chorbi.getId() != principal.getId());
		Assert.isTrue(this.chorbiService.findAllChorbiesWhoLikedByThisUserForNotDoubleLike(principal).contains(chorbi));

		for (final Taste t : principal.getGivenTastes())
			if (t.getChorbi().getId() == chorbi.getId()) {
				Assert.isTrue(t.getChorbi().isBan() == false);
				principal.getGivenTastes().remove(t);
				this.tasteRepository.delete(t);
				break;
			}
	}

	public void flush() {
		this.tasteRepository.flush();

	}

	public Collection<Taste> findAll() {

		final Collection<Taste> t = this.tasteRepository.findAll();
		Assert.notNull(t);
		return t;

	}

	public Taste findOne(final int id) {
		Taste t;
		t = this.tasteRepository.findOne(id);
		Assert.notNull(t);
		return t;
	}

	public String checkContactInfo(final String cadena) {

		String res = "";

		if (cadena.contains("@")) {
			final String[] trozos = cadena.split(" ");
			for (String t : trozos)
				if (t.contains("@")) {
					t = "***@***";
					res = res + " " + t;
				} else
					res = res + " " + t;
		} else
			res = cadena;

		final char[] res2 = cadena.toCharArray();
		int contador = 0;
		for (int i = 0; i < res.length(); i++)
			if (Character.isDigit(res2[i]))
				contador = contador + 1;

		if (contador >= 6) {
			String copia = "";
			final char c_aux = '*';
			for (int i = 0; i < res.length(); i++)
				if (Character.isDigit(res.charAt(i)))
					copia = copia + c_aux;
				else
					copia = copia + res.charAt(i);

			res = copia;

		}

		return res;

	}

	// Dashboard 2.0 

	public Double avgStars() {
		final Double res = this.tasteRepository.avgStars();
		return res;
	}

	public Long minStars() {
		Long res = null;
		final List<Long> maxMin = (List<Long>) this.tasteRepository.minMaxStars();
		for (final Long i : maxMin)
			if (i != null) {
				res = i;
				break;
			}
		return res;
	}

	public Long maxStars() {
		Long res = null;
		final List<Long> maxMin = (List<Long>) this.tasteRepository.minMaxStars();
		res = maxMin.get(maxMin.size() - 1);
		return res;
	}

}
