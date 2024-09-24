package co.tz.settlo.api.communication_template;

import co.tz.settlo.api.campaign.Campaign;
import co.tz.settlo.api.campaign.CampaignRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CommunicationTemplateService {

    private final CommunicationTemplateRepository communicationTemplateRepository;
    private final CampaignRepository campaignRepository;

    public CommunicationTemplateService(
            final CommunicationTemplateRepository communicationTemplateRepository,
            final CampaignRepository campaignRepository) {
        this.communicationTemplateRepository = communicationTemplateRepository;
        this.campaignRepository = campaignRepository;
    }

    public List<CommunicationTemplateDTO> findAll() {
        final List<CommunicationTemplate> communicationTemplates = communicationTemplateRepository.findAll(Sort.by("id"));
        return communicationTemplates.stream()
                .map(communicationTemplate -> mapToDTO(communicationTemplate, new CommunicationTemplateDTO()))
                .toList();
    }

    public CommunicationTemplateDTO get(final UUID id) {
        return communicationTemplateRepository.findById(id)
                .map(communicationTemplate -> mapToDTO(communicationTemplate, new CommunicationTemplateDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final CommunicationTemplateDTO communicationTemplateDTO) {
        final CommunicationTemplate communicationTemplate = new CommunicationTemplate();
        mapToEntity(communicationTemplateDTO, communicationTemplate);
        return communicationTemplateRepository.save(communicationTemplate).getId();
    }

    public void update(final UUID id, final CommunicationTemplateDTO communicationTemplateDTO) {
        final CommunicationTemplate communicationTemplate = communicationTemplateRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(communicationTemplateDTO, communicationTemplate);
        communicationTemplateRepository.save(communicationTemplate);
    }

    public void delete(final UUID id) {
        communicationTemplateRepository.deleteById(id);
    }

    private CommunicationTemplateDTO mapToDTO(final CommunicationTemplate communicationTemplate,
            final CommunicationTemplateDTO communicationTemplateDTO) {
        communicationTemplateDTO.setId(communicationTemplate.getId());
        communicationTemplateDTO.setMessage(communicationTemplate.getMessage());
        communicationTemplateDTO.setStatus(communicationTemplate.getStatus());
        communicationTemplateDTO.setSubject(communicationTemplate.getSubject());
        communicationTemplateDTO.setCanDelete(communicationTemplate.getCanDelete());
        communicationTemplateDTO.setIsArchived(communicationTemplate.getIsArchived());
        communicationTemplateDTO.setBroadcastType(communicationTemplate.getBroadcastType());
        return communicationTemplateDTO;
    }

    private CommunicationTemplate mapToEntity(
            final CommunicationTemplateDTO communicationTemplateDTO,
            final CommunicationTemplate communicationTemplate) {
        communicationTemplate.setMessage(communicationTemplateDTO.getMessage());
        communicationTemplate.setStatus(communicationTemplateDTO.getStatus());
        communicationTemplate.setSubject(communicationTemplateDTO.getSubject());
        communicationTemplate.setCanDelete(communicationTemplateDTO.getCanDelete());
        communicationTemplate.setIsArchived(communicationTemplateDTO.getIsArchived());
        communicationTemplate.setBroadcastType(communicationTemplateDTO.getBroadcastType());
        return communicationTemplate;
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final CommunicationTemplate communicationTemplate = communicationTemplateRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Campaign communicationTemplateCampaign = campaignRepository.findFirstByCommunicationTemplate(communicationTemplate);
        if (communicationTemplateCampaign != null) {
            referencedWarning.setKey("communicationTemplate.campaign.communicationTemplate.referenced");
            referencedWarning.addParam(communicationTemplateCampaign.getId());
            return referencedWarning;
        }
        return null;
    }

}
