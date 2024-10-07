package co.tz.settlo.api.controllers.location_subscription;

import co.tz.settlo.api.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class LocationSubscriptionService {

    private final LocationSubscriptionRepository subscriptionRepository;

    public LocationSubscriptionService(final LocationSubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<LocationSubscriptionDTO> findAll() {
        final List<LocationSubscription> subscriptions = subscriptionRepository.findAll(Sort.by("id"));
        return subscriptions.stream()
                .map(subscription -> mapToDTO(subscription, new LocationSubscriptionDTO()))
                .toList();
    }

    public LocationSubscriptionDTO get(final UUID id) {
        return subscriptionRepository.findById(id)
                .map(subscription -> mapToDTO(subscription, new LocationSubscriptionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final LocationSubscriptionDTO subscriptionDTO) {
        final LocationSubscription subscription = new LocationSubscription();
        mapToEntity(subscriptionDTO, subscription);
        return subscriptionRepository.save(subscription).getId();
    }

    public void update(final UUID id, final LocationSubscriptionDTO subscriptionDTO) {
        final LocationSubscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(subscriptionDTO, subscription);
        subscriptionRepository.save(subscription);
    }

    public void delete(final UUID id) {
        subscriptionRepository.deleteById(id);
    }

    private LocationSubscriptionDTO mapToDTO(final LocationSubscription locationSubscription,
            final LocationSubscriptionDTO subscriptionDTO) {
        subscriptionDTO.setId(locationSubscription.getId());
        subscriptionDTO.setStartDate(locationSubscription.getStartDate());
        subscriptionDTO.setEndDate(locationSubscription.getEndDate());
        subscriptionDTO.setStatus(locationSubscription.getStatus());
        subscriptionDTO.setCanDelete(locationSubscription.getCanDelete());
        subscriptionDTO.setIsArchived(locationSubscription.getIsArchived());
        return subscriptionDTO;
    }

    private LocationSubscription mapToEntity(final LocationSubscriptionDTO locationSubscriptionDTO,
            final LocationSubscription subscription) {
        subscription.setStartDate(locationSubscriptionDTO.getStartDate());
        subscription.setEndDate(locationSubscriptionDTO.getEndDate());
        subscription.setStatus(locationSubscriptionDTO.getStatus());
        subscription.setCanDelete(locationSubscriptionDTO.getCanDelete());
        subscription.setIsArchived(locationSubscriptionDTO.getIsArchived());
        return subscription;
    }

//    public boolean packageCodeExists(final String packageCode) {
//        return subscriptionRepository.existsByPackageCodeIgnoreCase(packageCode);
//    }

}
