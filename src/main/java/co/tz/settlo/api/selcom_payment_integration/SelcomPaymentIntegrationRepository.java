package co.tz.settlo.api.selcom_payment_integration;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.codec.digest.HmacUtils;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Repository
public class SelcomPaymentIntegrationRepository {

    private static final String VENDOR = "TILL60904810";
    private static final String KEY = "SETTLO-WsGHweDFyW5OOiAs";
    private static final String SECRET = "989LLk3-ktud-54fa-Pk63-8dh7y9eb69b4";
    private static final String BASE_URL = "https://apigw.selcommobile.com/v1";
    private static final String PUSH_URL = "https://apigw.selcommobile.com/v1/checkout/wallet-payment";
    private static final String SUCCESS_URL = "https://settlo.co.tz/payment-success.html";
    private static final String FAILED_URL = "https://settlo.co.tz/payment-failed.html";
    private static final String HOOK_URL = "http://167.71.33.187/api/pos/v23/listener";

    private final RestTemplate restTemplate;

    public SelcomRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getFailedUrl() {
        return FAILED_URL;
    }

    public String getVendor() {
        return VENDOR;
    }

    public String getKey() {
        return KEY;
    }

    public String getSecret() {
        return SECRET;
    }

    public String getUrl() {
        return BASE_URL;
    }

    public String getSuccessUrl() {
        return SUCCESS_URL;
    }

    public String getHook() {
        return HOOK_URL;
    }

    public Map<String, Object> request(Map<String, String> args) {
        Map<String, Object> body = new HashMap<>();
        body.put("buyer_name", args.get("buyer_name"));
        body.put("vendor", VENDOR);
        body.put("order_id", args.get("order_id"));
        body.put("buyer_email", "settloup@gmail.com");
        body.put("buyer_phone", args.get("buyer_phone"));
        body.put("amount", args.get("amount"));
        body.put("currency", args.get("currency"));
        body.put("webhook", args.get("hook"));
        body.put("redirect_url", args.get("webhook"));
        body.put("no_of_items", args.get("total_items"));
        body.put("billing.firstname", args.get("firstname"));
        body.put("billing.lastname", args.get("lastname"));
        body.put("billing.address_1", args.get("address"));
        body.put("billing.city", args.get("city"));
        body.put("billing.state_or_region", args.get("region"));
        body.put("billing.postcode_or_pobox", args.get("pobox"));
        body.put("billing.country", args.get("country"));
        body.put("billing.phone", args.get("billing_phone"));
        body.put("payment_methods", args.get("payment_methods"));

        String authorization = Base64.getEncoder().encodeToString(KEY.getBytes(StandardCharsets.UTF_8));
        String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT);
        String signedFields = String.join(",", body.keySet());
        String digest = computeSignature(body, signedFields, timestamp);

        return sendToSelcomCreateOrderAPI(BASE_URL + "/checkout/create-order", body, authorization, digest, signedFields, timestamp);
    }

    public Map<String, Object> cashIn(Map<String, String> args) {
        Map<String, Object> body = new HashMap<>();
        body.put("transid", args.get("transid"));
        body.put("utilitycode", args.get("network_code"));
        body.put("utilityref", args.get("receiver_phone"));
        body.put("amount", args.get("amount"));
        body.put("currency", args.get("currency"));
        body.put("vendor", VENDOR);
        body.put("pin", args.get("pin"));
        body.put("msisdn", args.get("notification_phone"));

        String authorization = Base64.getEncoder().encodeToString(KEY.getBytes(StandardCharsets.UTF_8));
        String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT);
        String signedFields = String.join(",", body.keySet());
        String digest = computeSignature(body, signedFields, timestamp);

        return sendToSelcomCreateOrderAPI(BASE_URL + "/walletcashin/process", body, authorization, digest, signedFields, timestamp);
    }

    private Map<String, Object> sendToSelcomCreateOrderAPI(String url, Map<String, Object> body, String authorization, String digest, String signedFields, String timestamp) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("Accept", "application/json");
        headers.set("Authorization", "SELCOM " + authorization);
        headers.set("Digest-Method", "HS256");
        headers.set("Digest", digest);
        headers.set("Timestamp", timestamp);
        headers.set("Signed-Fields", signedFields);

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        return response.getBody();
    }

    private String computeSignature(Map<String, Object> params, String signedFields, String timestamp) {
        StringBuilder signData = new StringBuilder("timestamp=").append(timestamp);
        for (String key : signedFields.split(",")) {
            signData.append("&").append(key).append("=").append(params.get(key));
        }
        return Base64.getEncoder().encodeToString(new HmacUtils(HmacUtils.HMAC_SHA_256, SECRET).hmac(signData.toString()));
    }
}
