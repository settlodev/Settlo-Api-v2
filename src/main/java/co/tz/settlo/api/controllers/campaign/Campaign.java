package co.tz.settlo.api.controllers.campaign;

import co.tz.settlo.api.controllers.business.Business;
import co.tz.settlo.api.controllers.communication_template.BroadcastType;
import co.tz.settlo.api.controllers.communication_template.CommunicationTemplate;
import co.tz.settlo.api.controllers.location.Location;
import co.tz.settlo.api.controllers.sender_id.SenderId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "Campaigns")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Campaign {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, columnDefinition = "text")
    private String message;

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = false)
    private Boolean isArchived;

    @Column(nullable = false)
    private Boolean canDelete;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Recepient audience;

    @Column
    private String customMessage;

    @Column
    @Enumerated(EnumType.STRING)
    private BroadcastType broadcastType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id_id")
    private SenderId senderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "communication_template_id")
    private CommunicationTemplate communicationTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
