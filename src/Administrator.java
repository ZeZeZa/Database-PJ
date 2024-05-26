import java.sql.Connection;
import java.sql.DriverManager;
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
                System.out.println("请输入需要操作的用户名称和身份(用空格分隔):");
                String input = scanner.nextLine();
                String[] parts = input.split(" ");
                String name = parts[0];
                String identity = parts[1];
                if (command == 1) {
                    insertCustomer(name, identity);
                } else if (command == 2) {
                    deleteCustomer(name, identity);
                }
            } else if (operation == 2) {
                System.out.println("请选择要执行的具体操作:(1:添加商户/2:删除商户/3:修改商户)");
                int command = scanner.nextInt();
                scanner.nextLine();
                System.out.println("请输入需要操作的商户名称:");
                String name = scanner.nextLine();
                if (command == 1) {
                    insertMerchant(name);
                } else if (command == 2) {
                    deleteMerchant(name);
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

    private void deleteCustomer(String name, String identity) {
        String sql = "DELETE FROM customer WHERE name = '" + name + "' AND identity = '" + identity + "'";
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected > 0) {
            } else {
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

    private void deleteMerchant(String name) {
        String sql = "DELETE FROM merchant WHERE name = '" + name + "'";
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected > 0) {
            } else {
                System.out.println("找不到该商户");
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("连接数据库时发生错误！");
            e.printStackTrace();
        }
    }
}
