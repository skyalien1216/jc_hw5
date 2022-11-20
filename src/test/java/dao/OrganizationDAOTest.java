package dao;

import entity.Organization;
import org.flywaydb.core.Flyway;
import org.java_courses.CREDS;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrganizationDAOTest {

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

        dao = new OrganizationDAO("jc_hw5_test");
    }

    private static OrganizationDAO dao;

    @Test
    void get() {
        assertEquals("Organization{name='org1', bankAccount='null', inn=1}", dao.get(1).toString());
    }

    @Test
    void all() {
        assertEquals("[Organization{name='org1', bankAccount='null', inn=1}, Organization{name='org2', bankAccount='null', inn=2}, Organization{name='org3', bankAccount='null', inn=3}, Organization{name='org4', bankAccount='null', inn=4}, Organization{name='org5', bankAccount='null', inn=5}, Organization{name='org6', bankAccount='null', inn=6}, Organization{name='org7', bankAccount='null', inn=7}, Organization{name='org8', bankAccount='null', inn=8}, Organization{name='org9', bankAccount='null', inn=9}, Organization{name='org10', bankAccount='null', inn=10}, Organization{name='org11', bankAccount='null', inn=11}]", dao.all().toString());
    }


   @Test
    void save() {
        var o = new Organization(101, "test", null);
        dao.save(o);
        assertEquals("Organization{name='test', bankAccount='null', inn=101}",dao.get(101).toString());
        dao.delete(o);
    }

    @Test
    void update() {
        var o = new Organization(101, "test", null);
        dao.save(o);
        o.setName("test11");
        dao.update(o);
        assertEquals("Organization{name='test11', bankAccount='null', inn=101}",dao.get(101).toString());
        dao.delete(o);
    }

    @Test
    void delete() {
        var o = new Organization(101, "test", null);
        dao.save(o);
        assertEquals("Organization{name='test', bankAccount='null', inn=101}",dao.get(101).toString());
        dao.delete(o);
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