package co.tz.settlo.api.order_item;

import co.tz.settlo.api.staff.Staff;
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
@Table(name = "OrderItems")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class OrderItem {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(columnDefinition = "text")
    private String comment;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PreparationStatus preparationStatus;

    @Column(nullable = false)
    private Boolean canDelete;

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = false)
    private Boolean isArchived;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id_id", nullable = false)
    private Staff staffId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
