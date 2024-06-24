import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.protocol.Resultset;

public class AdvancedQueries {

    
    private  Connection connection;
    private Statement statement;
    AdvancedQueries(String password) {
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
    //0,1,2: 最近一周，最近一个月，最近一年;0,1，2：在线、离线、全部
    public void queryDishSalesAdvanced(int duration,int method,int customer_id) {
        String sql="SELECT dish_id,name, SUM(online*count )AS online,SUM((online=False)*count ) AS offline FROM dish,orders WHERE dish.id=orders.dish_id GROUP BY dish_id";
        if(duration==0){
            if(method==0){
                sql="SELECT favorite_dish.dish_id,name, SUM(online*count )AS online FROM dish,orders,favorite_dish "
                +"WHERE dish.id=orders.dish_id AND favorite_dish.customer_id="+Integer.toString(customer_id)+" AND dish.id=favorite_dish.dish_id AND orders.time>DATE_SUB(NOW(), INTERVAL 1 WEEK) GROUP BY dish_id";
            }else if(method==1){
                sql="SELECT favorite_dish.dish_id,name, SUM((online=False)*count ) AS offline FROM dish,orders,favorite_dish "
                +"WHERE dish.id=orders.dish_id AND favorite_dish.customer_id="+Integer.toString(customer_id)+" AND dish.id=favorite_dish.dish_id AND orders.time>DATE_SUB(NOW(), INTERVAL 1 WEEK) GROUP BY dish_id";
            }else if(method==2){
                sql="SELECT favorite_dish.dish_id,name, SUM(online*count )AS online,SUM((online=False)*count ) AS offline FROM dish,orders,favorite_dish "
                +"WHERE dish.id=orders.dish_id AND favorite_dish.customer_id="+Integer.toString(customer_id)+" AND dish.id=favorite_dish.dish_id AND orders.time>DATE_SUB(NOW(), INTERVAL 1 WEEK) GROUP BY dish_id";
            }
        }else if(duration==1){
            if(method==0){
                sql="SELECT favorite_dish.dish_id,name, SUM(online*count )AS online FROM dish,orders,favorite_dish "
                +"WHERE dish.id=orders.dish_id AND favorite_dish.customer_id="+Integer.toString(customer_id)+" AND dish.id=favorite_dish.dish_id AND orders.time>DATE_SUB(NOW(), INTERVAL 1 MONTH) GROUP BY dish_id";
            }else if(method==1){
                sql="SELECT favorite_dish.dish_id,name, SUM((online=False)*count ) AS offline FROM dish,orders,favorite_dish "
                +"WHERE dish.id=orders.dish_id AND favorite_dish.customer_id="+Integer.toString(customer_id)+" AND dish.id=favorite_dish.dish_id AND orders.time>DATE_SUB(NOW(), INTERVAL 1 MONTH) GROUP BY dish_id";
            }else if(method==2){
                sql="SELECT favorite_dish.dish_id,name, SUM(online*count )AS online,SUM((online=False)*count ) AS offline FROM dish,orders,favorite_dish "
                +"WHERE dish.id=orders.dish_id AND favorite_dish.customer_id="+Integer.toString(customer_id)+" AND dish.id=favorite_dish.dish_id AND orders.time>DATE_SUB(NOW(), INTERVAL 1 MONTH) GROUP BY dish_id";
            }
        }else if(duration==2){
            if(method==0){
                sql="SELECT favorite_dish.dish_id,name, SUM(online*count )AS online FROM dish,orders,favorite_dish "
                +"WHERE dish.id=orders.dish_id AND favorite_dish.customer_id="+Integer.toString(customer_id)+" AND dish.id=favorite_dish.dish_id AND orders.time>DATE_SUB(NOW(), INTERVAL 1 YEAR) GROUP BY dish_id";
            }else if(method==1){
                sql="SELECT favorite_dish.dish_id,name, SUM((online=False)*count ) AS offline FROM dish,orders,favorite_dish "
                +"WHERE dish.id=orders.dish_id AND favorite_dish.customer_id="+Integer.toString(customer_id)+" AND dish.id=favorite_dish.dish_id AND orders.time>DATE_SUB(NOW(), INTERVAL 1 YEAR) GROUP BY dish_id";
            }else if(method==2){
                sql="SELECT favorite_dish.dish_id,name, SUM(online*count )AS online,SUM((online=False)*count ) AS offline FROM dish,orders,favorite_dish "
                +"WHERE dish.id=orders.dish_id AND favorite_dish.customer_id="+Integer.toString(customer_id)+" AND dish.id=favorite_dish.dish_id AND orders.time>DATE_SUB(NOW(), INTERVAL 1 YEAR) GROUP BY dish_id";
            }
        }
        
        try {
            ResultSet rs = statement.executeQuery(sql);
            if (!rs.isBeforeFirst()) {
                System.out.println("没有找到符合的记录");
            } else {
                
                printResultSet(rs);   
            }
        } catch (SQLException e) {
            System.err.println("查询失败。");
            e.printStackTrace();
        } 
    }
    //0,1: 周，月;
    public void customerAnalysis(int customer_id,int interval){
        String sql=null;
        if(interval==0)
        sql="SELECT time AS 时间段,count(*) AS 下单量"
        +"FROM orders"
        +"WHERE orders.customer_id="+Integer.toString(customer_id)+" AND orders.time>DATE_SUB(NOW(), INTERVAL 10 WEEK)"
        +"GROUP BY UNIX_TIMESTAMP(timestamp) DIV 604800";
        else if(interval==1)
        sql="SELECT time AS 时间段,count(*) AS 下单量"
        +"FROM orders"
        +"WHERE orders.customer_id="+Integer.toString(customer_id)+" AND orders.time>DATE_SUB(NOW(), INTERVAL 10 MONTH)"
        +" GROUP BY UNIX_TIMESTAMP(timestamp) DIV 18144000";
        if(interval==1)
        sql="SELECT FROM_UNIXTIME(FLOOR((UNIX_TIMESTAMP(MIN(time)))/2592000)*2592000)AS 开始时间, FROM_UNIXTIME(FLOOR((UNIX_TIMESTAMP(MIN(time))+2592000)/2592000)*2592000)AS 结束时间,COUNT(DISTINCT orders.id) AS 下单量 FROM orders WHERE orders.customer_id="
        +Integer.toString(customer_id)+" GROUP BY UNIX_TIMESTAMP(time) DIV 18144000,customer_id;";
        else
        sql="SELECT FROM_UNIXTIME(FLOOR((UNIX_TIMESTAMP(MIN(time)))/604800)*604800)AS 开始时间, FROM_UNIXTIME(FLOOR((UNIX_TIMESTAMP(MIN(time))+604800)/604800)*604800)AS 结束时间,COUNT(DISTINCT orders.id) AS 下单量 FROM orders WHERE orders.customer_id="
        +Integer.toString(customer_id)+" GROUP BY UNIX_TIMESTAMP(time) DIV 604800,customer_id;";
        

        
        try {
            ResultSet rs = statement.executeQuery(sql);
            if (!rs.isBeforeFirst()) {
                System.out.println("没有找到符合的记录");   
            } else {
                printResultSet(rs);
            }
        }catch (SQLException e) {
            System.err.println("查询失败。");
            e.printStackTrace();
        }
        System.out.println("各时间段分布");
        sql="select '18:00:01-23:59:59' as 时间段,COUNT(DISTINCT orders.id) AS 下单量 from orders where TIME(time) >'18:00:01' AND TIME(time) <'23:59:59'" ;
        executeQuery(sql);
        sql="select '06:00:01-10:59:59' as 时间段,COUNT(DISTINCT orders.id) AS 下单量 from orders where TIME(time) >'06:00:01' AND TIME(time) <'10:59:59'";
        executeQuery(sql);
        sql="select '11:00:01-13:00:00' as 时间段,COUNT(DISTINCT orders.id) AS 下单量 from orders where TIME(time) >'11:00:01'AND TIME(time) <'13:00:00'";
        executeQuery(sql);
        sql="select '13:00:01-17:59:59' as 时间段,COUNT(DISTINCT orders.id) AS 下单量 from orders where TIME(time) >'13:00:01' AND TIME(time) <'17:59:59'";
        executeQuery(sql);
        sql="select '00:00:01-05:59:59' as 时间段,COUNT(DISTINCT orders.id) AS 下单量 from orders where TIME(time) >'00:00:01' AND TIME(time) <'05:59:59'";
        executeQuery(sql);

    }
    private void executeQuery(String sql) {
        try {
            ResultSet rs = statement.executeQuery(sql);
            printResultSet(rs);            
        } catch (SQLException e) {
            System.err.println("查询失败。");
            e.printStackTrace();
        }
    
    }

    public void dishSalesOfDedecatedCustomer(int threshold,int merchant_id){
        String sql="SELECT dish.id AS 菜品编号,dish.name AS 菜品名称,SUM(count) AS 忠实用户销量 "
        +"FROM orders JOIN dish ON orders.dish_id=dish.id"
        
        +" WHERE dish.merchant_id= "+Integer.toString(merchant_id)+" AND orders.customer_id IN "
        +"(SELECT customer_id FROM orders,dish WHERE orders.dish_id=dish.id AND dish.merchant_id ="+Integer.toString(merchant_id)+" GROUP BY orders.customer_id HAVING SUM(count)>"+Integer.toString(threshold)+")"
        +" GROUP BY dish.id";
        try {
            ResultSet rs = statement.executeQuery(sql);
            if (!rs.isBeforeFirst()) {
                System.out.println("没有找到符合的记录");   
            } else {
                printResultSet(rs);
            }
        }catch (SQLException e) {
            System.err.println("查询失败。");
            e.printStackTrace();
        }
    }

    public void dishStatAnalysis(int merchant_id){
        String sql="SELECT dish.id AS 菜品编号,dish.name AS 菜品名称,SUM(count) AS 总销量,AVG(rating) AS 评分,"
        +"(SELECT customer.id FROM customer,orders WHERE dish.id=orders.dish_id AND orders.customer_id=customer.id GROUP BY customer.id ORDER BY SUM(count) DESC LIMIT 1 )AS 最忠实顾客编号,  "
        +"(SELECT customer.name FROM customer,orders WHERE dish.id=orders.dish_id AND orders.customer_id=customer.id GROUP BY customer.id ORDER BY 购买量 DESC LIMIT 1 )AS 最忠实顾客,  "
        +"(SELECT SUM(count) FROM customer,orders WHERE dish.id=orders.dish_id AND orders.customer_id=customer.id GROUP BY customer.id ORDER BY 购买量 DESC LIMIT 1 )AS 最忠实顾客购买量  "
            +"FROM orders,dish,dish_review"
            
            +"WHERE dish.id=orders.dish_id AND dish.merchant_id="+Integer.toString(merchant_id)+" AND dish_review.dish_id=dish.id AND temp.dish_id=dish.id"
            +"GROUP BY dish.id";
        //菜品数据分析：某个商户所有菜品的评分、销量以及购买该菜品次数最多的人。
        try {
            ResultSet rs = statement.executeQuery(sql);
            if (!rs.isBeforeFirst()) {
                System.out.println("没有找到符合的记录");   
            } else {
                printResultSet(rs);
            }
        }catch (SQLException e) {
            System.err.println("查询失败。");
            e.printStackTrace();
        }
    }
    public void customerStatAnalysis(){
        String sql="SELECT merchant.name,销量 FROM merchant,(SELECT COUNT(*) AS 销量,merchant_id FROM orders,customer,dish WHERE customer.gender='男' AND customer.id=orders.customer_id AND dish.id=orders.dish_id GROUP BY dish.merchant_id ORDER BY COUNT(DISTINCT orders.id) DESC LIMIT 1)AS 最受欢迎商户 WHERE merchant.id=最受欢迎商户.merchant_id";
        
        try {
            ResultSet rs=statement.executeQuery(sql);   
            while(rs.next()){
                String name=rs.getString("name");
                int sales=rs.getInt("销量");
                System.out.println("男性最喜爱的商户是："+name+"，销量为："+Integer.toString(sales));
            }   
            
        } catch (Exception e) {
            // TODO: handle exception
        }
         sql="SELECT merchant.name,销量 FROM merchant,(SELECT COUNT(*) AS 销量,merchant_id FROM orders,customer,dish WHERE customer.gender='女' AND customer.id=orders.customer_id AND dish.id=orders.dish_id GROUP BY dish.merchant_id ORDER BY COUNT(DISTINCT orders.id) DESC LIMIT 1)AS 最受欢迎商户 WHERE merchant.id=最受欢迎商户.merchant_id";
        
        try {
            ResultSet rs=statement.executeQuery(sql);   
            while(rs.next()){
                String name=rs.getString("name");
                int sales=rs.getInt("销量");
                System.out.println("女性最喜爱的商户是："+name+"，销量为："+Integer.toString(sales));
            }   
            
        } catch (Exception e) {
            // TODO: handle exception
        }
        sql="SELECT dish.name,销量 FROM dish,(SELECT COUNT(*) AS 销量,orders.dish_id FROM orders,customer WHERE customer.gender='女' AND customer.id=orders.customer_id GROUP BY dish_id ORDER BY COUNT(DISTINCT orders.id) DESC LIMIT 1)AS 最受欢迎商户 WHERE dish.id=最受欢迎商户.dish_id";
        try {
            ResultSet rs=statement.executeQuery(sql);   
            while(rs.next()){
                String name=rs.getString("name");
                int sales=rs.getInt("销量");
                System.out.println("女性最喜爱的菜品是："+name+"，销量为："+Integer.toString(sales));
            }   
            
        } catch (Exception e) {
            // TODO: handle exception
        }
        sql="SELECT dish.name,销量 FROM dish,(SELECT COUNT(*) AS 销量,orders.dish_id FROM orders,customer WHERE customer.gender='男' AND customer.id=orders.customer_id GROUP BY dish_id ORDER BY COUNT(DISTINCT orders.id) DESC LIMIT 1)AS 最受欢迎商户 WHERE dish.id=最受欢迎商户.dish_id";
        try {
            ResultSet rs=statement.executeQuery(sql);   
            while(rs.next()){
                String name=rs.getString("name");
                int sales=rs.getInt("销量");
                System.out.println("男性最喜爱的菜品是："+name+"，销量为："+Integer.toString(sales));
            }   
            
        } catch (Exception e) {
            // TODO: handle exception
        }
        String sql1="SELECT COUNT(*) FROM dish_review,customer WHERE dish_review.customer_id=customer.id AND customer.gender='女'"; 
        String sql2="SELECT COUNT(*) FROM dish_review,customer WHERE dish_review.customer_id=customer.id AND customer.gender='男'"; 
        String sql3="SELECT COUNT(DISTINCT orders.id) FROM orders,customer WHERE orders.customer_id=customer.id AND customer.gender='女'"; 
        String sql4="SELECT COUNT(DISTINCT orders.id) FROM orders,customer WHERE orders.customer_id=customer.id AND customer.gender='男'"; 
        try {
            int maleOrder;
            int femaleOrder;
            int maleReview;
            int femaleReview;
            ResultSet rs=statement.executeQuery(sql1); 
            rs.next();  
            femaleReview=rs.getInt(1);
            
            rs=statement.executeQuery(sql2);   
            rs.next();
            maleReview=rs.getInt(1);

            rs=statement.executeQuery(sql3);   
            rs.next();
            femaleOrder=rs.getInt(1);

            rs=statement.executeQuery(sql4);   
            rs.next();
            maleOrder=rs.getInt(1);

            System.out.println("女性的评论频率为："+Float.toString((float)femaleReview/(float)femaleOrder)+"/每单");
            System.out.println("男性的评论频率为："+Float.toString((float)maleReview/(float)maleOrder)+"/每单");
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void printResultSet(ResultSet rs) throws SQLException {
        // 获取结果集的元数据
        java.sql.ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        
        // 打印列名
        for (int i = 1; i <= columnCount; i++) {
            System.out.print(rsmd.getColumnName(i) + "\t");
        }
        System.out.println();
        
        // 打印数据行
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(rs.getObject(i) + "\t");
            }
            System.out.println();
        }
    }

    //return single line of result set
    private ResultSet findCustomerMostDedicated(int dish_id){
        String sql="SELECT customer.id FROM customer,orders WHERE AND orders.customer_id=customer.id AND orders.dish_id="+Integer.toString(dish_id)+" GROUP BY customer.id ORDER BY 购买量 DESC LIMIT 1";
        try {
            ResultSet rs = statement.executeQuery(sql);
            if (!rs.isBeforeFirst()) {
                System.out.println("没有找到符合的记录");
            } else {
                return rs;
            }
        } catch (SQLException e) {
            System.err.println("查询失败。");
            e.printStackTrace();
        }
        return null;
    }
}
