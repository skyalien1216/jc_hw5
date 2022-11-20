package dao;

import entity.Invoice;
import entity.Organization;
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

class InvoiceDAOTest {

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

        dao = new InvoiceDAO("jc_hw5_test");
    }

    private static InvoiceDAO dao;

    @Test
    void get() {
        assertEquals("Invoice{id=1, date=2019-01-01, organization=Organization{name='org1', bankAccount='null', inn=1}}", dao.get(1).toString());
    }

    @Test
    void all() {
        assertEquals("[Invoice{id=1, date=2019-01-01, organization=Organization{name='org1', bankAccount='null', inn=1}}, Invoice{id=2, date=2019-01-02, organization=Organization{name='org1', bankAccount='null', inn=1}}, Invoice{id=3, date=2019-01-01, organization=Organization{name='org2', bankAccount='null', inn=2}}, Invoice{id=4, date=2019-01-02, organization=Organization{name='org2', bankAccount='null', inn=2}}, Invoice{id=5, date=2019-01-03, organization=Organization{name='org2', bankAccount='null', inn=2}}, Invoice{id=6, date=2019-01-01, organization=Organization{name='org3', bankAccount='null', inn=3}}, Invoice{id=7, date=2019-01-03, organization=Organization{name='org3', bankAccount='null', inn=3}}, Invoice{id=8, date=2019-01-02, organization=Organization{name='org4', bankAccount='null', inn=4}}, Invoice{id=9, date=2019-01-03, organization=Organization{name='org4', bankAccount='null', inn=4}}, Invoice{id=10, date=2019-01-01, organization=Organization{name='org5', bankAccount='null', inn=5}}, Invoice{id=11, date=2019-01-02, organization=Organization{name='org5', bankAccount='null', inn=5}}, Invoice{id=12, date=2019-01-03, organization=Organization{name='org5', bankAccount='null', inn=5}}, Invoice{id=13, date=2019-01-01, organization=Organization{name='org6', bankAccount='null', inn=6}}, Invoice{id=14, date=2019-01-02, organization=Organization{name='org6', bankAccount='null', inn=6}}, Invoice{id=15, date=2019-01-03, organization=Organization{name='org6', bankAccount='null', inn=6}}]", dao.all().toString());
    }

    @Test
    void save() {
        dao.save(new Invoice(101, new Date(119, 1,1), new Organization(1, "org1", null)));
        assertEquals("Invoice{id=101, date=2019-02-01, organization=Organization{name='org1', bankAccount='null', inn=1}}",dao.get(101).toString() );
        dao.delete(new Invoice(101, new Date(119, 1,1), new Organization(1, "org1", null)));

    }

    @Test
    void update() {
        dao.save(new Invoice(101, new Date(119, 1,1), new Organization(1, "org1", null)));
        dao.update(new Invoice(101, new Date(119, 1,1), new Organization(3, "org3", null)));
        assertEquals("Invoice{id=101, date=2019-02-01, organization=Organization{name='org3', bankAccount='null', inn=3}}",dao.get(101).toString());
        dao.delete(new Invoice(101, new Date(119, 1,1), new Organization(3, "org3", null)));
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
       try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + CREDS.dbName, CREDS.user, CREDS.password)) {
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