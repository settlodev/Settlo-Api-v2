package co.tz.settlo.api.csv_parsers.product_csv_parser;

import com.opencsv.bean.CsvBindByName;

/// Represents a single product in a CSV file
public class Product {
    @CsvBindByName
    public String product;

    @CsvBindByName
    public String category;

    @CsvBindByName
    public String variant;

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
        return String.format("product = {%s}, category = {%s}, variant = {%s}, price = {%f}, cost = {%f}, quantity = {%f}, sku = {%s}, barcode = {%s}", product.trim(), category.trim(), variant.trim(), price, cost, quantity, sku.trim(), barcode.trim());
    }
}