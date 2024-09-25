package co.tz.settlo.api.location;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.expense.Expense;
import co.tz.settlo.api.location_setting.LocationSetting;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface LocationRepository extends JpaRepository<Location, UUID>, JpaSpecificationExecutor<Location> {
    List<Location> findAllByBusinessId(UUID businessId);

    Location findFirstBySetting(LocationSetting locationSetting);

    Location findFirstByBusiness(Business business);

    boolean existsBySettingId(UUID id);

}
