package co.tz.settlo.api.controllers.communication_log;

import co.tz.settlo.api.controllers.campaign.Campaign;
import co.tz.settlo.api.controllers.campaign.CampaignRepository;
import co.tz.settlo.api.util.NotFoundException;
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
public class CommunicationLogService {

    private final CommunicationLogRepository communicationLogRepository;
    private final CampaignRepository campaignRepository;

    public CommunicationLogService(final CommunicationLogRepository communicationLogRepository,
            final CampaignRepository campaignRepository) {
        this.communicationLogRepository = communicationLogRepository;
        this.campaignRepository = campaignRepository;
    }

    @Transactional(readOnly = true)
    public List<CommunicationLogDTO> findAll(final UUID locationId) {
        final List<CommunicationLog> communicationLogs = communicationLogRepository.findAll(Sort.by("id"));
        return communicationLogs.stream()
                .map(communicationLog -> mapToDTO(communicationLog, new CommunicationLogDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public CommunicationLogDTO get(final UUID id) {
        return communicationLogRepository.findById(id)
                .map(communicationLog -> mapToDTO(communicationLog, new CommunicationLogDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<CommunicationLogDTO> searchAll(SearchRequest request) {
        SearchSpecification<CommunicationLog> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<CommunicationLog> communicationLogPage = communicationLogRepository.findAll(specification, pageable);

        return communicationLogPage.map(communicationLog -> mapToDTO(communicationLog, new CommunicationLogDTO()));
    }

    @Transactional
    public UUID create(final CommunicationLogDTO communicationLogDTO) {
        final CommunicationLog communicationLog = new CommunicationLog();
        mapToEntity(communicationLogDTO, communicationLog);
        return communicationLogRepository.save(communicationLog).getId();
    }

    @Transactional
    public void update(final UUID id, final CommunicationLogDTO communicationLogDTO) {
        final CommunicationLog communicationLog = communicationLogRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(communicationLogDTO, communicationLog);
        communicationLogRepository.save(communicationLog);
    }

    @Transactional
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
                .orElseThrow(() -> new NotFoundException("Campaign not found"));
        communicationLog.setCampaign(campaign);
        return communicationLog;
    }

}
