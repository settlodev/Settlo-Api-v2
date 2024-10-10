package co.tz.settlo.api.controllers.pending_product_variants;

import co.tz.settlo.api.controllers.pending_product.PendingProduct;
import co.tz.settlo.api.controllers.tag.Tag;
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
@Table(name = "PendingProductVariants")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class PendingVariant {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column
    private String sku;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private String barcode;

    @Column
    private String image;

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = false)
    private Boolean canDelete;

    @Column(nullable = false)
    private Boolean isArchived;

    @Column(length = 100)
    private String color;

    @Column
    private Boolean taxIncluded;

    @Column(precision = 10, scale = 2)
    private BigDecimal taxAmount;

    @Column
    @Enumerated(EnumType.STRING)
    private TaxClass taxClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private PendingProduct pendingProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
