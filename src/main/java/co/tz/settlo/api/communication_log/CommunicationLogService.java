package co.tz.settlo.api.communication_log;

import co.tz.settlo.api.campaign.Campaign;
import co.tz.settlo.api.campaign.CampaignRepository;
import co.tz.settlo.api.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CommunicationLogService {

    private final CommunicationLogRepository communicationLogRepository;
    private final CampaignRepository campaignRepository;

    public CommunicationLogService(final CommunicationLogRepository communicationLogRepository,
            final CampaignRepository campaignRepository) {
        this.communicationLogRepository = communicationLogRepository;
        this.campaignRepository = campaignRepository;
    }

    public List<CommunicationLogDTO> findAll() {
        final List<CommunicationLog> communicationLogs = communicationLogRepository.findAll(Sort.by("id"));
        return communicationLogs.stream()
                .map(communicationLog -> mapToDTO(communicationLog, new CommunicationLogDTO()))
                .toList();
    }

    public CommunicationLogDTO get(final UUID id) {
        return communicationLogRepository.findById(id)
                .map(communicationLog -> mapToDTO(communicationLog, new CommunicationLogDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final CommunicationLogDTO communicationLogDTO) {
        final CommunicationLog communicationLog = new CommunicationLog();
        mapToEntity(communicationLogDTO, communicationLog);
        return communicationLogRepository.save(communicationLog).getId();
    }

    public void update(final UUID id, final CommunicationLogDTO communicationLogDTO) {
        final CommunicationLog communicationLog = communicationLogRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(communicationLogDTO, communicationLog);
        communicationLogRepository.save(communicationLog);
    }

    public void delete(final UUID id) {
        communicationLogRepository.deleteById(id);
    }

    private CommunicationLogDTO mapToDTO(final CommunicationLog communicationLog,
            final CommunicationLogDTO communicationLogDTO) {
        communicationLogDTO.setId(communicationLog.getId());
        communicationLogDTO.setTotalAudience(communicationLog.getTotalAudience());
        communicationLogDTO.setTotalSuccessful(communicationLog.getTotalSuccessful());
        communicationLogDTO.setTotalFailed(communicationLog.getTotalFailed());
        communicationLogDTO.setStatus(communicationLog.getStatus());
        communicationLogDTO.setIsArchived(communicationLog.getIsArchived());
        communicationLogDTO.setCanDelete(communicationLog.getCanDelete());
        communicationLogDTO.setCampaign(communicationLog.getCampaign() == null ? null : communicationLog.getCampaign().getId());
        return communicationLogDTO;
    }

    private CommunicationLog mapToEntity(final CommunicationLogDTO communicationLogDTO,
            final CommunicationLog communicationLog) {
        communicationLog.setTotalAudience(communicationLogDTO.getTotalAudience());
        communicationLog.setTotalSuccessful(communicationLogDTO.getTotalSuccessful());
        communicationLog.setTotalFailed(communicationLogDTO.getTotalFailed());
        communicationLog.setStatus(communicationLogDTO.getStatus());
        communicationLog.setIsArchived(communicationLogDTO.getIsArchived());
        communicationLog.setCanDelete(communicationLogDTO.getCanDelete());
        final Campaign campaign = communicationLogDTO.getCampaign() == null ? null : campaignRepository.findById(communicationLogDTO.getCampaign())
                .orElseThrow(() -> new NotFoundException("campaign not found"));
        communicationLog.setCampaign(campaign);
        return communicationLog;
    }

}
