package dao;

import entity.Invoice;
import entity.InvoicePositions;
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

class InvoicePositionsDAOTest {

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
            dao = new InvoicePositionsDAO(connection, "jc_hw5_test");
        } catch (SQLException e) {
            System.out.println("Test preparation failed!!");
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static Connection connection;
    private static InvoicePositionsDAO dao;

    @Test
    void get() {
        assertEquals("InvoicePositions{id=1, invoice=Invoice{id=1, date=2019-01-01, organization=Organization{name='org1', bankAccount='null', inn=1}}, price=50, amount=20, product=Product{name='table', code=1}}", dao.get(1).toString());
    }

    @Test
    void all() {
        assertEquals(21, dao.all().size());
    }

    @Test
    void save() {
        var ip = new InvoicePositions( 101, new Invoice(1, new Date(119, 0,1), new Organization(1, "org1", null)), 10, 1, new Product(1, "table"));
        dao.save(ip);
        assertEquals("InvoicePositions{id=101, invoice=Invoice{id=1, date=2019-01-01, organization=Organization{name='org1', bankAccount='null', inn=1}}, price=10, amount=1, product=Product{name='table', code=1}}", dao.get(101).toString());
        dao.delete(ip);

    }

    @Test
    void update() {
        var ip = dao.get(3);
        var amount = ip.getAmount();
        ip.setAmount(0);
        dao.update(ip);
        assertEquals("InvoicePositions{id=3, invoice=Invoice{id=1, date=2019-01-01, organization=Organization{name='org1', bankAccount='null', inn=1}}, price=27, amount=0, product=Product{name='spoon', code=3}}", dao.get(3).toString());
        ip.setAmount(amount);
        dao.update(ip);
    }

    @Test
    void delete() {
        var ip = new InvoicePositions( 101, new Invoice(1, new Date(119, 0,1), new Organization(1, "org1", null)), 10, 1, new Product(1, "table"));
        dao.save(ip);
        assertEquals("InvoicePositions{id=101, invoice=Invoice{id=1, date=2019-01-01, organization=Organization{name='org1', bankAccount='null', inn=1}}, price=10, amount=1, product=Product{name='table', code=1}}", dao.get(101).toString());
        dao.delete(ip);
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