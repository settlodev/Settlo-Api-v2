package co.tz.settlo.api.location_subscription;

import co.tz.settlo.api.common.models.SubscriptionStatus;
import co.tz.settlo.api.subscription.Subscription;
import co.tz.settlo.api.util.NotFoundException;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "LocationSubscriptions")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class LocationSubscription {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private OffsetDateTime startDate;

    @Column(nullable = false)
    private OffsetDateTime endDate;

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

    @Column(nullable = false)
    private SubscriptionStatus subscriptionStatus;

    @Column(nullable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    /// Creates a Trial Location Subscription
    ///
    /// Sets the trial period to 7 Days and Subscription status to Trial
    public static LocationSubscription createTrial(Subscription trialSubscription) {
        OffsetDateTime now =OffsetDateTime.now() ;
        LocationSubscription trialLocationSubscription = new LocationSubscription();

        trialLocationSubscription.setActive(true);
        trialLocationSubscription.setStartDate(now);
        trialLocationSubscription.setEndDate(now.plusDays(7));
        trialLocationSubscription.setActive(true);
        trialLocationSubscription.setCanDelete(false);
        trialLocationSubscription.setIsArchived(false);
        trialLocationSubscription.setStatus(true);
        trialLocationSubscription.setSubscription(trialSubscription);
        trialLocationSubscription.setSubscriptionStatus(SubscriptionStatus.TRIAL);

        return trialLocationSubscription;
    }
}
