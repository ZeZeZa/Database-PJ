import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Administrator {
    public static String url = "jdbc:mysql://localhost:3306/pj";
    public static String username = "root";
    public static String password = "gyh040506";
    public static Connection connection;
    public static Statement statement;
    private Scanner scanner;

    public void administrator() {
        scanner = new Scanner(System.in);
        while (true) {
            System.out.println("请选择要执行的操作:(1:用户操作/2:商户操作/3:退出)");
            int operation = scanner.nextInt();
            if (operation == 1) {
                System.out.println("请选择要执行的具体操作:(1:添加用户/2:删除用户/3:修改用户)");
                int command = scanner.nextInt();
                scanner.nextLine();
                if (command == 1) {
                    System.out.println("请输入需要插入的用户名称和身份(用空格分隔):");
                    String input = scanner.nextLine();
                    String[] parts = input.split(" ");
                    String name = parts[0];
                    String identity = parts[1];
                    insertCustomer(name, identity);
                } else if (command == 2) {
                    showCustomers();
                    System.out.println("请输入需要删除的用户id:");
                    int id = scanner.nextInt();
                    deleteCustomer(id);
                    scanner.nextLine();
                }
            } else if (operation == 2) {
                System.out.println("请选择要执行的具体操作:(1:添加商户/2:删除商户/3:修改商户)");
                int command = scanner.nextInt();
                scanner.nextLine();
                if (command == 1) {
                    System.out.println("请输入需要插入的商户名称:");
                    String name = scanner.nextLine();
                    insertMerchant(name);
                } else if (command == 2) {
                    showMerchants();
                    System.out.println("请输入需要删除的商户id:");
                    int id = scanner.nextInt();
                    deleteMerchant(id);
                    scanner.nextLine();
                }
            } else if (operation == 3) {
                break;
            }
        }
    }

    private void insertCustomer(String name, String identity) {
        String sql = "INSERT INTO customer (name, identity) VALUES ('"
                + name + "', '" + identity + "')";
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("连接数据库时发生错误！");
            e.printStackTrace();
        }
    }

    private void deleteCustomer(int id) {
        String deleteFM = "DELETE FROM favorite_merchant WHERE customer_id = '" + id + "'";
        String deleteFD = "DELETE FROM favorite_dish WHERE customer_id = '" + id + "'";
        String deleteMR = "DELETE FROM merchant_review WHERE customer_id = '" + id + "'";
        String deleteDR = "DELETE FROM dish_review WHERE customer_id = '" + id + "'";
        String deleteOrder = "DELETE FROM orders WHERE customer_id = '" + id + "'";
        String sql = "DELETE FROM customer WHERE id = '" + id + "'";
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            connection.setAutoCommit(false);
            statement.executeUpdate(deleteFM);
            statement.executeUpdate(deleteFD);
            statement.executeUpdate(deleteMR);
            statement.executeUpdate(deleteDR);
            statement.executeUpdate(deleteOrder);
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected > 0) {
                connection.commit();
                System.out.println("用户及其记录删除成功");
            } else {
                connection.rollback();
                System.out.println("找不到该用户");
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("连接数据库时发生错误！");
            e.printStackTrace();
        }
    }

    private void insertMerchant(String name) {
        String sql = "INSERT INTO merchant (name) VALUES ('" + name + "')";
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("连接数据库时发生错误！");
            e.printStackTrace();
        }
    }

    private void deleteMerchant(int id) {
        String deleteDish = "DELETE FROM dish WHERE merchant_id = '" + id + "'";
        String deleteFM = "DELETE FROM favorite_merchant WHERE merchant_id = '" + id + "'";
        String deleteMR = "DELETE FROM merchant_review WHERE merchant_id = '" + id + "'";
        String sql = "DELETE FROM merchant WHERE id = '" + id + "'";
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            connection.setAutoCommit(false);
            statement.executeUpdate(deleteDish);
            statement.executeUpdate(deleteFM);
            statement.executeUpdate(deleteMR);
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected > 0) {
                connection.commit();
                System.out.println("商户及其记录删除成功");

            } else {
                connection.rollback();
                System.out.println("找不到该商户");
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("连接数据库时发生错误！");
            e.printStackTrace();
        }
    }

    private void showCustomers() {
        String sql = "SELECT * FROM customer";
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String identity = rs.getString("identity");
                System.out.println("用户ID: " + id + ", 名字: " + name + ", 身份: " + identity);
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("连接数据库时发生错误！");
            e.printStackTrace();
        }
    }

    private void showMerchants() {
        String sql = "SELECT * FROM merchant";
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                System.out.println("商户ID: " + id + ", 名称: " + name);
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("连接数据库时发生错误！");
            e.printStackTrace();
        }
    }
}
