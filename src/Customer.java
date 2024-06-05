import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Customer {
    public static String url = "jdbc:mysql://localhost:3306/pj";
    public static String username = "root";
    public static String password = "gyh040506";
    public static Connection connection;
    public static Statement statement;
    public static PreparedStatement preparedStatement;
    private int id;
    private String name;
    private String identity;
    private Scanner scanner;

    public void customer() {
        scanner = new Scanner(System.in);
        if (logIn()) {
            while (true) {
                System.out.println("请选择要执行的操作(1:查看个人信息/2:搜索商户/3:退出):");
                int operation = scanner.nextInt();
                scanner.nextLine();
                if (operation == 1) {
                    printCustomerInfo();
                } else if (operation == 2) {
                    searchMerchantsByKeyword();
                    System.out.println("输入商户id以查看该商户的完整菜单,输入非数字以回到上一页");
                    String input = scanner.nextLine();
                    if (isInteger(input)) {
                        int merchantId = Integer.parseInt(input);
                        searchSpecificMerchant(merchantId);
                    } else {
                    }
                } else if (operation == 3) {
                    break;
                }
            }
        }
    }

    private boolean logIn() {
        scanner = new Scanner(System.in);
        while (true) {
            System.out.println("请输入您的名字与身份(用空格分隔):");
            String input = scanner.nextLine();
            String[] parts = input.split(" ");
            String name = parts[0];
            String identity = parts[1];
            String sql = "SELECT * FROM customer WHERE name = '" + name + "' AND identity = '" + identity + "'";
            try {
                connection = DriverManager.getConnection(url, username, password);
                statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                if (rs.next()) {
                    int id = rs.getInt("id");
                    this.id = id;
                    this.name = name;
                    this.identity = identity;
                    statement.close();
                    connection.close();
                    return true;
                } else {
                    System.out.println("名字或身份错误！");
                }
            } catch (SQLException e) {
                System.err.println("连接数据库时发生错误！");
                e.printStackTrace();
            }
        }
    }

    private void printCustomerInfo() {
        System.out.println("用户ID: " + id + ", 名称: " + name + ", 身份: " + identity);
    }

    private void searchMerchantsByKeyword() {
        scanner = new Scanner(System.in);
        while (true) {
            System.out.println("请输入关键字:");
            String keyword = scanner.nextLine();
            String sql = "SELECT * FROM merchant WHERE name LIKE '%" + keyword + "%'";
            try {
                connection = DriverManager.getConnection(url, username, password);
                statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                if (!rs.isBeforeFirst()) {
                    System.out.println("没有找到符合的商户");
                } else {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        System.out.println("商户ID: " + id + ", 名称: " + name);
                    }
                    statement.close();
                    connection.close();
                    break;
                }
            } catch (SQLException e) {
                System.err.println("连接数据库时发生错误！");
                e.printStackTrace();
            }
        }
    }

    private void searchSpecificMerchant(int merchantId) {
        String sql = "SELECT * FROM dish WHERE merchant_id = '" + merchantId + "'";
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            if (!rs.isBeforeFirst()) {
                System.out.println("没有找到该商户");
            } else {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String type = rs.getString("type");
                    int price = rs.getInt("price");
                    System.out.println("菜品ID: " + id + ", 名称: " + name + ", 类型：" + type + ", 价格：" + price);
                }
                statement.close();
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("连接数据库时发生错误！");
            e.printStackTrace();
        }
    }

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
