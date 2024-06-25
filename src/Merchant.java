import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Merchant {
    public static String url = "jdbc:mysql://localhost:3306/pj";
    public static String username = "root";
    public static String password = "gyh040506";
    public static Connection connection;
    public static Statement statement;
    private int id;
    private String name;
    private Scanner scanner;

    public void merchant() {
        if (logIn()) {
            while (true) {
                System.out.println("请选择要执行的操作:(1:查看本商户信息/2:菜单操作/3:退出)");
                int operation = scanner.nextInt();
                if (operation == 1) {
                    printMerchantInfo();
                } else if (operation == 2) {
                    System.out.println("请选择要执行的具体操作:(1:添加菜品/2:删除菜品/3:修改菜品信息/4:退出)");
                    int command = scanner.nextInt();
                    scanner.nextLine();
                    if (command == 1) {
                        System.out.println("请输入菜品名称、类型和价格(用空格分隔):");
                        String input = scanner.nextLine();
                        String[] parts = input.split(" ");
                        String name = parts[0];
                        String type = parts[1];
                        int price = Integer.parseInt(parts[2]);
                        insertDish(name, type, price);
                    } else if (command == 2) {
                        System.out.println("请输入菜品名称:");
                        String name = scanner.nextLine();
                        deleteDish(name);
                    } else if (command == 3) {
                        updateDish();
                    } else if (command == 4) {
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
            System.out.println("请输入您的商户名称:");
            String name = scanner.nextLine();
            String sql = "SELECT * FROM merchant WHERE name = '" + name + "'";
            try {
                connection = DriverManager.getConnection(url, username, password);
                statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                if (rs.next()) {
                    int id = rs.getInt("id");
                    this.id = id;
                    this.name = name;
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

    private void printMerchantInfo() {
        System.out.println("商户ID: " + id + ", 名称: " + name);
    }

    private void insertDish(String name, String type, int price) {
        String sql = "INSERT INTO dish (name, type, price, merchant_id) VALUES ('" + name + "','" + type + "','" + price
                + "','" + id + "')";
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

    private void deleteDish(String name) {
        String sql = "DELETE FROM dish WHERE name = '" + name + "'";
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected > 0) {
            } else {
                System.out.println("找不到该菜品");
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("连接数据库时发生错误！");
            e.printStackTrace();
        }
    }

    private void updateDish() {
        System.out.println("请输入要修改的菜品ID:");
        int dishId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("请输入要修改的属性:");
        String attribute = scanner.nextLine();
        System.out.println("要修改成什么?");
        String newValue = scanner.nextLine();
        if (attribute.equalsIgnoreCase("price")) {
            newValue = newValue.replace(",", ".");
            String sql = "UPDATE dish SET " + attribute + " = " + newValue + " WHERE id = " + dishId;
            try {
                connection = DriverManager.getConnection(url, username, password);
                statement = connection.createStatement();
                int rowsAffected = statement.executeUpdate(sql);
                if (rowsAffected > 0) {
                    System.out.println("菜品信息更新成功");
                } else {
                    System.out.println("找不到该菜品");
                }
            } catch (SQLException e) {
                System.err.println("更新菜品信息时发生错误！");
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
        } else {
            String sql = "UPDATE dish SET " + attribute + " = '" + newValue + "' WHERE id = " + dishId;
            try {
                connection = DriverManager.getConnection(url, username, password);
                statement = connection.createStatement();
                int rowsAffected = statement.executeUpdate(sql);
                if (rowsAffected > 0) {
                    System.out.println("菜品信息更新成功");
                } else {
                    System.out.println("找不到该菜品");
                }
            } catch (SQLException e) {
                System.err.println("更新菜品信息时发生错误！");
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
    }
}
