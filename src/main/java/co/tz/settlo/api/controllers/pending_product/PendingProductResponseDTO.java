package co.tz.settlo.api.controllers.pending_product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class PendingProductResponseDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    @PendingProductNameUnique
    private String name;

    @NotNull
    @Size(max = 100)
    @PendingProductSlugUnique
    private String slug;

    @Size(max = 255)
    private String sku;

    @Size(max = 255)
    private String image;

    private String description;

    @Size(max = 100)
    private String color;

    private Boolean sellOnline;

    @NotNull
    private Boolean status;

    @NotNull
    private Boolean canDelete;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    @NotNull
    private UUID business;

    private UUID location;

    private UUID department;

    private String departmentName;

    private UUID brand;

    private String brandName;
}
