package co.tz.settlo.api.communication_log;

import co.tz.settlo.api.campaign.Campaign;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface CommunicationLogRepository extends JpaRepository<CommunicationLog, UUID>, JpaSpecificationExecutor<CommunicationLog> {
    List<CommunicationLog> findAllByLocationId(UUID locationId);

    CommunicationLog findFirstByCampaign(Campaign campaign);

}
