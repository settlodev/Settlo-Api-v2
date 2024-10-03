package co.tz.settlo.api.product_csv_parser.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/// Exception to be returned when a product is missing a Category
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingCategoryException extends RuntimeException {
    public MissingCategoryException(int lineNumber, String productName) {
        super(String.format("Product '%s' is missing a category at line number %d", productName, lineNumber));
    }
}