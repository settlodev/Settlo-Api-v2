package co.tz.settlo.api.user;

import co.tz.settlo.api.auth.*;
import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.business.BusinessRepository;
import co.tz.settlo.api.country.Country;
import co.tz.settlo.api.country.CountryRepository;
import co.tz.settlo.api.kyc.Kyc;
import co.tz.settlo.api.kyc.KycRepository;
import co.tz.settlo.api.role.Role;
import co.tz.settlo.api.role.RoleRepository;
import co.tz.settlo.api.role.RoleService;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.tz.settlo.api.util.UniqueIdGenerator;
import io.sentry.Sentry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final BusinessRepository businessRepository;
    private final KycRepository kycRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private static final int VERIFICATION_TOKEN_EXPIRY_HOURS = 1;
    private static final String TOKEN_NOT_FOUND = "Token not found";
    private static final String USER_NOT_FOUND = "User not found";
    private final UniqueIdGenerator uniqueIdGenerator;

    public UserService(final UserRepository userRepository,
                       final CountryRepository countryRepository, final BusinessRepository businessRepository,
                       final KycRepository kycRepository, final RoleRepository roleRepository, final PasswordEncoder passwordEncoder,
                       final RoleService roleService, final VerificationTokenRepository verificationTokenRepository, final PasswordResetTokenRepository passwordResetTokenRepository, UniqueIdGenerator uniqueIdGenerator) {
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
        this.businessRepository = businessRepository;
        this.kycRepository = kycRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.uniqueIdGenerator = uniqueIdGenerator;
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDTO get(final UUID id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public UserDTO get(final String email) {
        return userRepository.findFirstByEmail(email)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
    }

    @Transactional
    public UUID create(final UserRegistrationDTO userRegistrationDTO) {
        UserDTO userDTO = new UserDTO();

        userDTO.setEmail(userRegistrationDTO.getEmail());
        userDTO.setFirstName(userRegistrationDTO.getFirstName());
        userDTO.setLastName(userRegistrationDTO.getLastName());
        userDTO.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        userDTO.setPhoneNumber(userRegistrationDTO.getPhoneNumber());
        userDTO.setEmail(userRegistrationDTO.getEmail());
        userDTO.setAccountNumber(uniqueIdGenerator.generate());
        userDTO.setTheme("light");
        userDTO.setCanDelete(false);
        userDTO.setCountry(userRegistrationDTO.getCountry());
        userDTO.setStatus(true);
        userDTO.setConsent(true);
        userDTO.setIsArchived(false);
        userDTO.setIsOwner(true);

        User user = mapToEntity(userDTO, new User());

        user.setIsBusinessRegistrationComplete(false);
        user.setIsLocationRegistrationComplete(false);

        return userRepository.save(user).getId();
    }

    @Transactional
    public void update(final UUID id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    @Transactional
    public UserCheckDTO userCheck(final String username) {
        if (username == null || username.trim().isEmpty()) {
            return createUserCheckErrorResponse(400, "Username cannot be null or empty");
        }

        return userRepository.findFirstByEmail(username)
                .map(this::createUserCheckSuccessResponse)
                .orElseGet(() -> createUserCheckErrorResponse(404, "Account not found"));
    }

    @Transactional
    public UUID generateVerificationToken(String email) {
        VerificationToken verificationToken = verificationTokenRepository.findByEmail(email)
                .map(token -> {
                    // Update existing token
                    token.setUsed(false);
                    token.setExpiresAt(LocalDateTime.now().plusHours(VERIFICATION_TOKEN_EXPIRY_HOURS));
                    token.setTokenId(UUID.randomUUID());
                    return token;
                })
                .orElseGet(() -> {
                    // Create new token
                    return VerificationToken.builder()
                            .email(email)
                            .used(false)
                            .tokenId(UUID.randomUUID())
                            .expiresAt(LocalDateTime.now().plusHours(VERIFICATION_TOKEN_EXPIRY_HOURS))
                            .build();
                });

        VerificationToken savedToken = verificationTokenRepository.save(verificationToken);
        logger.info("Created/Updated verification token with ID: {} for email: {}", savedToken.getId(), email);

        return savedToken.getTokenId();
    }


    @Transactional
    public UUID verifyToken(final UUID token) {
        logger.info("Verifying token {}", token);
        VerificationToken tokenData = verificationTokenRepository.findByTokenId(token)
                .orElseThrow(() -> {
                    Sentry.captureException(new Error(TOKEN_NOT_FOUND + ": " + token));
                    return new NotFoundException(TOKEN_NOT_FOUND);
                });

        if (tokenData.getUsed()) {
            Sentry.captureException(new Error("Token already used: " + token));
            throw new IllegalStateException("Token already used");
        }

        UserDTO userDTO = get(tokenData.getEmail());
        userDTO.setEmailVerified(LocalDateTime.now());
        update(userDTO.getId(), userDTO);

        tokenData.setUsed(true);
        verificationTokenRepository.save(tokenData);

        return tokenData.getId();
    }

    @Transactional
    public UUID updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        PasswordResetToken tokenData = passwordResetTokenRepository.findByTokenId(updatePasswordDTO.token())
                .orElseThrow(() -> new NotFoundException(TOKEN_NOT_FOUND));

        if (tokenData.getUsed()) {
            throw new IllegalStateException("Token already used");
        }

        UserDTO userDTO = get(tokenData.getEmail());
        userDTO.setPassword(passwordEncoder.encode(updatePasswordDTO.password()));
        update(userDTO.getId(), userDTO);

        tokenData.setUsed(true);
        passwordResetTokenRepository.save(tokenData);

        return tokenData.getId();
    }

    @Transactional
    public void delete(final UUID id) {
        userRepository.deleteById(id);
    }

    public boolean accountNumberExists(final String accountNumber) {
        return userRepository.existsByAccountNumber(accountNumber);
    }

    public boolean emailExists(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    private UserCheckDTO createUserCheckSuccessResponse(User user) {
        return UserCheckDTO.builder()
                .username(user.getEmail())
                .phoneVerified(user.getPhoneNumberVerified())
                .emailVerified(user.getEmailVerified())
                .responseCode(200)
                .message("Account response success")
                .build();
    }

    private UserCheckDTO createUserCheckErrorResponse(int responseCode, String message) {
        return UserCheckDTO.builder()
                .username(null)
                .emailVerified(null)
                .phoneVerified(null)
                .responseCode(responseCode)
                .message(message)
                .build();
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setAccountNumber(user.getAccountNumber());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setAvatar(user.getAvatar());
        userDTO.setEmail(user.getEmail());
        userDTO.setSlug(user.getSlug());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setEmailVerified(user.getEmailVerified());
        userDTO.setPhoneNumberVerified(user.getPhoneNumberVerified());
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
        user.setAccountNumber(userDTO.getAccountNumber());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAvatar(userDTO.getAvatar());
        user.setEmail(userDTO.getEmail());
        user.setSlug(userDTO.getSlug());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setEmailVerified(userDTO.getEmailVerified());
        user.setPhoneNumberVerified(userDTO.getPhoneNumberVerified());
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
        user.setTheme(userDTO.getTheme());
        user.setPassword(userDTO.getPassword());

        final Role role = userDTO.getRole() == null ? null : roleRepository.findById(userDTO.getRole())
                .orElseThrow(() -> new NotFoundException("Role not found"));
        user.setRole(role);

        final Country country = userDTO.getCountry() == null ? null : countryRepository.findById(userDTO.getCountry())
                .orElseThrow(() -> new NotFoundException("country not found"));
        user.setCountry(country);
        return user;
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
