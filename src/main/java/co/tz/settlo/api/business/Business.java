package co.tz.settlo.api.business;

import co.tz.settlo.api.country.Country;
import co.tz.settlo.api.location.Location;
import co.tz.settlo.api.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Businesses")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Business {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(length = 50)
    private String prefix;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double tax;

    @Column(length = 50)
    private String identificationNumber;

    @Column(length = 50)
    private String vrn;

    @Column(length = 100)
    private String serial;

    @Column(length = 100)
    private String uin;

    @Column(length = 50)
    private String receiptPrefix;

    @Column(length = 50)
    private String receiptSuffix;

    @Column(length = 100)
    private String businessType;

    @Column(nullable = false, length = 100)
    private String slug;

    @Column(length = 100)
    private String storeName;

    @Column
    private String image;

    @Column
    private String receiptImage;

    @Column
    private String logo;

    @Column
    private String facebook;

    @Column
    private String twitter;

    @Column
    private String instagram;

    @Column
    private String linkedin;

    @Column
    private String youtube;

    @Column
    private String certificateOfIncorporation;

    @Column
    private String businessIdentificationDocument;

    @Column
    private String businessLicense;

    @Column
    private String memarts;

    @Column
    private String notificationPhone;

    @Column(nullable = false)
    private String notificationEmailAddress;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private Boolean vfdRegistrationState;

    @Column
    private String website;

    @Column(nullable = false)
    private Boolean canDelete;

    @Column(nullable = false)
    private Boolean isArchived;

    @Column(nullable = false)
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @OneToMany(mappedBy = "business")
    private Set<Location> locations;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
