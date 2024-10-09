package co.tz.settlo.api.selcom_payment_integration.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class PaymentRequest {
    @JsonProperty("buyer_name")
    private String buyerName;

    private String vendor;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("buyer_email")
    private String buyerEmail;

    @JsonProperty("buyer_phone")
    private String buyerPhone;

    private BigDecimal amount;

    private String currency;

    private String webhook;

    private String redirect_url;

    @JsonProperty("no_of_items")
    private int numberOfItems;

    @JsonProperty("billing.firstname")
    private String billingFirstName;

    @JsonProperty("billing.lastname")
    private String billingLastName;

    @JsonProperty("billing.address_1")
    private String billingAddress;

    @JsonProperty("billing.city")
    private String billingCity;

    @JsonProperty("billing.state_or_region")
    private String billingRegion;

    @JsonProperty("billing.postcode_or_pobox")
    private String billingPostcode;

    @JsonProperty("billing.country")
    private String billingCountry;

    @JsonProperty("billing.phone")
    private String billingPhone;

    @JsonProperty("payment_methods")
    private String paymentMethods;
}
