package co.tz.settlo.api.controllers.settlement_account;

import co.tz.settlo.api.controllers.business.Business;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SettlementAccountRepository extends JpaRepository<SettlementAccount, UUID> {

    SettlementAccount findFirstByBusiness(Business business);

}
