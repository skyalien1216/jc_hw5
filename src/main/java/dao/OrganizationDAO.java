package dao;


import entity.Organization;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public final class OrganizationDAO implements DAO<Organization> {
    private final @NotNull Connection connection;
    private final String schema;

    public OrganizationDAO(@NotNull Connection connection, String schema) {
        this.connection = connection;
        this.schema = schema;
    }

    @Override
    public @NotNull Organization get(int id) {
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery("SELECT inn, name, bank_account FROM "+ schema +".organization WHERE inn = " + id)) {
                if (resultSet.next()) {
                    return new Organization(resultSet.getInt("inn"), resultSet.getString("name"), resultSet.getString("bank_account"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ORGANIZATIONDAO::GET");
        }
        throw new IllegalStateException("Organization with inn " + id + " not found");
    }

    @Override
    public @NotNull List<Organization> all() {
        final List<Organization> result = new ArrayList<>();
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery("SELECT * FROM "+ schema +".organization")) {
                while (resultSet.next()) {
                    result.add(new Organization(resultSet.getInt("inn"), resultSet.getString("name"), resultSet.getString("bank_account")));
                }
                return result;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ORGANIZATIONDAO::ALL");
        }
    }

    @Override
    public void save(@NotNull Organization entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+ schema +".organization(inn, name, bank_account) VALUES(?,?,?)")) {
            preparedStatement.setInt(1, entity.getInn());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, entity.getBankAccount());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ORGANIZATIONDAO::SAVE");
        }
    }

    @Override
    public void update(@NotNull Organization entity) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "+ schema +".organization SET name = ?, bank_account = ? WHERE inn = ?")) {
            int fieldIndex = 1;
            preparedStatement.setString(fieldIndex++, entity.getName());
            preparedStatement.setString(fieldIndex++, entity.getBankAccount());
            preparedStatement.setInt(fieldIndex, entity.getInn());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ORGANIZATIONDAO::UPDATE");
        }
    }

    @Override
    public void delete(@NotNull Organization entity) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+ schema +".organization WHERE inn = ?")) {
            preparedStatement.setInt(1, entity.getInn());
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalStateException("Organization with inn = " + entity.getInn() + " not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ORGANIZATIONDAO::DELETE");
        }
    }
}
