package co.tz.settlo.api.category;

import co.tz.settlo.api.discount.Discount;
import co.tz.settlo.api.discount.DiscountRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final DiscountRepository discountRepository;

    public CategoryService(final CategoryRepository categoryRepository,
            final DiscountRepository discountRepository) {
        this.categoryRepository = categoryRepository;
        this.discountRepository = discountRepository;
    }

    public List<CategoryDTO> findAll() {
        final List<Category> categories = categoryRepository.findAll(Sort.by("id"));
        return categories.stream()
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .toList();
    }

    public CategoryDTO get(final UUID id) {
        return categoryRepository.findById(id)
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final CategoryDTO categoryDTO) {
        final Category category = new Category();
        mapToEntity(categoryDTO, category);
        return categoryRepository.save(category).getId();
    }

    public void update(final UUID id, final CategoryDTO categoryDTO) {
        final Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(categoryDTO, category);
        categoryRepository.save(category);
    }

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
        return categoryDTO;
    }

    private Category mapToEntity(final CategoryDTO categoryDTO, final Category category) {
        category.setName(categoryDTO.getName());
        category.setImage(categoryDTO.getImage());
        category.setParentId(categoryDTO.getParentId());
        category.setStatus(categoryDTO.getStatus());
        category.setIsArchived(categoryDTO.getIsArchived());
        category.setCanDelete(categoryDTO.getCanDelete());
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
