package co.tz.settlo.api.controllers.settlement_account;

import co.tz.settlo.api.controllers.business.Business;
import co.tz.settlo.api.controllers.business.BusinessRepository;
import co.tz.settlo.api.controllers.settlement.Settlement;
import co.tz.settlo.api.controllers.settlement.SettlementRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SettlementAccountService {

    private final SettlementAccountRepository settlementAccountRepository;
    private final BusinessRepository businessRepository;
    private final SettlementRepository settlementRepository;

    public SettlementAccountService(final SettlementAccountRepository settlementAccountRepository,
            final BusinessRepository businessRepository,
            final SettlementRepository settlementRepository) {
        this.settlementAccountRepository = settlementAccountRepository;
        this.businessRepository = businessRepository;
        this.settlementRepository = settlementRepository;
    }

    public List<SettlementAccountDTO> findAll() {
        final List<SettlementAccount> settlementAccounts = settlementAccountRepository.findAll(Sort.by("id"));
        return settlementAccounts.stream()
                .map(settlementAccount -> mapToDTO(settlementAccount, new SettlementAccountDTO()))
                .toList();
    }

    public SettlementAccountDTO get(final UUID id) {
        return settlementAccountRepository.findById(id)
                .map(settlementAccount -> mapToDTO(settlementAccount, new SettlementAccountDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final SettlementAccountDTO settlementAccountDTO) {
        final SettlementAccount settlementAccount = new SettlementAccount();
        mapToEntity(settlementAccountDTO, settlementAccount);
        return settlementAccountRepository.save(settlementAccount).getId();
    }

    public void update(final UUID id, final SettlementAccountDTO settlementAccountDTO) {
        final SettlementAccount settlementAccount = settlementAccountRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(settlementAccountDTO, settlementAccount);
        settlementAccountRepository.save(settlementAccount);
    }

    public void delete(final UUID id) {
        settlementAccountRepository.deleteById(id);
    }

    private SettlementAccountDTO mapToDTO(final SettlementAccount settlementAccount,
            final SettlementAccountDTO settlementAccountDTO) {
        settlementAccountDTO.setId(settlementAccount.getId());
        settlementAccountDTO.setType(settlementAccount.getType());
        settlementAccountDTO.setAccountNumber(settlementAccount.getAccountNumber());
        settlementAccountDTO.setStatus(settlementAccount.getStatus());
        settlementAccountDTO.setCanDelete(settlementAccount.getCanDelete());
        settlementAccountDTO.setIsArchived(settlementAccount.getIsArchived());
        settlementAccountDTO.setBusiness(settlementAccount.getBusiness() == null ? null : settlementAccount.getBusiness().getId());
        return settlementAccountDTO;
    }

    private SettlementAccount mapToEntity(final SettlementAccountDTO settlementAccountDTO,
            final SettlementAccount settlementAccount) {
        settlementAccount.setType(settlementAccountDTO.getType());
        settlementAccount.setAccountNumber(settlementAccountDTO.getAccountNumber());
        settlementAccount.setStatus(settlementAccountDTO.getStatus());
        settlementAccount.setCanDelete(settlementAccountDTO.getCanDelete());
        settlementAccount.setIsArchived(settlementAccountDTO.getIsArchived());
        final Business business = settlementAccountDTO.getBusiness() == null ? null : businessRepository.findById(settlementAccountDTO.getBusiness())
                .orElseThrow(() -> new NotFoundException("business not found"));
        settlementAccount.setBusiness(business);
        return settlementAccount;
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final SettlementAccount settlementAccount = settlementAccountRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Settlement accountSettlement = settlementRepository.findFirstByAccount(settlementAccount);
        if (accountSettlement != null) {
            referencedWarning.setKey("settlementAccount.settlement.account.referenced");
            referencedWarning.addParam(accountSettlement.getId());
            return referencedWarning;
        }
        return null;
    }

}
