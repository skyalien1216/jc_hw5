package org.java_courses;

import entity.Product;
import javafx.util.Pair;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SQLTasksTest {

    @BeforeAll
    static void prepare(){
        var dbName = "postgres";
        var user = "postgres";
        var password = "skyalien";

        final Flyway flyway = Flyway
                .configure().cleanDisabled(false)
                .dataSource("jdbc:postgresql://localhost/" + dbName, user, password)
                .locations("testdb")
                .load();
        flyway.clean();
        flyway.migrate();
        System.out.println("Migrations applied successfully");

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + dbName, user, password);
            sqlTasks = new SQLTasks(connection, "jc_hw5_test");
        } catch (SQLException e) {
            System.out.println("Test preparation failed!!");
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static Connection connection;
    private static SQLTasks sqlTasks;

    @Test
    void task1() {
        assertEquals("[Organization{name='org6', bankAccount='null', inn=6}=95, Organization{name='org1', bankAccount='null', inn=1}=46, Organization{name='org2', bankAccount='null', inn=2}=40, Organization{name='org5', bankAccount='null', inn=5}=40, Organization{name='org3', bankAccount='null', inn=3}=17, Organization{name='org4', bankAccount='null', inn=4}=7]", sqlTasks.task1().toString());
    }

    @Test
    void task2() {
        List<Pair<Product,Integer>> list = new ArrayList<>();
        list.add(new Pair<>(new Product(1,"table"), 10));
        list.add(new Pair<>(new Product(2,"chair"), 5));
        assertEquals("[Organization{name='org2', bankAccount='null', inn=2}, " +
                "Organization{name='org1', bankAccount='null', inn=1}]", sqlTasks.task2(list).toString());
    }

    @Test
    void task3() {
        var res = sqlTasks.task3(new Date(118, 1, 1), new Date(119, 4, 1));
        assertEquals("[Task3Data{product=Product{name='table', code=1}, date=2019-01-01, sum=2275, amount=45}, Task3Data{product=Product{name='table', code=1}, date=2019-01-02, sum=340, amount=12}, Task3Data{product=Product{name='table', code=1}, date=2019-01-03, sum=600, amount=10}, Task3Data{product=Product{name='chair', code=2}, date=2019-01-01, sum=1340, amount=53}, Task3Data{product=Product{name='chair', code=2}, date=2019-01-02, sum=1290, amount=62}, Task3Data{product=Product{name='chair', code=2}, date=2019-01-03, sum=245, amount=7}, Task3Data{product=Product{name='spoon', code=3}, date=2019-01-01, sum=335, amount=15}, Task3Data{product=Product{name='spoon', code=3}, date=2019-01-02, sum=590, amount=16}, Task3Data{product=Product{name='spoon', code=3}, date=2019-01-03, sum=550, amount=25}]", res.getKey().toString());
        assertEquals("[Task3Data{product=Product{name='table', code=1}, date=null, sum=3215, amount=67}, Task3Data{product=Product{name='chair', code=2}, date=null, sum=2875, amount=122}, Task3Data{product=Product{name='spoon', code=3}, date=null, sum=1475, amount=56}]", res.getValue().toString());
    }

    @Test
    void task4() {
        var res = sqlTasks.task4(new Date(118, 1, 1), new Date(119,4,1));
        assertEquals("[Product{name='table', code=1}=52.0, Product{name='chair', code=2}=28.0, Product{name='spoon', code=3}=27.0]", res.toString());
    }

    @Test
    void task5() {
        var res = sqlTasks.task5(new Date(118, 1, 1), new Date(119,0,3));
        assertEquals("[Organization{name='org3', bankAccount='null', inn=3}=[Product{name='chair', code=2}], Organization{name='org11', bankAccount='null', inn=11}=[Product{name='null', code=0}], Organization{name='org4', bankAccount='null', inn=4}=[Product{name='table', code=1}], Organization{name='org10', bankAccount='null', inn=10}=[Product{name='null', code=0}], Organization{name='org5', bankAccount='null', inn=5}=[Product{name='chair', code=2}, Product{name='table', code=1}], Organization{name='org6', bankAccount='null', inn=6}=[Product{name='spoon', code=3}, Product{name='table', code=1}, Product{name='chair', code=2}], Organization{name='org7', bankAccount='null', inn=7}=[Product{name='null', code=0}], Organization{name='org8', bankAccount='null', inn=8}=[Product{name='null', code=0}], Organization{name='org1', bankAccount='null', inn=1}=[Product{name='chair', code=2}, Product{name='table', code=1}, Product{name='spoon', code=3}], Organization{name='org9', bankAccount='null', inn=9}=[Product{name='null', code=0}], Organization{name='org2', bankAccount='null', inn=2}=[Product{name='chair', code=2}, Product{name='table', code=1}, Product{name='spoon', code=3}]]", res.entrySet().toString());
    }

    @AfterAll
    public static void dropDB(){
        try {
            connection.createStatement().executeUpdate("drop table jc_hw5_test.organization cascade;" +
                    "drop table jc_hw5_test.product cascade;" +
                    "drop table jc_hw5_test.invoice cascade;" +
                    "drop table jc_hw5_test.invoice_positions cascade;");
        } catch (SQLException e) {
            System.out.println("Cleaning after test failed!!");
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}