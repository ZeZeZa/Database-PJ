import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Create.createTables();

        System.out.println("是否加载模板数据?这会清除已有的记录！(y/n)");
        if (scanner.next().equals("y")) {
            Create.createTuples();
        }
        System.out.println("是否创建索引?(y/n)");
        if (scanner.next().equals("y")) {
            Create.createIndex();
        }
        while (true) {
            System.out.println("请选择登录的身份:(1:管理员/2:用户/3:商户/4:退出)");
            int identity = scanner.nextInt();
            if (identity == 1) {
                Administrator administrator = new Administrator();
                administrator.administrator();
            } else if (identity == 2) {
                Customer customer = new Customer();
                customer.customer();
            } else if (identity == 3) {
                Merchant merchant = new Merchant();
                merchant.merchant();
            } else if (identity == 4) {
                break;
            }
        }
        scanner.close();
    }

}
