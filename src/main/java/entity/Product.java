package entity;

import java.util.Objects;

public class Product {
    private String name;
    private int code;

    public Product(int code, String name) {
        this.name = name;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return getCode() == product.getCode() && getName().equals(product.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCode());
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", code=" + code +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
