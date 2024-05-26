import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Customer {
    public static String url = "jdbc:mysql://localhost:3306/pj";
    public static String username = "root";
    public static String password = "gyh040506";
    public static Connection connection;
    public static Statement statement;
    private int id;
    private String name;
    private String identity;
    private Scanner scanner;

    public void customer() {
        scanner = new Scanner(System.in);
        if (logIn()) {
            while (true) {
                System.out.println("请选择要执行的操作:(1:查看个人信息/2:搜索商户/3:退出)");
                int operation = scanner.nextInt();
                if (operation == 1) {
                    printCustomerInfo();
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
}
