package co.tz.settlo.api.controllers.campaign;

import co.tz.settlo.api.controllers.business.Business;
import co.tz.settlo.api.controllers.communication_template.CommunicationTemplate;
import co.tz.settlo.api.controllers.location.Location;
import co.tz.settlo.api.controllers.sender_id.SenderId;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface  CampaignRepository  extends JpaRepository<Campaign, UUID>, JpaSpecificationExecutor<Campaign> {

    List<Campaign> findAllByLocationId(UUID locationId);

    Campaign findFirstBySenderId(SenderId senderId);

    Campaign findFirstByCommunicationTemplate(CommunicationTemplate communicationTemplate);

    Campaign findFirstByBusiness(Business business);

    Campaign findFirstByLocation(Location location);

    boolean existsByNameIgnoreCase(String name);

}
