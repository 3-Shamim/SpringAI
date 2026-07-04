package fyi.shamim.postureservice.repository;

import fyi.shamim.postureservice.model.SecurityPostureEntity;
import fyi.shamim.postureservice.model.SecurityPostureId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/27/26
 * Email: mdshamim723@gmail.com
 */

@Repository
public interface SecurityPostureRepository extends JpaRepository<SecurityPostureEntity, SecurityPostureId> {

    Optional<SecurityPostureEntity> findByServiceIdAndEnvironment(String serviceId, String environment);

}
