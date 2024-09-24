package co.tz.settlo.api.sender_id;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.business.BusinessRepository;
import co.tz.settlo.api.campaign.Campaign;
import co.tz.settlo.api.campaign.CampaignRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SenderIdService {

    private final SenderIdRepository senderIdRepository;
    private final BusinessRepository businessRepository;
    private final CampaignRepository campaignRepository;

    public SenderIdService(final SenderIdRepository senderIdRepository,
            final BusinessRepository businessRepository,
            final CampaignRepository campaignRepository) {
        this.senderIdRepository = senderIdRepository;
        this.businessRepository = businessRepository;
        this.campaignRepository = campaignRepository;
    }

    public List<SenderIdDTO> findAll() {
        final List<SenderId> senderIds = senderIdRepository.findAll(Sort.by("id"));
        return senderIds.stream()
                .map(senderId -> mapToDTO(senderId, new SenderIdDTO()))
                .toList();
    }

    public SenderIdDTO get(final UUID id) {
        return senderIdRepository.findById(id)
                .map(senderId -> mapToDTO(senderId, new SenderIdDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final SenderIdDTO senderIdDTO) {
        final SenderId senderId = new SenderId();
        mapToEntity(senderIdDTO, senderId);
        return senderIdRepository.save(senderId).getId();
    }

    public void update(final UUID id, final SenderIdDTO senderIdDTO) {
        final SenderId senderId = senderIdRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(senderIdDTO, senderId);
        senderIdRepository.save(senderId);
    }

    public void delete(final UUID id) {
        senderIdRepository.deleteById(id);
    }

    private SenderIdDTO mapToDTO(final SenderId senderId, final SenderIdDTO senderIdDTO) {
        senderIdDTO.setId(senderId.getId());
        senderIdDTO.setName(senderId.getName());
        senderIdDTO.setStatus(senderId.getStatus());
        senderIdDTO.setIsArchived(senderId.getIsArchived());
        senderIdDTO.setCanDelete(senderId.getCanDelete());
        senderIdDTO.setBusiness(senderId.getBusiness() == null ? null : senderId.getBusiness().getId());
        return senderIdDTO;
    }

    private SenderId mapToEntity(final SenderIdDTO senderIdDTO, final SenderId senderId) {
        senderId.setName(senderIdDTO.getName());
        senderId.setStatus(senderIdDTO.getStatus());
        senderId.setIsArchived(senderIdDTO.getIsArchived());
        senderId.setCanDelete(senderIdDTO.getCanDelete());
        final Business business = senderIdDTO.getBusiness() == null ? null : businessRepository.findById(senderIdDTO.getBusiness())
                .orElseThrow(() -> new NotFoundException("business not found"));
        senderId.setBusiness(business);
        return senderId;
    }

    public boolean nameExists(final String name) {
        return senderIdRepository.existsByNameIgnoreCase(name);
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final SenderId senderId = senderIdRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Campaign senderIdCampaign = campaignRepository.findFirstBySenderId(senderId);
        if (senderIdCampaign != null) {
            referencedWarning.setKey("senderId.campaign.senderId.referenced");
            referencedWarning.addParam(senderIdCampaign.getId());
            return referencedWarning;
        }
        return null;
    }

}
