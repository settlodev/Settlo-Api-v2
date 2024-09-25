package co.tz.settlo.api.kyc;

import co.tz.settlo.api.user.User;
import co.tz.settlo.api.user.UserRepository;
import co.tz.settlo.api.util.NotFoundException;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class KycService {

    private final KycRepository kycRepository;
    private final UserRepository userRepository;

    public KycService(final KycRepository kycRepository, final UserRepository userRepository) {
        this.kycRepository = kycRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public KycDTO findByUserId(final UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);

        return kycRepository.findByUser(user)
                .map(kyc -> mapToDTO(kyc, new KycDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public KycDTO get(final UUID id) {
        return kycRepository.findById(id)
                .map(kyc -> mapToDTO(kyc, new KycDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public UUID create(final KycDTO kycDTO) {
        final Kyc kyc = new Kyc();
        mapToEntity(kycDTO, kyc);
        return kycRepository.save(kyc).getId();
    }

    @Transactional
    public void update(final UUID id, final KycDTO kycDTO) {
        final Kyc kyc = kycRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(kycDTO, kyc);
        kycRepository.save(kyc);
    }

    @Transactional
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
