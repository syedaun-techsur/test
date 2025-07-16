package solutions.techsur.rfpaiservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import solutions.techsur.rfpaiservice.entity.ResponseOutline;

@Repository
public interface ResponseOutlineRepository extends JpaRepository<ResponseOutline, Integer>, JpaSpecificationExecutor<ResponseOutline> {
}
