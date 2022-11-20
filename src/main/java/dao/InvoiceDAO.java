package dao;


import entity.Invoice;
import entity.Organization;
import org.java_courses.CREDS;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public final class InvoiceDAO implements DAO<Invoice> {
    private final String schema;
    public InvoiceDAO(String schema) {
        this.schema = schema;
    }

    @Override
    public @NotNull Invoice get(int id) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            try (var statement = connection.createStatement()) {
                try (var resultSet = statement.executeQuery("SELECT inn, name, bank_account, i.id, i.date " +
                        "FROM " + schema + ".invoice i join " + schema + ".organization o on i.organization_id = o.inn" +
                        "  WHERE i.id = " + id)) {
                    if (resultSet.next()) {
                        var invId = resultSet.getInt("id");
                        var date = resultSet.getDate("date");
                        var inn = resultSet.getInt("inn");
                        var name = resultSet.getString("name");
                        var bAcc = resultSet.getString("bank_account");
                        return new Invoice(invId, date, new Organization(inn, name, bAcc));
                    }
                }
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("INVOICEDAO::GET");
        }
        throw new IllegalStateException("Invoice with id " + id + " not found");
    }

    @Override
    public @NotNull List<Invoice> all() {
        final List<Invoice> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            try (var statement = connection.createStatement()) {
                try (var resultSet = statement.executeQuery("SELECT inn, name, bank_account, i.id, i.date" +
                        " FROM " + schema + ".invoice i join " + schema + ".organization o on i.organization_id = o.inn")) {
                    while (resultSet.next()) {
                        var invId = resultSet.getInt("id");
                        var date = resultSet.getDate("date");
                        var inn = resultSet.getInt("inn");
                        var name = resultSet.getString("name");
                        var bAcc = resultSet.getString("bank_account");
                        result.add(new Invoice(invId, date, new Organization(inn, name, bAcc)));
                    }
                    return result;
                }
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("INVOICEDAO::ALL");
        }
    }

    @Override
    public void save(@NotNull Invoice entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + schema + ".invoice(id, date, organization_id) VALUES(?,?,?)")) {
                preparedStatement.setInt(1, entity.getId());
                preparedStatement.setDate(2, entity.getDate());
                preparedStatement.setInt(3, entity.getOrganization().getInn());
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("INVOICEDAO::SAVE");
        }
    }

    @Override
    public void update(@NotNull Invoice entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + schema + ".invoice SET date = ?, organization_id = ? WHERE id = ?")) {
                int fieldIndex = 1;
                preparedStatement.setDate(fieldIndex++, entity.getDate());
                preparedStatement.setInt(fieldIndex++, entity.getOrganization().getInn());
                preparedStatement.setInt(fieldIndex, entity.getId());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("INVOICEDAO::UPDATE");
        }
    }

    @Override
    public void delete(@NotNull Invoice entity) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + schema + ".invoice WHERE id = ?")) {
                preparedStatement.setInt(1, entity.getId());
                if (preparedStatement.executeUpdate() == 0) {
                    throw new IllegalStateException("Invoice with id = " + entity.getId() + " not found");
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("INVOICEDAO::DELETE");
        }
    }
}
