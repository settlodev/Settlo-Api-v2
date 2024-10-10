package co.tz.settlo.api.controllers.reservation;

import co.tz.settlo.api.controllers.business.Business;
import co.tz.settlo.api.controllers.customer.Customer;
import co.tz.settlo.api.controllers.location.Location;
import co.tz.settlo.api.controllers.pending_product.PendingProduct;
import co.tz.settlo.api.controllers.product.Product;

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

    Reservation findFirstByPendingProduct(PendingProduct pendingProduct);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByEmailIgnoreCase(String email);

}
