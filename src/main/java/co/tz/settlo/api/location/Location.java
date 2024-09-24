package co.tz.settlo.api.location;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.location_setting.LocationSetting;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Locations")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Location {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String region;

    @Column(length = 100)
    private String street;

    @Column
    private String address;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false, length = 10)
    private String openingTime;

    @Column(nullable = false, length = 10)
    private String closingTime;

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = false)
    private Boolean isArchived;

    @Column(nullable = false)
    private Boolean canDelete;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setting_id", nullable = false, unique = true)
    private LocationSetting setting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
