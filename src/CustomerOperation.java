import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerOperation {
    public static String url = "jdbc:mysql://localhost:3306/pj";
    public static String username = "root";
    public static String password = "gyh040506";
    public static Connection connection;
    public static Statement statement;

    public static void insertCustomer(String name, String identity) {
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

    public static void deleteCustomer(String name, String identity) {
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
}
