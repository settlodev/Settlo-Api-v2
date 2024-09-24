package co.tz.settlo.api.sender_id;

import co.tz.settlo.api.business.Business;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SenderIdRepository extends JpaRepository<SenderId, UUID> {

    SenderId findFirstByBusiness(Business business);

    boolean existsByNameIgnoreCase(String name);

}
