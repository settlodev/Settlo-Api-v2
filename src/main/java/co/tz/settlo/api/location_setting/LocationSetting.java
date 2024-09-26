package co.tz.settlo.api.location_setting;

import co.tz.settlo.api.location.Location;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "LocationSettings")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class LocationSetting {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal minimumSettlementAmount;

    @Column(nullable = false)
    private String systemPasscode;

    @Column(nullable = false)
    private String reportsPasscode;

    @Column(nullable = false)
    private Boolean isDefault;

    @Column(nullable = false)
    private Boolean trackInventory;

    @Column(nullable = false)
    private Boolean ecommerceEnabled;

    @Column(nullable = false)
    private Boolean enableNotifications;

    @Column(nullable = false)
    private Boolean useRecipe;

    @Column(nullable = false)
    private Boolean useDepartments;

    @Column(nullable = false)
    private Boolean useCustomPrice;

    @Column(nullable = false)
    private Boolean useWarehouse;

    @Column(nullable = false)
    private Boolean useShifts;

    @Column(nullable = false)
    private Boolean useKds;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private Boolean canDelete;

    @Column(nullable = false)
    private Boolean isArchived;

    @OneToOne(mappedBy = "setting")
    private Location location;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
