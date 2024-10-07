package co.tz.settlo.api.controllers.addon;

import co.tz.settlo.api.controllers.order_item.OrderItem;
import co.tz.settlo.api.controllers.order_item.OrderItemRepository;
import co.tz.settlo.api.util.NotFoundException;
import java.util.List;
import java.util.UUID;

import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import co.tz.settlo.api.util.RestApiFilter.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AddonService {

    private final AddonRepository addonRepository;
    private final OrderItemRepository orderItemRepository;

    public AddonService(final AddonRepository addonRepository,
            final OrderItemRepository orderItemRepository) {
        this.addonRepository = addonRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional(readOnly = true)
    public List<AddonDTO> findAll(final UUID locationId) {
        final List<Addon> addons = addonRepository.findAllByLocationId(locationId);
        return addons.stream()
                .map(addon -> mapToDTO(addon, new AddonDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<AddonDTO> searchAll(SearchRequest request) {
        SearchSpecification<Addon> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<Addon> addonsPage = addonRepository.findAll(specification, pageable);

        return addonsPage.map(addon -> mapToDTO(addon, new AddonDTO()));
    }

    @Transactional(readOnly = true)
    public AddonDTO get(final UUID id) {
        return addonRepository.findById(id)
                .map(addon -> mapToDTO(addon, new AddonDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public UUID create(final AddonDTO addonDTO) {
        final Addon addon = new Addon();
        mapToEntity(addonDTO, addon);
        return addonRepository.save(addon).getId();
    }

    @Transactional
    public void update(final UUID id, final AddonDTO addonDTO) {
        final Addon addon = addonRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(addonDTO, addon);
        addonRepository.save(addon);
    }

    @Transactional
    public void delete(final UUID id) {
        addonRepository.deleteById(id);
    }

    private AddonDTO mapToDTO(final Addon addon, final AddonDTO addonDTO) {
        addonDTO.setId(addon.getId());
        addonDTO.setTitle(addon.getTitle());
        addonDTO.setStatus(addon.getStatus());
        addonDTO.setCanDelete(addon.getCanDelete());
        addonDTO.setIsArchived(addon.getIsArchived());
        addonDTO.setPrice(addon.getPrice());
        addonDTO.setOrderItem(addon.getOrderItem() == null ? null : addon.getOrderItem().getId());
        return addonDTO;
    }

    private Addon mapToEntity(final AddonDTO addonDTO, final Addon addon) {
        addon.setTitle(addonDTO.getTitle());
        addon.setStatus(addonDTO.getStatus());
        addon.setCanDelete(addonDTO.getCanDelete());
        addon.setIsArchived(addonDTO.getIsArchived());
        addon.setPrice(addonDTO.getPrice());
        final OrderItem orderItem = addonDTO.getOrderItem() == null ? null : orderItemRepository.findById(addonDTO.getOrderItem())
                .orElseThrow(() -> new NotFoundException("orderItem not found"));
        addon.setOrderItem(orderItem);
        return addon;
    }

    public boolean titleExists(final String title) {
        return addonRepository.existsByTitleIgnoreCase(title);
    }

}
