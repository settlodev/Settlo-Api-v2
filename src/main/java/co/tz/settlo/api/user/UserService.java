package co.tz.settlo.api.user;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.business.BusinessRepository;
import co.tz.settlo.api.country.Country;
import co.tz.settlo.api.country.CountryRepository;
import co.tz.settlo.api.kyc.Kyc;
import co.tz.settlo.api.kyc.KycRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final BusinessRepository businessRepository;
    private final KycRepository kycRepository;

    public UserService(final UserRepository userRepository,
            final CountryRepository countryRepository, final BusinessRepository businessRepository,
            final KycRepository kycRepository) {
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
        this.businessRepository = businessRepository;
        this.kycRepository = kycRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final UUID id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(final UUID id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final UUID id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setPrefix(user.getPrefix());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setAvatar(user.getAvatar());
        userDTO.setEmail(user.getEmail());
        userDTO.setCompanyName(user.getCompanyName());
        userDTO.setSlug(user.getSlug());
        userDTO.setPhone(user.getPhone());
        userDTO.setVerificationStatus(user.getVerificationStatus());
        userDTO.setRegion(user.getRegion());
        userDTO.setDistrict(user.getDistrict());
        userDTO.setWard(user.getWard());
        userDTO.setAreaCode(user.getAreaCode());
        userDTO.setIdentificationId(user.getIdentificationId());
        userDTO.setMunicipal(user.getMunicipal());
        userDTO.setStatus(user.getStatus());
        userDTO.setIsArchived(user.getIsArchived());
        userDTO.setIsOwner(user.getIsOwner());
        userDTO.setCanDelete(user.getCanDelete());
        userDTO.setCountry(user.getCountry() == null ? null : user.getCountry().getId());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setPrefix(userDTO.getPrefix());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAvatar(userDTO.getAvatar());
        user.setEmail(userDTO.getEmail());
        user.setCompanyName(userDTO.getCompanyName());
        user.setSlug(userDTO.getSlug());
        user.setPhone(userDTO.getPhone());
        user.setVerificationStatus(userDTO.getVerificationStatus());
        user.setRegion(userDTO.getRegion());
        user.setDistrict(userDTO.getDistrict());
        user.setWard(userDTO.getWard());
        user.setAreaCode(userDTO.getAreaCode());
        user.setIdentificationId(userDTO.getIdentificationId());
        user.setMunicipal(userDTO.getMunicipal());
        user.setStatus(userDTO.getStatus());
        user.setIsArchived(userDTO.getIsArchived());
        user.setIsOwner(userDTO.getIsOwner());
        user.setCanDelete(userDTO.getCanDelete());
        final Country country = userDTO.getCountry() == null ? null : countryRepository.findById(userDTO.getCountry())
                .orElseThrow(() -> new NotFoundException("country not found"));
        user.setCountry(country);
        return user;
    }

    public boolean prefixExists(final Integer prefix) {
        return userRepository.existsByPrefix(prefix);
    }

    public boolean emailExists(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Business userBusiness = businessRepository.findFirstByUser(user);
        if (userBusiness != null) {
            referencedWarning.setKey("user.business.user.referenced");
            referencedWarning.addParam(userBusiness.getId());
            return referencedWarning;
        }
        final Kyc userKyc = kycRepository.findFirstByUser(user);
        if (userKyc != null) {
            referencedWarning.setKey("user.kyc.user.referenced");
            referencedWarning.addParam(userKyc.getId());
            return referencedWarning;
        }
        return null;
    }

}
