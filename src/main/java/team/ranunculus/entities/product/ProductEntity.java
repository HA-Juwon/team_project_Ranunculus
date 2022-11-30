package team.ranunculus.entities.product;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ProductEntity {
    public static final String ATTRIBUTE_NAME = "product";
    public static final String ATTRIBUTE_NAME_PLURAL = "products";
    public static ProductEntity build() {
        return new ProductEntity();
    }
    private int index;
    private String name;
    private int costPrice;
    private int netPrice;
    private byte[] image;
    private String mime;
    private byte[] prodDetailImage;
    private String prodDetailImageMime;
    private int capacity;
    private int category;
    private Date launchingDate;
    private int stock;
    private Date stockUpdate;
    private int salesQuantity;
    public int getIndex() {
        return index;
    }

    public ProductEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductEntity setName(String name) {
        this.name = name;
        return this;
    }
    public int getCostPrice() {
        return costPrice;
    }

    public ProductEntity setCostPrice(int costPrice) {
        this.costPrice = costPrice;
        return this;
    }
    public int getNetPrice() {
        return netPrice;
    }

    public ProductEntity setNetPrice(int netPrice) {
        this.netPrice = netPrice;
        return this;
    }

    public byte[] getImage() {
        return image;
    }

    public ProductEntity setImage(byte[] image) {
        this.image = image;
        return this;
    }

    public String getMime() {
        return mime;
    }

    public ProductEntity setMime(String mime) {
        this.mime = mime;
        return this;
    }

    public byte[] getProdDetailImage() {
        return prodDetailImage;
    }

    public ProductEntity setProdDetailImage(byte[] prodDetailImage) {
        this.prodDetailImage = prodDetailImage;
        return this;
    }

    public String getProdDetailImageMime() {
        return prodDetailImageMime;
    }

    public ProductEntity setProdDetailImageMime(String prodDetailImageMime) {
        this.prodDetailImageMime = prodDetailImageMime;
        return this;
    }

    public int getCapacity() {
        return capacity;
    }

    public ProductEntity setCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public int getCategory() {
        return category;
    }

    public ProductEntity setCategory(int category) {
        this.category = category;
        return this;
    }

    public Date getLaunchingDate() {
        return launchingDate;
    }

    public ProductEntity setLaunchingDate(Date launchingDate) {
        this.launchingDate = launchingDate;
        return this;
    }

    public int getStock() {
        return stock;
    }

    public ProductEntity setStock(int stock) {
        this.stock = stock;
        return this;
    }

    public Date getStockUpdate() {
        return stockUpdate;
    }

    public ProductEntity setStockUpdate(Date stockUpdate) {
        this.stockUpdate = stockUpdate;
        return this;
    }

    public int getSalesQuantity() {
        return salesQuantity;
    }

    public ProductEntity setSalesQuantity(int salesQuantity) {
        this.salesQuantity = salesQuantity;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity that = (ProductEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
