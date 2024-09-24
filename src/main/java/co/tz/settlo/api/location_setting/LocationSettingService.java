package co.tz.settlo.api.location_setting;

import co.tz.settlo.api.location.Location;
import co.tz.settlo.api.location.LocationRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class LocationSettingService {

    private final LocationSettingRepository locationSettingRepository;
    private final LocationRepository locationRepository;

    public LocationSettingService(final LocationSettingRepository locationSettingRepository,
            final LocationRepository locationRepository) {
        this.locationSettingRepository = locationSettingRepository;
        this.locationRepository = locationRepository;
    }

    public List<LocationSettingDTO> findAll() {
        final List<LocationSetting> locationSettings = locationSettingRepository.findAll(Sort.by("id"));
        return locationSettings.stream()
                .map(locationSetting -> mapToDTO(locationSetting, new LocationSettingDTO()))
                .toList();
    }

    public LocationSettingDTO get(final UUID id) {
        return locationSettingRepository.findById(id)
                .map(locationSetting -> mapToDTO(locationSetting, new LocationSettingDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final LocationSettingDTO locationSettingDTO) {
        final LocationSetting locationSetting = new LocationSetting();
        mapToEntity(locationSettingDTO, locationSetting);
        return locationSettingRepository.save(locationSetting).getId();
    }

    public void update(final UUID id, final LocationSettingDTO locationSettingDTO) {
        final LocationSetting locationSetting = locationSettingRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(locationSettingDTO, locationSetting);
        locationSettingRepository.save(locationSetting);
    }

    public void delete(final UUID id) {
        locationSettingRepository.deleteById(id);
    }

    private LocationSettingDTO mapToDTO(final LocationSetting locationSetting,
            final LocationSettingDTO locationSettingDTO) {
        locationSettingDTO.setId(locationSetting.getId());
        locationSettingDTO.setMinimumSettlementAmount(locationSetting.getMinimumSettlementAmount());
        locationSettingDTO.setSystemPasscode(locationSetting.getSystemPasscode());
        locationSettingDTO.setReportsPasscode(locationSetting.getReportsPasscode());
        locationSettingDTO.setIsDefault(locationSetting.getIsDefault());
        locationSettingDTO.setTrackInventory(locationSetting.getTrackInventory());
        locationSettingDTO.setEcommerceEnabled(locationSetting.getEcommerceEnabled());
        locationSettingDTO.setEnableNotifications(locationSetting.getEnableNotifications());
        locationSettingDTO.setUseRecipe(locationSetting.getUseRecipe());
        locationSettingDTO.setUseDepartments(locationSetting.getUseDepartments());
        locationSettingDTO.setUseCustomPrice(locationSetting.getUseCustomPrice());
        locationSettingDTO.setUseWarehouse(locationSetting.getUseWarehouse());
        locationSettingDTO.setUseShifts(locationSetting.getUseShifts());
        locationSettingDTO.setUseKds(locationSetting.getUseKds());
        locationSettingDTO.setIsActive(locationSetting.getIsActive());
        locationSettingDTO.setCanDelete(locationSetting.getCanDelete());
        locationSettingDTO.setIsArchived(locationSetting.getIsArchived());
        return locationSettingDTO;
    }

    private LocationSetting mapToEntity(final LocationSettingDTO locationSettingDTO,
            final LocationSetting locationSetting) {
        locationSetting.setMinimumSettlementAmount(locationSettingDTO.getMinimumSettlementAmount());
        locationSetting.setSystemPasscode(locationSettingDTO.getSystemPasscode());
        locationSetting.setReportsPasscode(locationSettingDTO.getReportsPasscode());
        locationSetting.setIsDefault(locationSettingDTO.getIsDefault());
        locationSetting.setTrackInventory(locationSettingDTO.getTrackInventory());
        locationSetting.setEcommerceEnabled(locationSettingDTO.getEcommerceEnabled());
        locationSetting.setEnableNotifications(locationSettingDTO.getEnableNotifications());
        locationSetting.setUseRecipe(locationSettingDTO.getUseRecipe());
        locationSetting.setUseDepartments(locationSettingDTO.getUseDepartments());
        locationSetting.setUseCustomPrice(locationSettingDTO.getUseCustomPrice());
        locationSetting.setUseWarehouse(locationSettingDTO.getUseWarehouse());
        locationSetting.setUseShifts(locationSettingDTO.getUseShifts());
        locationSetting.setUseKds(locationSettingDTO.getUseKds());
        locationSetting.setIsActive(locationSettingDTO.getIsActive());
        locationSetting.setCanDelete(locationSettingDTO.getCanDelete());
        locationSetting.setIsArchived(locationSettingDTO.getIsArchived());
        return locationSetting;
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final LocationSetting locationSetting = locationSettingRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Location settingLocation = locationRepository.findFirstBySetting(locationSetting);
        if (settingLocation != null) {
            referencedWarning.setKey("locationSetting.location.setting.referenced");
            referencedWarning.addParam(settingLocation.getId());
            return referencedWarning;
        }
        return null;
    }

}
