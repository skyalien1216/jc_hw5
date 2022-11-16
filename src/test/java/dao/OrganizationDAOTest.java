package dao;

import entity.Organization;
import org.flywaydb.core.Flyway;
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
            dao = new OrganizationDAO(connection, "jc_hw5_test");
        } catch (SQLException e) {
            System.out.println("Test preparation failed!!");
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static Connection connection;
    private static OrganizationDAO dao;

    @Test
    void get() {
        assertEquals("Organization{name='org1', bankAccount='null', inn=1}", dao.get(1).toString());
    }

    @Test
    void all() {
        assertEquals(11, dao.all().size());
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
        var o = dao.get(1);
        var name = o.getName();
        o.setName("test");
        dao.update(o);
        assertEquals("Organization{name='test', bankAccount='null', inn=1}",dao.get(1).toString());
        o.setName(name);
        dao.update(o);
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