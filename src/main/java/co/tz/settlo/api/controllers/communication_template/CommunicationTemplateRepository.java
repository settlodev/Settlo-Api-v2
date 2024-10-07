package co.tz.settlo.api.controllers.communication_template;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface CommunicationTemplateRepository extends JpaRepository<CommunicationTemplate, UUID>, JpaSpecificationExecutor<CommunicationTemplate> {
    List<CommunicationTemplate> findAllByLocationId(UUID locationId);
}
