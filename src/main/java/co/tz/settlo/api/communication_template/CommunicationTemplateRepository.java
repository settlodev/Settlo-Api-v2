package co.tz.settlo.api.communication_template;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommunicationTemplateRepository extends JpaRepository<CommunicationTemplate, UUID> {
}
