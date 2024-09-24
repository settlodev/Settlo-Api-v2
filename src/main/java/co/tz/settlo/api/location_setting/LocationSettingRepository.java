package co.tz.settlo.api.location_setting;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationSettingRepository extends JpaRepository<LocationSetting, UUID> {
}
