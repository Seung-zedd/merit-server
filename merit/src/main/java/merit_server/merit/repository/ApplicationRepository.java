package merit_server.merit.repository;

import merit_server.merit.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
