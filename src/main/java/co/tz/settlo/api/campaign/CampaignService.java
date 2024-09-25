package co.tz.settlo.api.campaign;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.business.BusinessRepository;
import co.tz.settlo.api.communication_log.CommunicationLog;
import co.tz.settlo.api.communication_log.CommunicationLogRepository;
import co.tz.settlo.api.communication_template.CommunicationTemplate;
import co.tz.settlo.api.communication_template.CommunicationTemplateRepository;
import co.tz.settlo.api.expense.Expense;
import co.tz.settlo.api.expense.ExpenseDTO;
import co.tz.settlo.api.location.Location;
import co.tz.settlo.api.location.LocationRepository;
import co.tz.settlo.api.sender_id.SenderId;
import co.tz.settlo.api.sender_id.SenderIdRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;

import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import co.tz.settlo.api.util.RestApiFilter.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final SenderIdRepository senderIdRepository;
    private final CommunicationTemplateRepository communicationTemplateRepository;
    private final BusinessRepository businessRepository;
    private final LocationRepository locationRepository;
    private final CommunicationLogRepository communicationLogRepository;

    public CampaignService(final CampaignRepository campaignRepository,
            final SenderIdRepository senderIdRepository,
            final CommunicationTemplateRepository communicationTemplateRepository,
            final BusinessRepository businessRepository,
            final LocationRepository locationRepository,
            final CommunicationLogRepository communicationLogRepository) {
        this.campaignRepository = campaignRepository;
        this.senderIdRepository = senderIdRepository;
        this.communicationTemplateRepository = communicationTemplateRepository;
        this.businessRepository = businessRepository;
        this.locationRepository = locationRepository;
        this.communicationLogRepository = communicationLogRepository;
    }

    @Transactional(readOnly = true)
    public Page<CampaignDTO> searchAll(SearchRequest request) {
        SearchSpecification<Campaign> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<Campaign> campaignsPage = campaignRepository.findAll(specification, pageable);

        return campaignsPage.map(expense -> mapToDTO(expense, new CampaignDTO()));
    }

    @Transactional(readOnly = true)
    public List<CampaignDTO> findAll(final UUID locationId) {
        final List<Campaign> campaigns = campaignRepository.findAllByLocationId(locationId);
        return campaigns.stream()
                .map(campaign -> mapToDTO(campaign, new CampaignDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public CampaignDTO get(final UUID id) {
        return campaignRepository.findById(id)
                .map(campaign -> mapToDTO(campaign, new CampaignDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public UUID create(final CampaignDTO campaignDTO) {
        final Campaign campaign = new Campaign();
        mapToEntity(campaignDTO, campaign);
        return campaignRepository.save(campaign).getId();
    }

    @Transactional
    public void update(final UUID id, final CampaignDTO campaignDTO) {
        final Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(campaignDTO, campaign);
        campaignRepository.save(campaign);
    }

    @Transactional
    public void delete(final UUID id) {
        campaignRepository.deleteById(id);
    }

    private CampaignDTO mapToDTO(final Campaign campaign, final CampaignDTO campaignDTO) {
        campaignDTO.setId(campaign.getId());
        campaignDTO.setName(campaign.getName());
        campaignDTO.setMessage(campaign.getMessage());
        campaignDTO.setStatus(campaign.getStatus());
        campaignDTO.setIsArchived(campaign.getIsArchived());
        campaignDTO.setCanDelete(campaign.getCanDelete());
        campaignDTO.setAudience(campaign.getAudience());
        campaignDTO.setCustomMessage(campaign.getCustomMessage());
        campaignDTO.setBroadcastType(campaign.getBroadcastType());
        campaignDTO.setSenderId(campaign.getSenderId() == null ? null : campaign.getSenderId().getId());
        campaignDTO.setCommunicationTemplate(campaign.getCommunicationTemplate() == null ? null : campaign.getCommunicationTemplate().getId());
        campaignDTO.setBusiness(campaign.getBusiness() == null ? null : campaign.getBusiness().getId());
        campaignDTO.setLocation(campaign.getLocation() == null ? null : campaign.getLocation().getId());
        return campaignDTO;
    }

    private Campaign mapToEntity(final CampaignDTO campaignDTO, final Campaign campaign) {
        campaign.setName(campaignDTO.getName());
        campaign.setMessage(campaignDTO.getMessage());
        campaign.setStatus(campaignDTO.getStatus());
        campaign.setIsArchived(campaignDTO.getIsArchived());
        campaign.setCanDelete(campaignDTO.getCanDelete());
        campaign.setAudience(campaignDTO.getAudience());
        campaign.setCustomMessage(campaignDTO.getCustomMessage());
        campaign.setBroadcastType(campaignDTO.getBroadcastType());
        final SenderId senderId = campaignDTO.getSenderId() == null ? null : senderIdRepository.findById(campaignDTO.getSenderId())
                .orElseThrow(() -> new NotFoundException("senderId not found"));
        campaign.setSenderId(senderId);
        final CommunicationTemplate communicationTemplate = campaignDTO.getCommunicationTemplate() == null ? null : communicationTemplateRepository.findById(campaignDTO.getCommunicationTemplate())
                .orElseThrow(() -> new NotFoundException("communicationTemplate not found"));
        campaign.setCommunicationTemplate(communicationTemplate);
        final Business business = campaignDTO.getBusiness() == null ? null : businessRepository.findById(campaignDTO.getBusiness())
                .orElseThrow(() -> new NotFoundException("business not found"));
        campaign.setBusiness(business);
        final Location location = campaignDTO.getLocation() == null ? null : locationRepository.findById(campaignDTO.getLocation())
                .orElseThrow(() -> new NotFoundException("location not found"));
        campaign.setLocation(location);
        return campaign;
    }

    public boolean nameExists(final String name) {
        return campaignRepository.existsByNameIgnoreCase(name);
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final CommunicationLog campaignCommunicationLog = communicationLogRepository.findFirstByCampaign(campaign);
        if (campaignCommunicationLog != null) {
            referencedWarning.setKey("campaign.communicationLog.campaign.referenced");
            referencedWarning.addParam(campaignCommunicationLog.getId());
            return referencedWarning;
        }
        return null;
    }

}
