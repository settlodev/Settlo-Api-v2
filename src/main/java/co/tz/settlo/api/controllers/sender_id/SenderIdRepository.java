package co.tz.settlo.api.controllers.sender_id;

import co.tz.settlo.api.controllers.business.Business;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface SenderIdRepository extends JpaRepository<SenderId, UUID>, JpaSpecificationExecutor<SenderId> {

    List<SenderId> findAllByLocationId(UUID locationId);

    SenderId findFirstByBusiness(Business business);

    boolean existsByNameIgnoreCase(String name);

}
