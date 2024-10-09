package co.tz.settlo.api.controllers.discount;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

public class DiscountCodeGenerator {
    static String generateDiscountCode(final String discountName, final DiscountType discountType, final OffsetDateTime expirationDate) {
        final char discountTypeChar = switch (discountType) {
            case DiscountType.FIXED ->  'F';
            case DiscountType.PERCENTAGE -> 'P';
        };

        return String.format("%s-%s-%s", expirationDate.toEpochSecond(), discountTypeChar, discountName.substring(0, 5));
    }

}
