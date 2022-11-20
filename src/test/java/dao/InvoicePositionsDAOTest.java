package dao;

import entity.Invoice;
import entity.InvoicePositions;
import entity.Organization;
import entity.Product;
import org.flywaydb.core.Flyway;
import org.java_courses.CREDS;
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
        final Flyway flyway = Flyway
                .configure().cleanDisabled(false)
                .dataSource("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)
                .locations("testdb")
                .load();
        flyway.clean();
        flyway.migrate();
        System.out.println("Migrations applied successfully");

        dao = new InvoicePositionsDAO("jc_hw5_test");
    }

    private static InvoicePositionsDAO dao;

    @Test
    void get() {
        assertEquals("InvoicePositions{id=1, invoice=Invoice{id=1, date=2019-01-01, organization=Organization{name='org1', bankAccount='null', inn=1}}, price=50, amount=20, product=Product{name='table', code=1}}", dao.get(1).toString());
    }

    @Test
    void all() {
        assertEquals("[InvoicePositions{id=1, invoice=Invoice{id=1, date=2019-01-01, organization=Organization{name='org1', bankAccount='null', inn=1}}, price=50, amount=20, product=Product{name='table', code=1}}, InvoicePositions{id=2, invoice=Invoice{id=1, date=2019-01-01, organization=Organization{name='org1', bankAccount='null', inn=1}}, price=30, amount=3, product=Product{name='chair', code=2}}, InvoicePositions{id=3, invoice=Invoice{id=1, date=2019-01-01, organization=Organization{name='org1', bankAccount='null', inn=1}}, price=27, amount=5, product=Product{name='spoon', code=3}}, InvoicePositions{id=4, invoice=Invoice{id=2, date=2019-01-02, organization=Organization{name='org1', bankAccount='null', inn=1}}, price=20, amount=12, product=Product{name='chair', code=2}}, InvoicePositions{id=5, invoice=Invoice{id=2, date=2019-01-02, organization=Organization{name='org1', bankAccount='null', inn=1}}, price=20, amount=6, product=Product{name='spoon', code=3}}, InvoicePositions{id=6, invoice=Invoice{id=3, date=2019-01-01, organization=Organization{name='org2', bankAccount='null', inn=2}}, price=45, amount=15, product=Product{name='table', code=1}}, InvoicePositions{id=7, invoice=Invoice{id=3, date=2019-01-01, organization=Organization{name='org2', bankAccount='null', inn=2}}, price=25, amount=10, product=Product{name='chair', code=2}}, InvoicePositions{id=8, invoice=Invoice{id=4, date=2019-01-02, organization=Organization{name='org2', bankAccount='null', inn=2}}, price=47, amount=10, product=Product{name='spoon', code=3}}, InvoicePositions{id=9, invoice=Invoice{id=5, date=2019-01-03, organization=Organization{name='org2', bankAccount='null', inn=2}}, price=55, amount=5, product=Product{name='table', code=1}}, InvoicePositions{id=10, invoice=Invoice{id=6, date=2019-01-01, organization=Organization{name='org3', bankAccount='null', inn=3}}, price=40, amount=10, product=Product{name='chair', code=2}}, InvoicePositions{id=11, invoice=Invoice{id=7, date=2019-01-03, organization=Organization{name='org3', bankAccount='null', inn=3}}, price=35, amount=7, product=Product{name='chair', code=2}}, InvoicePositions{id=12, invoice=Invoice{id=8, date=2019-01-02, organization=Organization{name='org4', bankAccount='null', inn=4}}, price=70, amount=2, product=Product{name='table', code=1}}, InvoicePositions{id=13, invoice=Invoice{id=9, date=2019-01-03, organization=Organization{name='org4', bankAccount='null', inn=4}}, price=65, amount=5, product=Product{name='table', code=1}}, InvoicePositions{id=14, invoice=Invoice{id=10, date=2019-01-01, organization=Organization{name='org5', bankAccount='null', inn=5}}, price=60, amount=10, product=Product{name='table', code=1}}, InvoicePositions{id=15, invoice=Invoice{id=11, date=2019-01-02, organization=Organization{name='org5', bankAccount='null', inn=5}}, price=45, amount=10, product=Product{name='chair', code=2}}, InvoicePositions{id=16, invoice=Invoice{id=12, date=2019-01-03, organization=Organization{name='org5', bankAccount='null', inn=5}}, price=20, amount=20, product=Product{name='spoon', code=3}}, InvoicePositions{id=17, invoice=Invoice{id=13, date=2019-01-01, organization=Organization{name='org6', bankAccount='null', inn=6}}, price=20, amount=30, product=Product{name='chair', code=2}}, InvoicePositions{id=18, invoice=Invoice{id=13, date=2019-01-01, organization=Organization{name='org6', bankAccount='null', inn=6}}, price=20, amount=10, product=Product{name='spoon', code=3}}, InvoicePositions{id=19, invoice=Invoice{id=14, date=2019-01-02, organization=Organization{name='org6', bankAccount='null', inn=6}}, price=15, amount=40, product=Product{name='chair', code=2}}, InvoicePositions{id=20, invoice=Invoice{id=14, date=2019-01-02, organization=Organization{name='org6', bankAccount='null', inn=6}}, price=20, amount=10, product=Product{name='table', code=1}}, InvoicePositions{id=21, invoice=Invoice{id=15, date=2019-01-03, organization=Organization{name='org6', bankAccount='null', inn=6}}, price=30, amount=5, product=Product{name='spoon', code=3}}]", dao.all().toString());
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
        var ip = new InvoicePositions( 101, new Invoice(1, new Date(119, 0,1), new Organization(1, "org1", null)), 10, 1, new Product(1, "table"));
        dao.save(ip);
        ip.setAmount(20);
        dao.update(ip);
        assertEquals("InvoicePositions{id=101, invoice=Invoice{id=1, date=2019-01-01, organization=Organization{name='org1', bankAccount='null', inn=1}}, price=10, amount=20, product=Product{name='table', code=1}}", dao.get(101).toString());
        dao.delete(ip);
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
       try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)){
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