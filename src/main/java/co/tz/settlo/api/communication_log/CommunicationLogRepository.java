package co.tz.settlo.api.communication_log;

import co.tz.settlo.api.campaign.Campaign;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommunicationLogRepository extends JpaRepository<CommunicationLog, UUID> {

    CommunicationLog findFirstByCampaign(Campaign campaign);

}
