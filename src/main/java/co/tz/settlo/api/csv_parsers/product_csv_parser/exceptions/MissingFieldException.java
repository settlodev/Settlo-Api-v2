package co.tz.settlo.api.csv_parsers.product_csv_parser.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/// Exception to be thrown when a product is missing a Variant
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingFieldException extends RuntimeException {
    public MissingFieldException(int lineNumber, String fieldName) {
        super(String.format("Missing field '%s' at line number %d", fieldName, lineNumber));
    }
}