import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Create.createTables();
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
