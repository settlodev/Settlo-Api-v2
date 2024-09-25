package co.tz.settlo.api.customer;

import co.tz.settlo.api.discount.Discount;
import co.tz.settlo.api.discount.DiscountRepository;
import co.tz.settlo.api.reservation.Reservation;
import co.tz.settlo.api.reservation.ReservationRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
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
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final DiscountRepository discountRepository;
    private final ReservationRepository reservationRepository;

    public CustomerService(final CustomerRepository customerRepository,
            final DiscountRepository discountRepository,
            final ReservationRepository reservationRepository) {
        this.customerRepository = customerRepository;
        this.discountRepository = discountRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> findAll(UUID locationId) {
        final List<Customer> customers = customerRepository.findAllByLocationId(locationId);
        return customers.stream()
                .map(customer -> mapToDTO(customer, new CustomerDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public CustomerDTO get(final UUID id) {
        return customerRepository.findById(id)
                .map(customer -> mapToDTO(customer, new CustomerDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<CustomerDTO> searchAll(SearchRequest request) {
        SearchSpecification<Customer> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<Customer> customersPage = customerRepository.findAll(specification, pageable);

        return customersPage.map(customer -> mapToDTO(customer, new CustomerDTO()));
    }

    @Transactional
    public UUID create(final CustomerDTO customerDTO) {
        final Customer customer = new Customer();
        mapToEntity(customerDTO, customer);
        return customerRepository.save(customer).getId();
    }

    @Transactional
    public void update(final UUID id, final CustomerDTO customerDTO) {
        final Customer customer = customerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(customerDTO, customer);
        customerRepository.save(customer);
    }

    @Transactional
    public void delete(final UUID id) {
        customerRepository.deleteById(id);
    }

    private CustomerDTO mapToDTO(final Customer customer, final CustomerDTO customerDTO) {
        customerDTO.setId(customer.getId());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setGender(customer.getGender());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setAllowNotifications(customer.getAllowNotifications());
        customerDTO.setStatus(customer.getStatus());
        customerDTO.setIsArchived(customer.getIsArchived());
        customerDTO.setCanDelete(customer.getCanDelete());
        return customerDTO;
    }

    private Customer mapToEntity(final CustomerDTO customerDTO, final Customer customer) {
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setGender(customerDTO.getGender());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setEmail(customerDTO.getEmail());
        customer.setAllowNotifications(customerDTO.getAllowNotifications());
        customer.setStatus(customerDTO.getStatus());
        customer.setIsArchived(customerDTO.getIsArchived());
        customer.setCanDelete(customerDTO.getCanDelete());
        return customer;
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Customer customer = customerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Discount customerDiscount = discountRepository.findFirstByCustomer(customer);
        if (customerDiscount != null) {
            referencedWarning.setKey("customer.discount.customer.referenced");
            referencedWarning.addParam(customerDiscount.getId());
            return referencedWarning;
        }
        final Reservation customerReservation = reservationRepository.findFirstByCustomer(customer);
        if (customerReservation != null) {
            referencedWarning.setKey("customer.reservation.customer.referenced");
            referencedWarning.addParam(customerReservation.getId());
            return referencedWarning;
        }
        return null;
    }

}
