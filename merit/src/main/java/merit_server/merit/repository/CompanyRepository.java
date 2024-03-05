package merit_server.merit.repository;

import merit_server.merit.domain.Company;
import merit_server.merit.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
