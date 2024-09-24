package co.tz.settlo.api.settlement;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.settlement_account.SettlementAccount;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Settlements")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Settlement {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String transactionReference;

    @Column
    private String externalReference;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(columnDefinition = "text")
    private String notes;

    @Column
    private LocalDateTime completionTime;

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = false)
    private Boolean canDelete;

    @Column(nullable = false)
    private Boolean isArchived;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private SettlementAccount account;

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
