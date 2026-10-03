import java.util.*;
class Restaurant
{
    static double calculateBill(double price, int quantity)
    {
        double bill = price*quantity;
        return bill;
    }
    public static void main(String args[])
    {
        Scanner sc= new Scanner (System.in);
        double price;
        String item[] = {"1. Burger-120", "2. Pizza-250", "3. Pasta-180", "4. Juice-80", "5. Sandwich-100"};
        for(int i=0; i<5; i++)
        {
            System.out.println(item[i]);
        }
        System.out.println("Enter  your choice:");
        int choice = sc.nextInt();
        switch (choice)
        {
            case 1:
                price = 120.0;
                break;
            case 2:
                price=250.0;
                break;
            case 3:
                price = 180.0;
                break;
            case 4:
                price = 80.0;
                break;
            case 5:
                price =100.0;
                break;
            default:
                System.out.println("Item Unavailable");
                return;
        }
        System.out.println("Enter the quantity:");
        int quantity = sc.nextInt();
        double bill = calculateBill(price, quantity);
        double discount;
        if(bill>1000)
            discount = bill*(10/100.0);
        else
            discount =0;
        double total = bill-discount;
        System.out.println("Final bill = "+total);
        sc.close();
    }
}
