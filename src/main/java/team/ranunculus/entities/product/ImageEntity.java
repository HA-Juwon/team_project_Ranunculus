package team.ranunculus.entities.product;

import java.util.Date;
import java.util.Objects;

public class ImageEntity {
    public static final String ATTRIBUTE_NAME = "image";
    public static final String ATTRIBUTE_NAME_PLURAL = "images";

    public static ImageEntity build() {
        return new ImageEntity();
    }

    private int index;
    private String name;
    private String mime;
    private byte[] data;

    public ImageEntity() {
    }

    public ImageEntity(int index, String name, String mime, byte[] data) {
        this.index = index;
        this.name = name;
        this.mime = mime;
        this.data = data;
    }

    public int getIndex() {
        return index;
    }

    public ImageEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getName() {
        return name;
    }

    public ImageEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getMime() {
        return mime;
    }

    public ImageEntity setMime(String mime) {
        this.mime = mime;
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public ImageEntity setData(byte[] data) {
        this.data = data;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageEntity that = (ImageEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
