package entity;

import java.util.Objects;

public class InvoicePositions{
    private int id;
    private Invoice invoice;
    private int price;
    private int amount;
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvoicePositions that)) return false;
        return getId() == that.getId() && getPrice() == that.getPrice() && getAmount() == that.getAmount() && getInvoice().equals(that.getInvoice()) && getProduct().equals(that.getProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getInvoice(), getPrice(), getAmount(), getProduct());
    }

    @Override
    public String toString() {
        return "InvoicePositions{" +
                "id=" + id +
                ", invoice=" + invoice +
                ", price=" + price +
                ", amount=" + amount +
                ", product=" + product +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public InvoicePositions(int id, Invoice invoice, int price, int amount, Product product) {
        this.id = id;
        this.invoice = invoice;
        this.price = price;
        this.amount = amount;
        this.product = product;
    }
}
