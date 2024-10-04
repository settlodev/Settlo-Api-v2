package co.tz.settlo.api.product_csv_parser;

import co.tz.settlo.api.product_csv_parser.exceptions.CsvParseException;
import co.tz.settlo.api.product_csv_parser.exceptions.MissingColumnException;
import co.tz.settlo.api.product_csv_parser.exceptions.MissingFieldException;
import com.opencsv.bean.CsvToBeanBuilder;

import java.util.List;

/// Represents all products rows in the CSV
public class ProductsCsv {
    final List<Product> products;

    ProductsCsv(CsvToBeanBuilder<Product> csvToBeanBuilder) {
        try {
            this.products = csvToBeanBuilder.withType(Product.class).build().parse();
        } catch (RuntimeException e) {
            throw new CsvParseException(e);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (final Product product : products) {
            stringBuilder.append(product).append("\n");
        }
        return stringBuilder.toString();
    }

    /// Perform checks to make sure the products CSV file meets all
    /// the expected requirements
    void performChecks() {
        checkMissingColumn();
        checkMissingField();
    }

    /// Checks for missing columns in the Products CSV
    /// Throws MissingColumnException when a column is not found
    void checkMissingColumn() {
        for (final Product product: products) {
           if (product.product == null) {
              throw new MissingColumnException("product");
            }

            if (product.category == null) {
                throw new MissingColumnException("category");
            }

            if (product.variant == null) {
                throw new MissingColumnException("variant");
            }

            if (product.price == null) {
                throw new MissingColumnException("price");
            }

            if (product.cost == null) {
                throw new MissingColumnException("cost");
            }

            if (product.quantity == null) {
                throw new MissingColumnException("quantity");
            }

            if (product.sku == null) {
                throw new MissingColumnException("sku");
            }

            if (product.barcode == null) {
                throw new MissingColumnException("barcode");
            }

        }
    }

    /// Checks for missing fields in the Products CSV
    /// Throws `MissingFieldException` when a field is not found
    void checkMissingField() {
        int currentLineNumber = 1;

        for (final Product product: products) {
            if (product.product.isBlank()) {
                throw new MissingFieldException(currentLineNumber, "product");
            }

            if (product.category.isBlank()) {
                throw new MissingFieldException(currentLineNumber, "category");
            }

            if (product.variant.isBlank()) {
                throw new MissingFieldException(currentLineNumber, "variant");
            }

        }
    }
}