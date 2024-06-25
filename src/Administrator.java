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
                    System.out.println("请输入需要插入的用户名称和密码(用逗号分隔):");
                    String input = scanner.nextLine();
                    String[] parts = input.split(",");
                    String name = parts[0];
                    String pw = parts[1];
                    insertCustomer(name, pw);
                } else if (command == 2) {
                    showCustomers();
                    System.out.println("请输入需要删除的用户ID:");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    deleteCustomer(id);
                } else if (command == 3) {
                    showCustomers();
                    updateCustomer();
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
                    System.out.println("请输入需要删除的商户ID:");
                    int id = scanner.nextInt();
                    deleteMerchant(id);
                    scanner.nextLine();
                } else if (command == 3) {
                    showMerchants();
                    updateMerchant();
                }
            } else if (operation == 3) {
                break;
            }
        }
    }

    private void insertCustomer(String name, String pw) {
        String sql = "INSERT INTO customer (name, password) VALUES ('"
                + name + "', '" + pw + "')";
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
        String deleteMessgae = "DELETE FROM message WHERE customer_id = '" + id + "'";
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
            statement.executeUpdate(deleteMessgae);
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

    private void updateCustomer() {
        System.out.println("请输入要修改的商户ID:");
        int customerId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("请输入要修改的属性 (name, identity, password):");
        String attribute = scanner.nextLine();
        System.out.println("要修改成什么?");
        String newValue = scanner.nextLine();
        String sql = "UPDATE customer SET " + attribute + " = '" + newValue + "' WHERE id = " + customerId;
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected > 0) {
                System.out.println("用户信息更新成功");
            } else {
                System.out.println("找不到该用户");
            }
        } catch (SQLException e) {
            System.err.println("更新用户信息时发生错误！");
            e.printStackTrace();
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        String deleteDR = "DELETE FROM dish_review WHERE dish_id IN (SELECT id FROM dish WHERE merchant_id = '" + id
                + "')";
        String deleteFD = "DELETE FROM favorite_dish WHERE dish_id IN (SELECT id FROM dish WHERE merchant_id = '" + id
                + "')";
        String deleteOrder = "DELETE FROM orders WHERE dish_id IN (SELECT id FROM dish WHERE merchant_id = '" + id
                + "')";
        String deleteDish = "DELETE FROM dish WHERE merchant_id = '" + id + "'";
        String deleteFM = "DELETE FROM favorite_merchant WHERE merchant_id = '" + id + "'";
        String deleteMR = "DELETE FROM merchant_review WHERE merchant_id = '" + id + "'";
        String sql = "DELETE FROM merchant WHERE id = '" + id + "'";
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            connection.setAutoCommit(false);
            statement.executeUpdate(deleteDR);
            statement.executeUpdate(deleteFD);
            statement.executeUpdate(deleteOrder);
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

    private void updateMerchant() {
        System.out.println("请输入要修改的商户ID:");
        int merchantId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("请输入要修改的属性 (name, address):");
        String attribute = scanner.nextLine();
        System.out.println("要修改成什么?");
        String newValue = scanner.nextLine();
        String sql = "UPDATE merchant SET " + attribute + " = '" + newValue + "' WHERE id = " + merchantId;
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected > 0) {
                System.out.println("商户信息更新成功");
            } else {
                System.out.println("找不到该商户");
            }
        } catch (SQLException e) {
            System.err.println("更新商户信息时发生错误！");
            e.printStackTrace();
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
