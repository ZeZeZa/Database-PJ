import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Create.createTables();
        while (true) {
            System.out.println("请选择登录的身份:(1:管理员/2:用户/3:商户/4:退出)");
            int identity = scanner.nextInt();
            if (identity == 1) {
                administrator();
            } else if (identity == 2) {
                Customer customer = new Customer();
                customer.customer();
            } else if (identity == 4) {
                break;
            }
        }
        scanner.close();
    }

    public static void administrator() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("请选择要执行的操作:(1:用户操作/2:商户操作/3:退出)");
            int operation = scanner.nextInt();
            if (operation == 1) {
                System.out.println("请选择要执行的具体操作:(1:添加用户/2:删除用户/3:修改用户)");
                int command = scanner.nextInt();
                scanner.nextLine();
                System.out.println("请输入需要操作的用户名称和身份(用空格分隔):");
                String input = scanner.nextLine();
                String[] parts = input.split(" ");
                String name = parts[0];
                String identity = parts[1];
                if (command == 1) {
                    CustomerOperation.insertCustomer(name, identity);
                } else if (command == 2) {
                    CustomerOperation.deleteCustomer(name, identity);
                }
            } else if (operation == 2) {
                System.out.println("请选择要执行的具体操作:(1:添加商户/2:删除商户/3:修改商户)");
                int command = scanner.nextInt();
                scanner.nextLine();
                System.out.println("请输入需要操作的商户名称:");
                String name = scanner.nextLine();
                if (command == 1) {
                    MerchantOperation.insertMerchant(name);
                } else if (command == 2) {
                    MerchantOperation.deleteMerchant(name);
                }
            } else if (operation == 3) {
                break;
            }
        }
    }

}
