package co.tz.settlo.api.location;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.location_setting.LocationSetting;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationRepository extends JpaRepository<Location, UUID> {

    Location findFirstBySetting(LocationSetting locationSetting);

    Location findFirstByBusiness(Business business);

    boolean existsBySettingId(UUID id);

}
