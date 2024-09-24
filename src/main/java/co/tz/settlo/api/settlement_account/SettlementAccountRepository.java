package co.tz.settlo.api.settlement_account;

import co.tz.settlo.api.business.Business;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SettlementAccountRepository extends JpaRepository<SettlementAccount, UUID> {

    SettlementAccount findFirstByBusiness(Business business);

}
