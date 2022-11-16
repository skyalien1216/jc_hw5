package entity;

import java.sql.Date;
import java.util.Objects;

public class Invoice {
    private int id;
    private Date date;
    private Organization organization;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invoice invoice)) return false;
        return getId() == invoice.getId() && getDate().equals(invoice.getDate()) && getOrganization().equals(invoice.getOrganization());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDate(), getOrganization());
    }

    public Invoice(int id, Date date, Organization organization) {
        this.id = id;
        this.date = date;
        this.organization = organization;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", date=" + date +
                ", organization=" + organization +
                '}';
    }
}
