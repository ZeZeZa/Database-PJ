import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomQueries {

    
    private  Connection connection;
    private Statement statement;
    CustomQueries(String password) {
            try {
                String url = "jdbc:mysql://localhost:3306/pj";
                String username = "root";
                connection = DriverManager.getConnection(url, username, password);
                statement = connection.createStatement();
            } catch (SQLException e) {
                System.err.println("查询模块初始化失败。");
                e.printStackTrace();
            }
    }
    public void queryOrders(int customer_id) {
        String sql="SELECT orders.id,time,dish.id,dish.name,count,online FROM orders,dish WHERE customer_id="+Integer.toString(customer_id)+" AND dish.id=orders.dish_id";
        try {
            ResultSet rs = statement.executeQuery(sql);
            if (!rs.isBeforeFirst()) {
                System.out.println("没有找到符合的记录");
            } else {
                System.out.println(" 订单ID\t"+ "时间\t\t\t"+ "菜品ID\t"+"菜品名称\t"+ "数量\t" +"在线\t");
                printResultSet(rs);   
            }
        } catch (SQLException e) {
            System.err.println("查询失败。");
            e.printStackTrace();
        } 
    }

    public void queryDishInMerchant(int input) {
        int merchantId = input;
        String sql = "SELECT * FROM dish WHERE merchant_id = '" + merchantId + "'";
        try {
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

            }
        } catch (SQLException e) {
            System.err.println("连接数据库时发生错误！");
            e.printStackTrace();
        }
    }
    public void queryDish(int input) {
        int dishId = input;
        String sql = "SELECT * FROM dish WHERE  id = '" + dishId + "'";
        try {

            ResultSet rs = statement.executeQuery(sql);
            if (!rs.isBeforeFirst()) {
                System.out.println("没有找到该菜品");
            } else {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String type = rs.getString("type");
                    String description = rs.getString("description");
                    String imageURL = rs.getString("imageURL");
                    String ingredient = rs.getString("ingredient");
                    String nutrient = rs.getString("nutrient");
                    String allergen = rs.getString("allergen");
                    int price = rs.getInt("price");
                    System.out.println("菜品ID: " + id + ", 名称: " + name + ", 类型：" + type + ", 价格：" + price
                            + "\n描述："
                            + description + "\n成分：" + ingredient + ", 营养信息：" + nutrient + ", 过敏源：" + allergen
                            + ", 图片地址：" + imageURL);
                }

            }
        } catch (SQLException e) {
            System.err.println("连接数据库时发生错误！");
            e.printStackTrace();
        }
    }
    public void queryMessage(int customer_id) {
        String sql="SELECT id,content,time FROM message WHERE customer_id="+Integer.toString(customer_id);
        try {
            ResultSet rs = statement.executeQuery(sql);
            if (!rs.isBeforeFirst()) {
                System.out.println("没有找到符合的记录");
            } else {
                System.out.println("消息ID\t"+" 内容\t\t\t\t\t\t\t\t" + "时间\t");
                printResultSet(rs);   
            }
        } catch (SQLException e) {
            System.err.println("查询失败。");
            e.printStackTrace();
        } 
    }
    public void queryFavoriteDishCount(int merchant_id) {
        String sql;
        if(merchant_id==0){
            sql="SELECT dish.id,name,COUNT(favorite_dish.dish_id) AS favorite_count FROM dish,favorite_dish WHERE favorite_dish.dish_id=dish.id GROUP BY dish.id";
        }else{
            sql="SELECT dish.id,name,COUNT(favorite_dish.dish_id) AS favorite_count FROM dish,favorite_dish WHERE favorite_dish.dish_id=dish.id AND dish.merchant_id="+Integer.toString(merchant_id)+" GROUP BY dish.id";
        }
        
        try {
            ResultSet rs = statement.executeQuery(sql);
            if (!rs.isBeforeFirst()) { 
                System.out.println("没有找到符合的记录");
            } else {
                System.out.println("菜品ID\t"+"菜品名称\t" + "收藏量\t");
                printResultSet(rs);   
            }
        } catch (SQLException e) {
            System.err.println("查询失败。");
            e.printStackTrace();
        } 
    }
    public void queryMerchants() {
        String sql="SELECT * FROM merchant";
        try {
            ResultSet rs = statement.executeQuery(sql);
            if (!rs.isBeforeFirst()) {
                System.out.println("没有找到符合的记录");
            } else {
                System.out.println("商户ID\t"+" 地址\t"+ "名称\t" );
                printResultSet(rs);   
            }
        } catch (SQLException e) {
            System.err.println("查询失败。");
            e.printStackTrace();
        } 
    }
    public void queryDishSales() {
        
        String sql="SELECT dish_id,name, SUM(online*count )AS online,SUM((online=False)*count ) AS offline FROM dish,orders WHERE dish.id=orders.dish_id GROUP BY dish_id";
        try {
            ResultSet rs = statement.executeQuery(sql);
            if (!rs.isBeforeFirst()) {
                System.out.println("没有找到符合的记录");
            } else {
                System.out.println("菜品ID\t"+"菜品名称\t"+ "在线销量\t"  + "离线销量\t");
                printResultSet(rs);   
            }
        } catch (SQLException e) {
            System.err.println("查询失败。");
            e.printStackTrace();
        } 
    }

    private static void printResultSet(ResultSet rs) throws SQLException {
        // 获取结果集的元数据
        java.sql.ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        // 打印数据行
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(rs.getObject(i) + "\t");
            }
            System.out.println();
        }
    }
}
