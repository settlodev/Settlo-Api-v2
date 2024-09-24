package co.tz.settlo.api.reservation;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.business.BusinessRepository;
import co.tz.settlo.api.customer.Customer;
import co.tz.settlo.api.customer.CustomerRepository;
import co.tz.settlo.api.location.Location;
import co.tz.settlo.api.location.LocationRepository;
import co.tz.settlo.api.product.Product;
import co.tz.settlo.api.product.ProductDTO;
import co.tz.settlo.api.product.ProductRepository;
import co.tz.settlo.api.util.NotFoundException;
import java.util.List;
import java.util.UUID;

import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import co.tz.settlo.api.util.RestApiFilter.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BusinessRepository businessRepository;
    private final LocationRepository locationRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public ReservationService(final ReservationRepository reservationRepository,
            final BusinessRepository businessRepository,
            final LocationRepository locationRepository,
            final CustomerRepository customerRepository,
            final ProductRepository productRepository) {
        this.reservationRepository = reservationRepository;
        this.businessRepository = businessRepository;
        this.locationRepository = locationRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservationDTO> findAll(UUID locationId) {
        final List<Reservation> reservations = reservationRepository.findAllByLocationId(locationId);
        return reservations.stream()
                .map(reservation -> mapToDTO(reservation, new ReservationDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<ReservationDTO> searchAll(SearchRequest request) {
        SearchSpecification<Reservation> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<Reservation> reservationsPage = reservationRepository.findAll(specification, pageable);

        return reservationsPage.map(reservation -> mapToDTO(reservation, new ReservationDTO()));
    }


    @Transactional(readOnly = true)
    public ReservationDTO get(final UUID id) {
        return reservationRepository.findById(id)
                .map(reservation -> mapToDTO(reservation, new ReservationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public UUID create(final ReservationDTO reservationDTO) {
        final Reservation reservation = new Reservation();
        mapToEntity(reservationDTO, reservation);
        return reservationRepository.save(reservation).getId();
    }

    @Transactional
    public void update(final UUID id, final ReservationDTO reservationDTO) {
        final Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reservationDTO, reservation);
        reservationRepository.save(reservation);
    }

    @Transactional
    public void delete(final UUID id) {
        reservationRepository.deleteById(id);
    }

    private ReservationDTO mapToDTO(final Reservation reservation,
            final ReservationDTO reservationDTO) {
        reservationDTO.setId(reservation.getId());
        reservationDTO.setDate(reservation.getDate());
        reservationDTO.setStartDate(reservation.getStartDate());
        reservationDTO.setEndDate(reservation.getEndDate());
        reservationDTO.setNumberOfPeople(reservation.getNumberOfPeople());
        reservationDTO.setName(reservation.getName());
        reservationDTO.setPhone(reservation.getPhone());
        reservationDTO.setEmail(reservation.getEmail());
        reservationDTO.setStatus(reservation.getStatus());
        reservationDTO.setCanDelete(reservation.getCanDelete());
        reservationDTO.setIsArchived(reservation.getIsArchived());
        reservationDTO.setBusiness(reservation.getBusiness() == null ? null : reservation.getBusiness().getId());
        reservationDTO.setLocation(reservation.getLocation() == null ? null : reservation.getLocation().getId());
        reservationDTO.setCustomer(reservation.getCustomer() == null ? null : reservation.getCustomer().getId());
        reservationDTO.setProduct(reservation.getProduct() == null ? null : reservation.getProduct().getId());
        return reservationDTO;
    }

    private Reservation mapToEntity(final ReservationDTO reservationDTO,
            final Reservation reservation) {
        reservation.setDate(reservationDTO.getDate());
        reservation.setStartDate(reservationDTO.getStartDate());
        reservation.setEndDate(reservationDTO.getEndDate());
        reservation.setNumberOfPeople(reservationDTO.getNumberOfPeople());
        reservation.setName(reservationDTO.getName());
        reservation.setPhone(reservationDTO.getPhone());
        reservation.setEmail(reservationDTO.getEmail());
        reservation.setStatus(reservationDTO.getStatus());
        reservation.setCanDelete(reservationDTO.getCanDelete());
        reservation.setIsArchived(reservationDTO.getIsArchived());
        final Business business = reservationDTO.getBusiness() == null ? null : businessRepository.findById(reservationDTO.getBusiness())
                .orElseThrow(() -> new NotFoundException("business not found"));
        reservation.setBusiness(business);
        final Location location = reservationDTO.getLocation() == null ? null : locationRepository.findById(reservationDTO.getLocation())
                .orElseThrow(() -> new NotFoundException("location not found"));
        reservation.setLocation(location);
        final Customer customer = reservationDTO.getCustomer() == null ? null : customerRepository.findById(reservationDTO.getCustomer())
                .orElseThrow(() -> new NotFoundException("customer not found"));
        reservation.setCustomer(customer);
        final Product product = reservationDTO.getProduct() == null ? null : productRepository.findById(reservationDTO.getProduct())
                .orElseThrow(() -> new NotFoundException("product not found"));
        reservation.setProduct(product);
        return reservation;
    }

    public boolean nameExists(final String name) {
        return reservationRepository.existsByNameIgnoreCase(name);
    }

    public boolean emailExists(final String email) {
        return reservationRepository.existsByEmailIgnoreCase(email);
    }

}
