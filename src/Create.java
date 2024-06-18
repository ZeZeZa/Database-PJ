import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Create {
        public static String url = "jdbc:mysql://localhost:3306/pj";
        public static String username = "root";
        public static String password = "[111111]";
        public static Connection connection;
        public static Statement statement;

        public static void createTables() {
                String createCustomer = "CREATE TABLE IF NOT EXISTS customer ("
                                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                                + "name VARCHAR(255) NOT NULL, "
                                + "identity VARCHAR(255) NOT NULL)";

                String createMerchant = "CREATE TABLE IF NOT EXISTS merchant ("
                                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                                + "name VARCHAR(255) NOT NULL)";

                String createDish = "CREATE TABLE IF NOT EXISTS dish ("
                                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                                + "name VARCHAR(255) NOT NULL, "
                                + "type VARCHAR(255) NOT NULL, "
                                + "description VARCHAR(255) DEFAULT '', "
                                + "price DECIMAL(10, 2) NOT NULL, "
                                + "imageURL VARCHAR(255) DEFAULT '', "
                                + "ingredient VARCHAR(255) DEFAULT '', "
                                + "nutrient VARCHAR(255) DEFAULT '', "
                                + "allergen VARCHAR(255) DEFAULT '', "
                                + "merchant_id INT, "
                                + "FOREIGN KEY (merchant_id) REFERENCES merchant(id))";

                String createOrder = "CREATE TABLE IF NOT EXISTS orders ("
                                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                                + "time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                                + "customer_id INT, "
                                + "dish_id INT, "
                                + "FOREIGN KEY (customer_id) REFERENCES customer(id), "
                                + "FOREIGN KEY (dish_id) REFERENCES dish(id))";

                String createMerchantReview = "CREATE TABLE IF NOT EXISTS merchant_review ("
                                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                                + "merchant_id INT, "
                                + "customer_id INT, "
                                + "review TEXT, "
                                + "rating INT CHECK (rating BETWEEN 1 AND 5), "
                                + "time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                                + "FOREIGN KEY (merchant_id) REFERENCES merchant(id), "
                                + "FOREIGN KEY (customer_id) REFERENCES customer(id))";

                String createDishReview = "CREATE TABLE IF NOT EXISTS dish_review ("
                                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                                + "dish_id INT, "
                                + "customer_id INT, "
                                + "review TEXT, "
                                + "rating INT CHECK (rating BETWEEN 1 AND 5), "
                                + "time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                                + "FOREIGN KEY (dish_id) REFERENCES dish(id), "
                                + "FOREIGN KEY (customer_id) REFERENCES customer(id))";

                String createFavoriteMerchant = "CREATE TABLE IF NOT EXISTS favorite_merchant ("
                                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                                + "customer_id INT, "
                                + "merchant_id INT, "
                                + "FOREIGN KEY (customer_id) REFERENCES customer(id), "
                                + "FOREIGN KEY (merchant_id) REFERENCES merchant(id))";

                String createFavoriteDish = "CREATE TABLE IF NOT EXISTS favorite_dish ("
                                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                                + "customer_id INT, "
                                + "dish_id INT, "
                                + "FOREIGN KEY (customer_id) REFERENCES customer(id), "
                                + "FOREIGN KEY (dish_id) REFERENCES dish(id))";

                try {
                        connection = DriverManager.getConnection(url, username, password);
                        statement = connection.createStatement();
                        statement.execute(createCustomer);
                        statement.execute(createMerchant);
                        statement.execute(createDish);
                        statement.execute(createOrder);
                        statement.execute(createDishReview);
                        statement.execute(createMerchantReview);
                        statement.execute(createFavoriteMerchant);
                        statement.execute(createFavoriteDish);
                        statement.close();
                        connection.close();
                } catch (SQLException e) {
                        System.err.println("连接数据库时发生错误！检查用户和密码。");
                        e.printStackTrace();
                }
        }
        public static void createTuples(){
                BufferedReader bufferedReader;
                ArrayList<String> sql = new ArrayList<>();
                ArrayList<String> sqlDelete = new ArrayList<>();
                //read text file
                try{ 
                        
                        FileReader fileReader = new FileReader("./data/customer.txt");
                        bufferedReader = new BufferedReader(fileReader);
                        while(true){
                                String tableName;
                                String attribute;
                                ArrayList<String> tuples=new ArrayList<>();
                                //read first line
                                String line;
                                line=bufferedReader.readLine();
                                if(line==null||line.equals(""))break;
                                String[] arr = line.split(" ");
                                tableName = arr[0];
                                attribute=arr[1].replaceAll("^\\(|\\)$", "");
                                while (true) {
                                        line=bufferedReader.readLine();
                                        if(line==null||line.equals(""))break;
                                        tuples.add(line);
                                }
                                sqlDelete.add(delete(tableName));
                                sql.add(insert(tableName,attribute,tuples));
                                
                                
                        }
                        
                }catch(Exception e){
                        System.err.println("读取文件时发生错误！");
                        e.printStackTrace();
                }
                


                
                try {
                        connection = DriverManager.getConnection(url, username, password);
                        statement = connection.createStatement();
                        for (int i=0;i<sql.size();i++){

                                String string = sqlDelete.get(sqlDelete.size()-1-i);
                                statement.execute(string);
                        }
                        for (int i=0;i<sql.size();i++){
                                String string = sql.get(i);
                                statement.execute(string);
                        }
                        

                        statement.close();
                        connection.close();
                } catch (SQLException e) {
                        System.err.println("连接数据库时发生错误！");
                        e.printStackTrace();
                }
        }
        public static void createIndex(){
                // String dropIndexCustomer="ALTER TABLE dish DROP INDEX idx_customer_name;";

                // String dropIndexOrder="DROP INDEX idx_order_customer_id ON orders";
                // String dropIndexMerchant="DROP INDEX idx_merchant_name ON merchant";
                // String dropIndexDish="DROP INDEX idx_dish_name ON dish";

                String createIndexCustomer = "CREATE INDEX  idx_customer_name ON customer(id)";
                String createOrder= "CREATE INDEX   idx_order_customer_id ON orders(customer_id)";
                String createMerchant= "CREATE INDEX   idx_merchant_name ON merchant(id)";
                String createDish= "CREATE INDEX  idx_dish_name  ON dish(id)";
                try {
                        connection = DriverManager.getConnection(url, username, password);
                        statement = connection.createStatement();
                        statement.execute(createIndexCustomer);
                        statement.close();
                        connection.close();
                } catch (SQLException e) {
                }
                try {
                        connection = DriverManager.getConnection(url, username, password);
                        statement = connection.createStatement();
                        
                        statement.execute(createMerchant);
                        
                        statement.close();
                        connection.close();
                } catch (SQLException e) {
                }
                try {
                        connection = DriverManager.getConnection(url, username, password);
                        statement = connection.createStatement();
                        
                        statement.execute(createDish);
                        
                        statement.close();
                        connection.close();
                } catch (SQLException e) {
                }
                try {
                        connection = DriverManager.getConnection(url, username, password);
                        statement = connection.createStatement();

                        statement.execute(createOrder);
                        statement.close();
                        connection.close();
                } catch (SQLException e) {
                }
        }
        private static String insert(String tableName, String attribute, List<String> arrAttribute){
                String sql = "INSERT INTO "+tableName+"("+attribute+") VALUES ";
                for(int i=0;i<arrAttribute.size();i++){
                        if(i==arrAttribute.size()-1){
                                sql+="("+arrAttribute.get(i)+");";
                        }else{
                                sql+="("+arrAttribute.get(i)+"),";
                        }
                }
                return sql;
        }
        private static String delete(String tableName){
                String sql = "DELETE FROM "+tableName+" ;";
                return sql;
        }
        
}
 