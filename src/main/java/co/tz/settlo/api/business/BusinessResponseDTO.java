package co.tz.settlo.api.business;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class BusinessResponseDTO {

    private UUID id;

    @Size(max = 50)
    private String prefix;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private Double tax;

    @Size(max = 50)
    private String identificationNumber;

    @Size(max = 50)
    private String vrn;

    @Size(max = 100)
    private String serial;

    @Size(max = 100)
    private String uin;

    @Size(max = 50)
    private String receiptPrefix;

    @Size(max = 50)
    private String receiptSuffix;

    @Size(max = 100)
    private String businessType;

    @NotNull
    @Size(max = 100)
    private String slug;

    @Size(max = 100)
    private String storeName;

    @Size(max = 255)
    private String image;

    @Size(max = 255)
    private String receiptImage;

    @Size(max = 255)
    private String logo;

    @Size(max = 255)
    private String facebook;

    @Size(max = 255)
    private String twitter;

    @Size(max = 255)
    private String instagram;

    @Size(max = 255)
    private String linkedin;

    @Size(max = 255)
    private String youtube;

    @Size(max = 255)
    private String certificateOfIncorporation;

    @Size(max = 255)
    private String businessIdentificationDocument;

    @Size(max = 255)
    private String businessLicense;

    @Size(max = 255)
    private String memarts;

    @Size(max = 255)
    private String notificationPhone;

    @NotNull
    @Size(max = 255)
    private String notificationEmailAddress;

    private String description;

    @NotNull
    private Boolean vfdRegistrationState;

    @Size(max = 255)
    private String website;

    @NotNull
    private Boolean canDelete;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    @NotNull
    private Boolean status;

    @NotNull
    private UUID user;

    @NotNull
    private UUID country;

    @NotNull
    private String countryName;

    @NotNull
    private int totalLocations;

}
