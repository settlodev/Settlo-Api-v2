package co.tz.settlo.api.controllers.subscription;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@Table(name = "Subscriptions")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Subscription {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private Double amount;

    @Column
    private Double discount;

//    @Column(nullable = false)
//    private OffsetDateTime startDate;
//
//    @Column(nullable = false)
//    private OffsetDateTime endDate;

    @Column(nullable = false)
    private String packageName;

    @Column(nullable = false, unique = true)
    private String packageCode;

    @Column(nullable = false)
    private Boolean isTrial;

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = false)
    private Boolean canDelete;

    @Column(nullable = false)
    private Boolean isArchived;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
