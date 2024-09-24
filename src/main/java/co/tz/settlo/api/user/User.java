package co.tz.settlo.api.user;

import co.tz.settlo.api.country.Country;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "Users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class User {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, unique = true)
    private Integer prefix;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column
    private String avatar;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String companyName;

    @Column
    private String slug;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private Boolean verificationStatus;

    @Column
    private String region;

    @Column
    private String district;

    @Column
    private String ward;

    @Column(length = 10)
    private String areaCode;

    @Column(length = 100)
    private String identificationId;

    @Column
    private String municipal;

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = false)
    private Boolean isArchived;

    @Column(nullable = false)
    private Boolean isOwner;

    @Column(nullable = false)
    private Boolean canDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
