package co.tz.settlo.api.campaign;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.communication_template.CommunicationTemplate;
import co.tz.settlo.api.location.Location;
import co.tz.settlo.api.sender_id.SenderId;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CampaignRepository extends JpaRepository<Campaign, UUID> {

    Campaign findFirstBySenderId(SenderId senderId);

    Campaign findFirstByCommunicationTemplate(CommunicationTemplate communicationTemplate);

    Campaign findFirstByBusiness(Business business);

    Campaign findFirstByLocation(Location location);

    boolean existsByNameIgnoreCase(String name);

}
