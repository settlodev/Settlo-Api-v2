package co.tz.settlo.api.subscription;

import co.tz.settlo.api.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(final SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<SubscriptionDTO> findAll() {
        final List<Subscription> subscriptions = subscriptionRepository.findAll(Sort.by("id"));
        return subscriptions.stream()
                .map(subscription -> mapToDTO(subscription, new SubscriptionDTO()))
                .toList();
    }

    public SubscriptionDTO get(final UUID id) {
        return subscriptionRepository.findById(id)
                .map(subscription -> mapToDTO(subscription, new SubscriptionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final SubscriptionDTO subscriptionDTO) {
        final Subscription subscription = new Subscription();
        mapToEntity(subscriptionDTO, subscription);
        return subscriptionRepository.save(subscription).getId();
    }

    public void update(final UUID id, final SubscriptionDTO subscriptionDTO) {
        final Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(subscriptionDTO, subscription);
        subscriptionRepository.save(subscription);
    }

    public void delete(final UUID id) {
        subscriptionRepository.deleteById(id);
    }

    private SubscriptionDTO mapToDTO(final Subscription subscription,
            final SubscriptionDTO subscriptionDTO) {
        subscriptionDTO.setId(subscription.getId());
        subscriptionDTO.setAmount(subscription.getAmount());
        subscriptionDTO.setDiscount(subscription.getDiscount());
        subscriptionDTO.setPackageName(subscription.getPackageName());
        subscriptionDTO.setPackageCode(subscription.getPackageCode());
        subscriptionDTO.setIsTrial(subscription.getIsTrial());
        subscriptionDTO.setStatus(subscription.getStatus());
        subscriptionDTO.setCanDelete(subscription.getCanDelete());
        subscriptionDTO.setIsArchived(subscription.getIsArchived());
        return subscriptionDTO;
    }

    private Subscription mapToEntity(final SubscriptionDTO subscriptionDTO,
            final Subscription subscription) {
        subscription.setAmount(subscriptionDTO.getAmount());
        subscription.setDiscount(subscriptionDTO.getDiscount());
        subscription.setPackageName(subscriptionDTO.getPackageName());
        subscription.setPackageCode(subscriptionDTO.getPackageCode());
        subscription.setIsTrial(subscriptionDTO.getIsTrial());
        subscription.setStatus(subscriptionDTO.getStatus());
        subscription.setCanDelete(subscriptionDTO.getCanDelete());
        subscription.setIsArchived(subscriptionDTO.getIsArchived());
        return subscription;
    }

    public boolean packageCodeExists(final String packageCode) {
        return subscriptionRepository.existsByPackageCodeIgnoreCase(packageCode);
    }

}
