package co.tz.settlo.api.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    @ProductNameUnique
    private String name;

    @NotNull
    @Size(max = 100)
    @ProductSlugUnique
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

    private UUID brand;

}
