package co.tz.settlo.api.settlement;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.settlement_account.SettlementAccount;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SettlementRepository extends JpaRepository<Settlement, UUID> {

    Settlement findFirstByAccount(SettlementAccount settlementAccount);

    Settlement findFirstByBusiness(Business business);

    boolean existsByTransactionReferenceIgnoreCase(String transactionReference);

}
