package co.tz.settlo.api.reservation;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.customer.Customer;
import co.tz.settlo.api.location.Location;
import co.tz.settlo.api.product.Product;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ReservationRepository extends JpaRepository<Reservation, UUID>, JpaSpecificationExecutor<Reservation> {

    List<Reservation> findAllByLocationId(UUID locationId);

    Reservation findFirstByBusiness(Business business);

    Reservation findFirstByLocation(Location location);

    Reservation findFirstByCustomer(Customer customer);

    Reservation findFirstByProduct(Product product);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByEmailIgnoreCase(String email);

}
