package co.tz.settlo.api.product_csv_parser.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CsvParseException extends RuntimeException {
    public CsvParseException(RuntimeException e) {
        super(e.toString());
    }
}