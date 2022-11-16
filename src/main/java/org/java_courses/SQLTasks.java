package org.java_courses;

import entity.Organization;
import entity.Product;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SQLTasks {
    private final @NotNull Connection connection;
    private final String schema;

    public SQLTasks(@NotNull Connection connection, String schema) {
        this.connection = connection;
        this.schema = schema;
    }

    public List<Pair<Organization, Integer>> task1()
    {
        String sql = "select o.inn, ii.amount, name, bank_account" +
                " from "+ schema +".organization o join " +
                "(select organization_id as inn , sum(amount) as amount from "+ schema +".invoice i " +
                "join "+ schema +".invoice_positions ip on i.id = ip.invoice_id group by organization_id) ii " +
                "on ii.inn = o.inn order by ii.amount desc limit 10;";
        final List<Pair<Organization, Integer>> result = new ArrayList<>();
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    var amount = resultSet.getInt("amount");
                    var inn = resultSet.getInt("inn");
                    var name = resultSet.getString("name");
                    var bAcc = resultSet.getString("bank_account");
                    result.add(new Pair<>(new Organization(inn,name,bAcc), amount));
                }
                return result;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("SQLTASKS::1");
        }
    }

    public List<Organization> task2(List<Pair<Product,Integer>> parameters)
    {
        final List<Organization> result = new ArrayList<>();
        if (parameters.isEmpty())
            return result;
        String sqlStart = "select inn, name, bank_account " +
                "from (select organization_id, sum(amount) as amount, product_id  " +
                " from "+ schema +".invoice i join "+ schema +".invoice_positions ip on i.id = ip.invoice_id " +
                " where  ";
        var sqlEnd = "group by organization_id, product_id) ii join "+ schema +".organization o on organization_id = o.inn";
        String sqlMiddle = "ip.product_id = " + parameters.get(0).getKey().getCode()
                + " and amount > " + parameters.get(0).getValue() + " ";
        StringBuilder sql = new StringBuilder(sqlStart + sqlMiddle + sqlEnd);
        parameters.remove(0);
        if (!parameters.isEmpty()){
            for (var p: parameters) {
                sql.append(" intersect ");
                sqlMiddle = "ip.product_id = " + p.getKey().getCode()
                        + " and amount > " + p.getValue() + " ";
                sql.append(sqlStart).append(sqlMiddle).append(sqlEnd);
            }
        }
        sql.append(";");
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(sql.toString())) {
                while (resultSet.next()) {
                    var inn = resultSet.getInt("inn");
                    var name = resultSet.getString("name");
                    var bAcc = resultSet.getString("bank_account");
                    result.add(new Organization(inn,name,bAcc));
                }
                return result;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("SQLTASKS::2");
        }
    }

    public Pair<List<Task3Data>, List<Task3Data>> task3(Date startDate, Date endDate){
        var sql ="select product_id, name, date, sum(price*amount), sum(amount) as amount " +
                "from "+ schema +".invoice i join "+ schema +".invoice_positions ip on i.id = ip.invoice_id " +
                "join "+ schema +".product p on p.code = product_id " +
                "where date > ? and date < ? group by product_id, date, name ;";

        final List<Task3Data> result = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);
            try (var resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()) {
                    var prId = resultSet.getInt("product_id");
                    var date = resultSet.getDate("date");
                    var sum = resultSet.getInt("sum");
                    var amount = resultSet.getInt("amount");
                    var name = resultSet.getString("name");
                    result.add(new Task3Data(new Product(prId, name), date, sum, amount));
                }
                return new Pair<>(result, getFinalResForPeriod(startDate, endDate));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("SQLTASKS::3");
        }
    }

    private List<Task3Data> getFinalResForPeriod(Date startDate, Date endDate) {
        var sql ="select product_id, name, sum(price*amount), sum(amount) as amount " +
                "from "+ schema +".invoice i join "+ schema +".invoice_positions ip on i.id = ip.invoice_id " +
                "join "+ schema +".product p on p.code = product_id " +
                "where date > ? and date < ? group by product_id, name ;";

        final var result = new ArrayList<Task3Data>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);
            try (var resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()) {
                    var prId = resultSet.getInt("product_id");
                    var sum = resultSet.getInt("sum");
                    var amount = resultSet.getInt("amount");
                    var name = resultSet.getString("name");
                    result.add(new Task3Data(new Product(prId, name), null, sum, amount));
                }
                return result;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("SQLTASKS::3_additional_func");
        }
    }

    public List<Pair<Product, Double>> task4(Date startDate, Date endDate){
        var sql ="select product_id, name, sum(price)/count(price) as avg " +
                "from "+ schema +".invoice i join "+ schema +".invoice_positions ip on i.id = ip.invoice_id" +
                " join "+ schema +".product p on p.code = product_id" +
                " where date > ? and date < ? group by product_id, name;";

        final var result = new ArrayList<Pair<Product,Double>>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);
            try (var resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()) {
                    var prId = resultSet.getInt("product_id");
                    var avg = resultSet.getDouble("avg");
                    var name = resultSet.getString("name");
                    result.add(new Pair<Product, Double>(new Product(prId,name), avg));
                }
                return result;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("SQLTASKS::4");
        }
    }

    public Map<Organization, List<Product>> task5(Date startDate, Date endDate){
        var sql ="select inn, o.name as org_name, bank_account, code, p.name as pr_name" +
                " from (select organization_id, product_id from "+ schema +".invoice i" +
                " join "+ schema +".invoice_positions ip on i.id = ip.invoice_id " +
                "where date > ? and date < ? group by organization_id, product_id) op" +
                " join "+ schema +".product p on op.product_id = p.code right join "+ schema +".organization o" +
                " on op.organization_id = o.inn order by inn;";

        final var result = new HashMap<Organization, List<Product>>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);
            try (var resultSet = preparedStatement.executeQuery()){
                var seen = new ArrayList<Integer>();
                while (resultSet.next()) {
                    var code = resultSet.getInt("code");
                    var inn = resultSet.getInt("inn");
                    var orgName = resultSet.getString("org_name");
                    var pName = resultSet.getString("pr_name");
                    var bAcc = resultSet.getString("bank_account");
                    var product = new Product(code, pName);
                    var org = new Organization(inn, orgName,bAcc);
                    List<Product> list;
                    if (seen.contains(inn)){
                        var key = result.keySet().stream().filter(x -> x.getInn() == inn).findFirst().orElse(null);
                        list = result.get(key);
                        result.remove(key);
                    } else {
                        seen.add(inn);
                        list = new ArrayList<Product>();
                    }
                    list.add(product);
                    result.put(org, list);
                }
                return result;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("SQLTASKS::5");
        }
    }
}
