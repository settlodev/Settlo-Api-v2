package co.tz.settlo.api.kyc;

import co.tz.settlo.api.user.User;
import co.tz.settlo.api.user.UserRepository;
import co.tz.settlo.api.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class KycService {

    private final KycRepository kycRepository;
    private final UserRepository userRepository;

    public KycService(final KycRepository kycRepository, final UserRepository userRepository) {
        this.kycRepository = kycRepository;
        this.userRepository = userRepository;
    }

    public List<KycDTO> findAll() {
        final List<Kyc> kycs = kycRepository.findAll(Sort.by("id"));
        return kycs.stream()
                .map(kyc -> mapToDTO(kyc, new KycDTO()))
                .toList();
    }

    public KycDTO get(final UUID id) {
        return kycRepository.findById(id)
                .map(kyc -> mapToDTO(kyc, new KycDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final KycDTO kycDTO) {
        final Kyc kyc = new Kyc();
        mapToEntity(kycDTO, kyc);
        return kycRepository.save(kyc).getId();
    }

    public void update(final UUID id, final KycDTO kycDTO) {
        final Kyc kyc = kycRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(kycDTO, kyc);
        kycRepository.save(kyc);
    }

    public void delete(final UUID id) {
        kycRepository.deleteById(id);
    }

    private KycDTO mapToDTO(final Kyc kyc, final KycDTO kycDTO) {
        kycDTO.setId(kyc.getId());
        kycDTO.setGender(kyc.getGender());
        kycDTO.setDateOfBirth(kyc.getDateOfBirth());
        kycDTO.setCanDelete(kyc.getCanDelete());
        kycDTO.setIsArchived(kyc.getIsArchived());
        kycDTO.setStatus(kyc.getStatus());
        kycDTO.setUser(kyc.getUser() == null ? null : kyc.getUser().getId());
        return kycDTO;
    }

    private Kyc mapToEntity(final KycDTO kycDTO, final Kyc kyc) {
        kyc.setGender(kycDTO.getGender());
        kyc.setDateOfBirth(kycDTO.getDateOfBirth());
        kyc.setCanDelete(kycDTO.getCanDelete());
        kyc.setIsArchived(kycDTO.getIsArchived());
        kyc.setStatus(kycDTO.getStatus());
        final User user = kycDTO.getUser() == null ? null : userRepository.findById(kycDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        kyc.setUser(user);
        return kyc;
    }

}
