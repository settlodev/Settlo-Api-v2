package co.tz.settlo.api.tag;

import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import co.tz.settlo.api.util.RestApiFilter.SearchSpecification;
import co.tz.settlo.api.product_variants.Variant;
import co.tz.settlo.api.product_variants.VariantRepository;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TagService {

    private final TagRepository tagRepository;
    private final VariantRepository variantRepository;

    public TagService(final TagRepository tagRepository,
            final VariantRepository variantRepository) {
        this.tagRepository = tagRepository;
        this.variantRepository = variantRepository;
    }

    @Transactional(readOnly = true)
    public List<TagDTO> findAll() {
        final List<Tag> tags = tagRepository.findAll(Sort.by("id"));
        return tags.stream()
                .map(tag -> mapToDTO(tag, new TagDTO()))
                .toList();
    }
  
    @Transactional(readOnly = true)
    public Page<TagDTO> searchAll(SearchRequest request) {
        SearchSpecification<Tag> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<Tag> tagsPage = tagRepository.findAll(pageable);

        return tagsPage.map(product -> mapToDTO(product, new TagDTO()));
    }
    

    @Transactional(readOnly = true)
    public TagDTO get(final UUID id) {
        return tagRepository.findById(id)
                .map(tag -> mapToDTO(tag, new TagDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public UUID create(final TagDTO tagDTO) {
        final Tag tag = new Tag();
        mapToEntity(tagDTO, tag);
        return tagRepository.save(tag).getId();
    }

    @Transactional
    public void update(final UUID id, final TagDTO tagDTO) {
        final Tag tag = tagRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(tagDTO, tag);
        tagRepository.save(tag);
    }

    @Transactional
    public void delete(final UUID id) {
        tagRepository.deleteById(id);
    }

    private TagDTO mapToDTO(final Tag tag, final TagDTO tagDTO) {
        tagDTO.setId(tag.getId());
        tagDTO.setName(tag.getName());
        tagDTO.setStatus(tag.getStatus());
        tagDTO.setIsArchived(tag.getIsArchived());
        tagDTO.setCanDelete(tag.getCanDelete());
        return tagDTO;
    }

    private Tag mapToEntity(final TagDTO tagDTO, final Tag tag) {
        tag.setName(tagDTO.getName());
        tag.setStatus(tagDTO.getStatus());
        tag.setIsArchived(tagDTO.getIsArchived());
        tag.setCanDelete(tagDTO.getCanDelete());
        return tag;
    }

    public boolean nameExists(final String name) {
        return tagRepository.existsByNameIgnoreCase(name);
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Tag tag = tagRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Variant tagVariant = variantRepository.findFirstByTag(tag);
        if (tagVariant != null) {
            referencedWarning.setKey("tag.variant.tag.referenced");
            referencedWarning.addParam(tagVariant.getId());
            return referencedWarning;
        }
        return null;
    }

}
