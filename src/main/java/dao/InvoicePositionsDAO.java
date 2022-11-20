package dao;

import entity.Invoice;
import entity.InvoicePositions;
import entity.Organization;
import entity.Product;
import org.java_courses.CREDS;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public final class InvoicePositionsDAO implements DAO<InvoicePositions> {
    private final String schema;

    public InvoicePositionsDAO(String schema) {
        this.schema = schema;
    }

    @Override
    public @NotNull InvoicePositions get(int id) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            try (var statement = connection.createStatement()) {
                try (var resultSet = statement.executeQuery("SELECT ip.id, price, amount, inn, i.name," +
                        " bank_account, i.date, i.id as invoice_id,  p.code as product_id , p.name as product_name " +
                        "FROM " + schema + ".invoice_positions ip join " + schema + ".product p on ip.product_id = p.code join " +
                        "(SELECT inn, name, bank_account, i.id, i.date FROM " + schema + ".invoice i join " + schema + ".organization o " +
                        "on i.organization_id = o.inn) i on ip.invoice_id = i.id WHERE ip.id = " + id)) {
                    if (resultSet.next()) {
                        var invId = resultSet.getInt("invoice_id");
                        var ipid = resultSet.getInt("id");
                        var price = resultSet.getInt("price");
                        var amount = resultSet.getInt("amount");
                        var prId = resultSet.getInt("product_id");
                        var iName = resultSet.getString("name");
                        var inn = resultSet.getInt("inn");
                        var bAcc = resultSet.getString("bank_account");
                        var date = resultSet.getDate("date");
                        var pName = resultSet.getString("product_name");
                        var code = resultSet.getInt("product_id");
                        return new InvoicePositions(ipid, new Invoice(invId, date, new Organization(inn, iName, bAcc)), price,
                                amount, new Product(code, pName));
                    }
                }
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("INVOICEPOSITIONSDAO::GET");
        }
        throw new IllegalStateException("InvoicePosition with id " + id + " not found");
    }

    @Override
    public @NotNull List<InvoicePositions> all() {
        final List<InvoicePositions> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            try (var statement = connection.createStatement()) {
                try (var resultSet = statement.executeQuery("SELECT ip.id, price, amount, inn, i.name," +
                        " bank_account, i.date, i.id as invoice_id,  p.code as product_id , p.name as product_name " +
                        "FROM " + schema + ".invoice_positions ip join " + schema + ".product p on ip.product_id = p.code join " +
                        "(SELECT inn, name, bank_account, i.id, i.date FROM " + schema + ".invoice i join " + schema + ".organization o " +
                        "on i.organization_id = o.inn) i on ip.invoice_id = i.id")) {
                    while (resultSet.next()) {
                        var invId = resultSet.getInt("invoice_id");
                        var ipid = resultSet.getInt("id");
                        var price = resultSet.getInt("price");
                        var amount = resultSet.getInt("amount");
                        var prId = resultSet.getInt("product_id");
                        var iName = resultSet.getString("name");
                        var inn = resultSet.getInt("inn");
                        var bAcc = resultSet.getString("bank_account");
                        var date = resultSet.getDate("date");
                        var pName = resultSet.getString("product_name");
                        var code = resultSet.getInt("product_id");
                        result.add(new InvoicePositions(ipid, new Invoice(invId, date, new Organization(inn, iName, bAcc)), price,
                                amount, new Product(code, pName)));
                    }
                    return result;
                }
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("INVOICEPOSITIONSDAO::ALL");
        }
    }

    @Override
    public void save(@NotNull InvoicePositions entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + schema + ".invoice_positions(id, invoice_id, amount, price, product_id) VALUES(?,?,?,?,?)")) {
                preparedStatement.setInt(1, entity.getId());
                preparedStatement.setInt(2, entity.getInvoice().getId());
                preparedStatement.setInt(3, entity.getAmount());
                preparedStatement.setInt(4, entity.getPrice());
                preparedStatement.setInt(5, entity.getProduct().getCode());
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("INVOICEPOSITIONSDAO::SAVE");
        }
    }

    @Override
    public void update(@NotNull InvoicePositions entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + schema + ".invoice_positions SET invoice_id = ?, amount = ?, price = ?, product_id = ? WHERE id = ?")) {
                int fieldIndex = 1;
                preparedStatement.setInt(fieldIndex++, entity.getInvoice().getId());
                preparedStatement.setInt(fieldIndex++, entity.getAmount());
                preparedStatement.setInt(fieldIndex++, entity.getPrice());
                preparedStatement.setInt(fieldIndex++, entity.getProduct().getCode());
                preparedStatement.setInt(fieldIndex, entity.getId());
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("INVOICEPOSITIONSDAO::UPDATE");
        }
    }

    @Override
    public void delete(@NotNull InvoicePositions entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + schema + ".invoice_positions WHERE id = ?")) {
                preparedStatement.setInt(1, entity.getId());
                if (preparedStatement.executeUpdate() == 0) {
                    throw new IllegalStateException("InvoicePosition with id = " + entity.getId() + " not found");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("INVOICEPOSITIONSDAO::DELETE");
        }
    }
}
