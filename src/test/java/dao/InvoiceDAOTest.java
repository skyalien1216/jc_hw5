package dao;

import entity.Invoice;
import entity.Organization;
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

class InvoiceDAOTest {

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
            dao = new InvoiceDAO(connection, "jc_hw5_test");
        } catch (SQLException e) {
            System.out.println("Test preparation failed!!");
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static Connection connection;
    private static InvoiceDAO dao;

    @Test
    void get() {
        assertEquals("Invoice{id=1, date=2019-01-01, organization=Organization{name='org1', bankAccount='null', inn=1}}", dao.get(1).toString());
    }

    @Test
    void all() {
        assertEquals(15, dao.all().size());
    }

    @Test
    void save() {
        dao.save(new Invoice(101, new Date(119, 1,1), new Organization(1, "org1", null)));
        assertEquals("Invoice{id=101, date=2019-02-01, organization=Organization{name='org1', bankAccount='null', inn=1}}",dao.get(101).toString() );
        dao.delete(new Invoice(101, new Date(119, 1,1), new Organization(1, "org1", null)));

    }

    @Test
    void update() {
        dao.update(new Invoice(3, new Date(119, 1,1), new Organization(3, "org3", null)));
        assertEquals("Invoice{id=3, date=2019-02-01, organization=Organization{name='org3', bankAccount='null', inn=3}}",dao.get(3).toString());
        dao.update(new Invoice(3, new Date(119, 0,1), new Organization(2, "org2", null)));
    }

    @Test
    void delete() {
        dao.save(new Invoice(101, new Date(119, 0,2), new Organization(1, "org1", null)));
        assertEquals("Invoice{id=101, date=2019-01-02, organization=Organization{name='org1', bankAccount='null', inn=1}}",dao.get(101).toString());
        dao.delete(new Invoice(101, new Date(119, 0,2), new Organization(1, "org1", null)));
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