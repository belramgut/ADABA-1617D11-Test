
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Template;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Integer> {

}
