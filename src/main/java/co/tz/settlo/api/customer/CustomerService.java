package co.tz.settlo.api.customer;

import co.tz.settlo.api.discount.Discount;
import co.tz.settlo.api.discount.DiscountRepository;
import co.tz.settlo.api.reservation.Reservation;
import co.tz.settlo.api.reservation.ReservationRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


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

    public List<CustomerDTO> findAll() {
        final List<Customer> customers = customerRepository.findAll(Sort.by("id"));
        return customers.stream()
                .map(customer -> mapToDTO(customer, new CustomerDTO()))
                .toList();
    }

    public CustomerDTO get(final UUID id) {
        return customerRepository.findById(id)
                .map(customer -> mapToDTO(customer, new CustomerDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final CustomerDTO customerDTO) {
        final Customer customer = new Customer();
        mapToEntity(customerDTO, customer);
        return customerRepository.save(customer).getId();
    }

    public void update(final UUID id, final CustomerDTO customerDTO) {
        final Customer customer = customerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(customerDTO, customer);
        customerRepository.save(customer);
    }

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
