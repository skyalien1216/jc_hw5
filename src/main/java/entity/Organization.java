package entity;

import java.util.Objects;

public class Organization {
    private String name;
    private String bankAccount;
    private int inn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organization that)) return false;
        return getInn() == that.getInn() && getName().equals(that.getName()) && Objects.equals(getBankAccount(), that.getBankAccount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getBankAccount(), getInn());
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public int getInn() {
        return inn;
    }

    public void setInn(int inn) {
        this.inn = inn;
    }

    public Organization( int inn, String name, String bankAccount) {
        this.name = name;
        this.bankAccount = bankAccount;
        this.inn = inn;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "name='" + name + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", inn=" + inn +
                '}';
    }
}
