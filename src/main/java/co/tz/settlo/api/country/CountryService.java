package co.tz.settlo.api.country;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.business.BusinessRepository;
import co.tz.settlo.api.user.User;
import co.tz.settlo.api.user.UserRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;

    public CountryService(final CountryRepository countryRepository,
            final BusinessRepository businessRepository, final UserRepository userRepository) {
        this.countryRepository = countryRepository;
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
    }

    public List<CountryDTO> findAll() {
        final List<Country> countries = countryRepository.findAll(Sort.by("id"));
        return countries.stream()
                .map(country -> mapToDTO(country, new CountryDTO()))
                .toList();
    }

    public CountryDTO get(final UUID id) {
        return countryRepository.findById(id)
                .map(country -> mapToDTO(country, new CountryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final CountryDTO countryDTO) {
        final Country country = new Country();
        mapToEntity(countryDTO, country);
        return countryRepository.save(country).getId();
    }

    public void update(final UUID id, final CountryDTO countryDTO) {
        final Country country = countryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(countryDTO, country);
        countryRepository.save(country);
    }

    public void delete(final UUID id) {
        countryRepository.deleteById(id);
    }

    private CountryDTO mapToDTO(final Country country, final CountryDTO countryDTO) {
        countryDTO.setId(country.getId());
        countryDTO.setName(country.getName());
        countryDTO.setCode(country.getCode());
        countryDTO.setLocale(country.getLocale());
        countryDTO.setCurrencyCode(country.getCurrencyCode());
        countryDTO.setSupported(country.getSupported());
        countryDTO.setStatus(country.getStatus());
        countryDTO.setCanDelete(country.getCanDelete());
        countryDTO.setIsArchived(country.getIsArchived());
        return countryDTO;
    }

    private Country mapToEntity(final CountryDTO countryDTO, final Country country) {
        country.setName(countryDTO.getName());
        country.setCode(countryDTO.getCode());
        country.setLocale(countryDTO.getLocale());
        country.setCurrencyCode(countryDTO.getCurrencyCode());
        country.setSupported(countryDTO.getSupported());
        country.setStatus(countryDTO.getStatus());
        country.setCanDelete(countryDTO.getCanDelete());
        country.setIsArchived(countryDTO.getIsArchived());
        return country;
    }

    public boolean nameExists(final String name) {
        return countryRepository.existsByNameIgnoreCase(name);
    }

    public boolean codeExists(final String code) {
        return countryRepository.existsByCodeIgnoreCase(code);
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Country country = countryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Business countryBusiness = businessRepository.findFirstByCountry(country);
        if (countryBusiness != null) {
            referencedWarning.setKey("country.business.country.referenced");
            referencedWarning.addParam(countryBusiness.getId());
            return referencedWarning;
        }
        final User countryUser = userRepository.findFirstByCountry(country);
        if (countryUser != null) {
            referencedWarning.setKey("country.user.country.referenced");
            referencedWarning.addParam(countryUser.getId());
            return referencedWarning;
        }
        return null;
    }

}
