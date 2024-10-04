package co.tz.settlo.api.product_csv_parser;

import com.opencsv.bean.CsvBindByName;

public class ProductsValidator {
    @CsvBindByName
    public String productName;

    @CsvBindByName
    public String category;

    @CsvBindByName
    public String variantName;

    @CsvBindByName
    public Double price;

    @CsvBindByName
    public Double cost;

    @CsvBindByName
    public Double quantity;

    @CsvBindByName
    public String sku;

    @CsvBindByName
    public String barcode;

    @Override
    public String toString() {
        return String.format("product = {%s}, category = {%s}, variant = {%s}, price = {%f}, cost = {%f}, quantity = {%f}, sku = {%s}, barcode = {%s}", productName.trim(), category.trim(), variantName.trim(), price, cost, quantity, sku.trim(), barcode.trim());
    }
}