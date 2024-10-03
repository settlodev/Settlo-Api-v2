package co.tz.settlo.api.product_csv_parser.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingColumnException extends RuntimeException {
    public MissingColumnException(String columnName) {
        super(String.format("The '%s' column is missing", columnName));
    }
}