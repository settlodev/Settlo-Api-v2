package co.tz.settlo.api.controllers.communication_template;

import co.tz.settlo.api.controllers.campaign.Campaign;
import co.tz.settlo.api.controllers.campaign.CampaignRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;

import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import co.tz.settlo.api.util.RestApiFilter.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    @Transactional(readOnly = true)
    public List<CommunicationTemplateDTO> findAll(final UUID locationId) {
        final List<CommunicationTemplate> communicationTemplates = communicationTemplateRepository.findAllByLocationId(locationId);
        return communicationTemplates.stream()
                .map(communicationTemplate -> mapToDTO(communicationTemplate, new CommunicationTemplateDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public CommunicationTemplateDTO get(final UUID id) {
        return communicationTemplateRepository.findById(id)
                .map(communicationTemplate -> mapToDTO(communicationTemplate, new CommunicationTemplateDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<CommunicationTemplateDTO> searchAll(SearchRequest request) {
        SearchSpecification<CommunicationTemplate> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<CommunicationTemplate> communicationTemplatesPage = communicationTemplateRepository.findAll(specification, pageable);

        return communicationTemplatesPage.map(communicationTemplate -> mapToDTO(communicationTemplate, new CommunicationTemplateDTO()));
    }

    @Transactional
    public UUID create(final CommunicationTemplateDTO communicationTemplateDTO) {
        final CommunicationTemplate communicationTemplate = new CommunicationTemplate();
        mapToEntity(communicationTemplateDTO, communicationTemplate);
        return communicationTemplateRepository.save(communicationTemplate).getId();
    }

    @Transactional
    public void update(final UUID id, final CommunicationTemplateDTO communicationTemplateDTO) {
        final CommunicationTemplate communicationTemplate = communicationTemplateRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(communicationTemplateDTO, communicationTemplate);
        communicationTemplateRepository.save(communicationTemplate);
    }

    @Transactional
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
