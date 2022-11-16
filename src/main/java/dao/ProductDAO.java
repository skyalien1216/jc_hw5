package dao;


import entity.Product;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public final class ProductDAO implements DAO<Product> {
    private final @NotNull Connection connection;
    private final String schema;

    public ProductDAO(@NotNull Connection connection, String schema) {
        this.connection = connection;
        this.schema = schema;
    }

    @Override
    public @NotNull Product get(int id) {
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery("SELECT code, name FROM "+ schema +".product" +
                    " WHERE code = " + id)) {
                if (resultSet.next()) {
                    return new Product(resultSet.getInt("code"), resultSet.getString("name"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("PRODUCTDAO::GET");
        }
        throw new IllegalStateException("Product with code " + id + " not found");
    }

    @Override
    public @NotNull List<Product> all() {
        final List<Product> result = new ArrayList<>();
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery("SELECT * FROM "+ schema +".product")) {
                while (resultSet.next()) {
                    result.add(new Product(resultSet.getInt("code"), resultSet.getString("name")));
                }
                return result;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("PRODUCTDAO::ALL");
        }
    }

    @Override
    public void save(@NotNull Product entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+ schema +".product(code,name) VALUES(?,?)")) {
            preparedStatement.setInt(1, entity.getCode());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("PRODUCTDAO::SAVE");
        }
    }

    @Override
    public void update(@NotNull Product entity) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "+ schema +".product SET name = ? WHERE code = ?")) {
            int fieldIndex = 1;
            preparedStatement.setString(fieldIndex++, entity.getName());
            preparedStatement.setInt(fieldIndex, entity.getCode());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("PRODUCTDAO::UPDATE");
        }
    }

    @Override
    public void delete(@NotNull Product entity) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+ schema +".product WHERE code = ?")) {
            preparedStatement.setInt(1, entity.getCode());
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalStateException("Product with code = " + entity.getCode() + " not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("PRODUCTDAO::DELETE");
        }
    }
}
