
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Chorbi;
import domain.Template;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class TemplateTest extends AbstractTest {

	@Autowired
	private TemplateService	templateService;

	@Autowired
	private ChorbiService	chorbiService;


	//findChorbiesByMyTemplate
	@Test
	public void findOneChirp() {

		this.authenticate("chorbi1");

		final Chorbi c = this.chorbiService.findOne(63);

		final Template t = c.getTemplate();

		final Collection<Chorbi> cc = this.templateService.findChorbiesByMyTemplate(t);

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------Find Chorbies By My Template------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("-----------------Para el Template del chorbi 1, se esperan los datos de Chorbi 2--------------");

		for (final Chorbi c1 : cc) {
			System.out.println("ID: " + c1.getId());
			System.out.println("User Name: " + c1.getUserAccount().getUsername());
			System.out.println("Name: " + c1.getName());
			System.out.println("Surname: " + c1.getSurName());
		}

		this.unauthenticate();

	}

}
