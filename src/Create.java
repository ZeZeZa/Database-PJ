import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Create {
        public static String url = "jdbc:mysql://localhost:3306/pj";
        public static String username = "root";
        public static String password = "gyh040506";
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
                        System.err.println("连接数据库时发生错误！");
                        e.printStackTrace();
                }
        }
}
