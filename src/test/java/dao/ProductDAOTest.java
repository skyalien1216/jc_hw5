package dao;

import entity.Invoice;
import entity.Organization;
import entity.Product;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductDAOTest {

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
            dao = new ProductDAO(connection, "jc_hw5_test");
        } catch (SQLException e) {
            System.out.println("Test preparation failed!!");
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static Connection connection;
    private static ProductDAO dao;

    @Test
    void get() {
        assertEquals("Product{name='table', code=1}", dao.get(1).toString());
    }

    @Test
    void all() {
        assertEquals(3, dao.all().size());
    }

   @Test
    void save() {
        var p = new Product(101, "test");
        dao.save(p);
        assertEquals("Product{name='test', code=101}",dao.get(101).toString());
        dao.delete(p);
    }

    @Test
    void update() {
        var p = dao.get(1);
        var name = p.getName();
        p.setName("test");
        dao.update(p);
        assertEquals("Product{name='test', code=1}",dao.get(1).toString());
        p.setName(name);
        dao.update(p);
    }

    @Test
    void delete() {
    var p = new Product(101, "test");
        dao.save(p);
        assertEquals("Product{name='test', code=101}",dao.get(101).toString());
        dao.delete(p);
        assertThrows(IllegalStateException.class , ()-> {dao.get(101);});
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