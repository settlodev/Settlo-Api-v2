package co.tz.settlo.api.controllers.settlement;

import co.tz.settlo.api.controllers.business.Business;
import co.tz.settlo.api.controllers.business.BusinessRepository;
import co.tz.settlo.api.controllers.settlement_account.SettlementAccount;
import co.tz.settlo.api.controllers.settlement_account.SettlementAccountRepository;
import co.tz.settlo.api.util.NotFoundException;
import java.util.List;
import java.util.UUID;

import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import co.tz.settlo.api.util.RestApiFilter.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final SettlementAccountRepository settlementAccountRepository;
    private final BusinessRepository businessRepository;

    public SettlementService(final SettlementRepository settlementRepository,
            final SettlementAccountRepository settlementAccountRepository,
            final BusinessRepository businessRepository) {
        this.settlementRepository = settlementRepository;
        this.settlementAccountRepository = settlementAccountRepository;
        this.businessRepository = businessRepository;
    }

    @Transactional(readOnly = true)
    public List<SettlementDTO> findAll(final UUID locationId) {
        final List<Settlement> settlements = settlementRepository.findAllByLocationId(locationId);
        return settlements.stream()
                .map(settlement -> mapToDTO(settlement, new SettlementDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public SettlementDTO get(final UUID id) {
        return settlementRepository.findById(id)
                .map(settlement -> mapToDTO(settlement, new SettlementDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<SettlementDTO> searchAll(SearchRequest request) {
        SearchSpecification<Settlement> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<Settlement> settlementsPage = settlementRepository.findAll(specification, pageable);

        return settlementsPage.map(settlement -> mapToDTO(settlement, new SettlementDTO()));
    }

    @Transactional
    public UUID create(final SettlementDTO settlementDTO) {
        final Settlement settlement = new Settlement();
        mapToEntity(settlementDTO, settlement);
        return settlementRepository.save(settlement).getId();
    }

    @Transactional
    public void update(final UUID id, final SettlementDTO settlementDTO) {
        final Settlement settlement = settlementRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(settlementDTO, settlement);
        settlementRepository.save(settlement);
    }

    @Transactional
    public void delete(final UUID id) {
        settlementRepository.deleteById(id);
    }

    private SettlementDTO mapToDTO(final Settlement settlement, final SettlementDTO settlementDTO) {
        settlementDTO.setId(settlement.getId());
        settlementDTO.setTransactionReference(settlement.getTransactionReference());
        settlementDTO.setExternalReference(settlement.getExternalReference());
        settlementDTO.setAmount(settlement.getAmount());
        settlementDTO.setNotes(settlement.getNotes());
        settlementDTO.setCompletionTime(settlement.getCompletionTime());
        settlementDTO.setStatus(settlement.getStatus());
        settlementDTO.setCanDelete(settlement.getCanDelete());
        settlementDTO.setIsArchived(settlement.getIsArchived());
        settlementDTO.setAccount(settlement.getAccount() == null ? null : settlement.getAccount().getId());
        settlementDTO.setBusiness(settlement.getBusiness() == null ? null : settlement.getBusiness().getId());
        return settlementDTO;
    }

    private Settlement mapToEntity(final SettlementDTO settlementDTO, final Settlement settlement) {
        settlement.setTransactionReference(settlementDTO.getTransactionReference());
        settlement.setExternalReference(settlementDTO.getExternalReference());
        settlement.setAmount(settlementDTO.getAmount());
        settlement.setNotes(settlementDTO.getNotes());
        settlement.setCompletionTime(settlementDTO.getCompletionTime());
        settlement.setStatus(settlementDTO.getStatus());
        settlement.setCanDelete(settlementDTO.getCanDelete());
        settlement.setIsArchived(settlementDTO.getIsArchived());
        final SettlementAccount account = settlementDTO.getAccount() == null ? null : settlementAccountRepository.findById(settlementDTO.getAccount())
                .orElseThrow(() -> new NotFoundException("account not found"));
        settlement.setAccount(account);
        final Business business = settlementDTO.getBusiness() == null ? null : businessRepository.findById(settlementDTO.getBusiness())
                .orElseThrow(() -> new NotFoundException("business not found"));
        settlement.setBusiness(business);
        return settlement;
    }

    public boolean transactionReferenceExists(final String transactionReference) {
        return settlementRepository.existsByTransactionReferenceIgnoreCase(transactionReference);
    }

}
