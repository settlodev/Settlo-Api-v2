package co.tz.settlo.api.category;

import co.tz.settlo.api.discount.Discount;
import co.tz.settlo.api.discount.DiscountRepository;
import co.tz.settlo.api.location.Location;
import co.tz.settlo.api.location.LocationRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;

import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import co.tz.settlo.api.util.RestApiFilter.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final DiscountRepository discountRepository;

    public CategoryService(final CategoryRepository categoryRepository,
            final DiscountRepository discountRepository, final LocationRepository locationRepository) {
        this.categoryRepository = categoryRepository;
        this.discountRepository = discountRepository;
        this.locationRepository = locationRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(final UUID locationId) {
        final List<Category> categories = categoryRepository.findAllByLocationId(locationId);
        return categories.stream()
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<CategoryDTO> searchAll(SearchRequest request) {
        SearchSpecification<Category> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<Category> categoryPage = categoryRepository.findAll(specification, pageable);

        return categoryPage.map(category -> mapToDTO(category, new CategoryDTO()));
    }

    @Transactional(readOnly = true)
    public CategoryDTO get(final UUID id) {
        return categoryRepository.findById(id)
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public UUID create(final CategoryDTO categoryDTO) {
        final Category category = new Category();
        mapToEntity(categoryDTO, category);
        return categoryRepository.save(category).getId();
    }

    @Transactional
    public void update(final UUID id, final CategoryDTO categoryDTO) {
        final Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(categoryDTO, category);
        categoryRepository.save(category);
    }

    @Transactional
    public void delete(final UUID id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDTO mapToDTO(final Category category, final CategoryDTO categoryDTO) {
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setImage(category.getImage());
        categoryDTO.setParentId(category.getParentId());
        categoryDTO.setStatus(category.getStatus());
        categoryDTO.setIsArchived(category.getIsArchived());
        categoryDTO.setCanDelete(category.getCanDelete());
        categoryDTO.setLocation(category.getLocation() == null ? null : category.getLocation().getId());

        return categoryDTO;
    }

    private Category mapToEntity(final CategoryDTO categoryDTO, final Category category) {
        category.setName(categoryDTO.getName());
        category.setImage(categoryDTO.getImage());
        category.setParentId(categoryDTO.getParentId());
        category.setStatus(categoryDTO.getStatus());
        category.setIsArchived(categoryDTO.getIsArchived());
        category.setCanDelete(categoryDTO.getCanDelete());
        final Location location = category.getLocation() == null ? null : locationRepository.findById(categoryDTO.getLocation())
                .orElseThrow(() -> new NotFoundException("Location not found"));
        category.setLocation(location);

        return category;
    }

    public boolean nameExists(final String name) {
        return categoryRepository.existsByNameIgnoreCase(name);
    }

    public boolean parentIdExists(final UUID parentId) {
        return categoryRepository.existsByParentId(parentId);
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Discount categoryDiscount = discountRepository.findFirstByCategory(category);
        if (categoryDiscount != null) {
            referencedWarning.setKey("category.discount.category.referenced");
            referencedWarning.addParam(categoryDiscount.getId());
            return referencedWarning;
        }
        return null;
    }

}
