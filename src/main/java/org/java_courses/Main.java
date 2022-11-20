package org.java_courses;

import dao.InvoiceDAO;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        final Flyway flyway = Flyway
                .configure().cleanDisabled(false)
                .dataSource("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)
                .locations("db")
                .load();
        flyway.clean();
        flyway.migrate();
        System.out.println("Migrations applied successfully");

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
            var dao = new InvoiceDAO("jc_hw5_kha");
            dao.all().forEach(System.out::println);
            destroyDB(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void destroyDB(Connection connection)
    {
        try {
            connection.createStatement().executeUpdate("drop table jc_hw5_kha.organization cascade;" +
                    "drop table jc_hw5_kha.product cascade;" +
                    "drop table jc_hw5_kha.invoice cascade;" +
                    "drop table jc_hw5_kha.invoice_positions cascade;");
        } catch (SQLException e) {
            System.out.println("Destroying Database failed!!");
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}