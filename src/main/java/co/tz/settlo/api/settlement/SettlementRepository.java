package co.tz.settlo.api.settlement;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.settlement_account.SettlementAccount;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface SettlementRepository extends JpaRepository<Settlement, UUID>, JpaSpecificationExecutor<Settlement> {

    List<Settlement> findAllByLocationId(UUID locationId);

    Settlement findFirstByAccount(SettlementAccount settlementAccount);

    Settlement findFirstByBusiness(Business business);

    boolean existsByTransactionReferenceIgnoreCase(String transactionReference);

}
