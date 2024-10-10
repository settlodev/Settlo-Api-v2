package co.tz.settlo.api.csv_parsers.product_csv_parser;

import co.tz.settlo.api.controllers.pending_product.PendingProduct;
import co.tz.settlo.api.controllers.pending_product_variants.PendingVariant;
import com.opencsv.bean.CsvBindByName;
import lombok.Getter;

import java.math.BigDecimal;

/// Represents a single product in a CSV file
public class Product {
    @CsvBindByName
    public String product;

    @Getter
    @CsvBindByName
    public String category;

    @CsvBindByName
    public String variant;

    @CsvBindByName
    public BigDecimal price;

    @CsvBindByName
    public BigDecimal cost;

    @CsvBindByName
    public BigDecimal quantity;

    @CsvBindByName
    public String sku;

    @CsvBindByName
    public String barcode;

    @Override
    public String toString() {
        return String.format("product = {%s}, category = {%s}, variant = {%s}, price = {%f}, cost = {%f}, quantity = {%f}, sku = {%s}, barcode = {%s}", product.trim(), category.trim(), variant.trim(), price, cost, quantity, sku.trim(), barcode.trim());
    }

    public PendingProduct getPendingProduct() {
        PendingProduct pendingProduct = new PendingProduct();

        pendingProduct.setName(this.product);
        pendingProduct.setSlug(this.product);
        pendingProduct.setCanDelete(true);
        pendingProduct.setIsArchived(false);
        pendingProduct.setStatus(true);

        return pendingProduct;
    }

    public PendingVariant getPendingVariant() {
        PendingVariant pendingVariant = new PendingVariant();

        pendingVariant.setName(this.variant);
        pendingVariant.setCost(this.cost);
        pendingVariant.setPrice(this.price);
        pendingVariant.setQuantity(this.quantity);
        pendingVariant.setSku(this.sku);
        pendingVariant.setBarcode(this.barcode);
        pendingVariant.setCanDelete(true);
        pendingVariant.setIsArchived(false);
        pendingVariant.setStatus(true);

        return pendingVariant;
    }

}