import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class RestaurantSystem {

    // CO1: Variables and console input/output
    static Scanner sc = new Scanner(System.in);

    // CO3: Array to store the restaurant menu
    static MenuItem[] menu = {
        new MenuItem("Idli", 40),
        new MenuItem("Dosa", 70),
        new MenuItem("Poori", 60),
        new MenuItem("Veg Fried Rice", 120),
        new MenuItem("Paneer Biryani", 180),
        new MenuItem("Chicken Biryani", 220),
        new MenuItem("Veg Manchurian", 130),
        new MenuItem("Paneer Butter Masala", 190),
        new MenuItem("Lime Soda", 50),
        new MenuItem("Gulab Jamun", 60)
    };

    // CO6: ArrayList stores the orders made during this run
    static ArrayList<Order> orders = new ArrayList<>();
    static int orderNumber = 1;

    public static void main(String[] args) {
        int choice;

        // CO2: Loop repeats until the user chooses Exit
        do {
            System.out.println("\n===== RESTAURANT SYSTEM =====");
            System.out.println("1. Show menu");
            System.out.println("2. Place order");
            System.out.println("3. View latest order");
            System.out.println("4. Generate bill");
            System.out.println("5. Search food item");
            System.out.println("6. Update kitchen status");
            System.out.println("7. Show daily sales");
            System.out.println("8. Exit");

            choice = readNumber("Enter choice: ");

            // CO2: switch-case selects an operation
            switch (choice) {
                case 1:
                    showMenu();
                    break;
                case 2:
                    placeOrder();
                    break;
                case 3:
                    viewOrder();
                    break;
                case 4:
                    printBill();
                    break;
                case 5:
                    searchFood();
                    break;
                case 6:
                    updateKitchenStatus();
                    break;
                case 7:
                    showSales();
                    break;
                case 8:
                    System.out.println("Thank you!");
                    break;
                default:
                    System.out.println("Please enter a number from 1 to 8.");
            }
        } while (choice != 8);
    }

    // CO3: Method to display menu items stored in an array
    static void showMenu() {
        System.out.println("\n--- FOOD MENU ---");
        for (int i = 0; i < menu.length; i++) {
            System.out.printf("%d. %-22s Rs. %.2f%n",
                    i + 1, menu[i].name, menu[i].price);
        }
    }

    // CO3: Method and array used to take an order
    static void placeOrder() {
        int table = readNumber("Enter table number: ");
        if (table <= 0) {
            System.out.println("Table number must be positive.");
            return;
        }

        int[] quantity = new int[menu.length];
        showMenu();

        while (true) {
            int item = readNumber("Enter item number (0 to finish): ");
            if (item == 0) {
                break;
            }
            if (item < 1 || item > menu.length) {
                System.out.println("Invalid item number.");
                continue;
            }

            int qty = readNumber("Enter quantity: ");
            if (qty <= 0) {
                System.out.println("Quantity must be positive.");
                continue;
            }

            quantity[item - 1] = quantity[item - 1] + qty;
            System.out.println("Item added.");
        }

        boolean empty = true;
        for (int q : quantity) {
            if (q > 0) {
                empty = false;
            }
        }

        if (empty) {
            System.out.println("No items selected.");
            return;
        }

        // CO4: Create an Order object; CO6: add it to ArrayList
        Order order = new Order(orderNumber, table, quantity);
        orders.add(order);
        orderNumber++;

        System.out.println("Order placed successfully. Order number: " + order.id);
    }

    static Order latestOrder() {
        if (orders.size() == 0) {
            return null;
        }
        return orders.get(orders.size() - 1);
    }

    static void viewOrder() {
        Order order = latestOrder();
        if (order == null) {
            System.out.println("No order found.");
            return;
        }

        System.out.println("\nOrder number: " + order.id);
        System.out.println("Table number: " + order.table);
        System.out.println("Kitchen status: " + order.status);
        for (int i = 0; i < menu.length; i++) {
            if (order.quantity[i] > 0) {
                System.out.println(menu[i].name + " x " + order.quantity[i]);
            }
        }
    }

    // CO3: Method calculates the subtotal
    static double calculateTotal(Order order) {
        double total = 0;
        for (int i = 0; i < menu.length; i++) {
            total = total + menu[i].price * order.quantity[i];
        }
        return total;
    }

    static void printBill() {
        Order order = latestOrder();
        if (order == null) {
            System.out.println("Place an order first.");
            return;
        }

        double subtotal = calculateTotal(order);
        double tax = subtotal * 0.05; // Example tax rate of 5%
        double total = subtotal + tax;

        System.out.println("\n========== BILL ==========");
        System.out.println("Order: " + order.id + "  Table: " + order.table);
        for (int i = 0; i < menu.length; i++) {
            if (order.quantity[i] > 0) {
                double amount = menu[i].price * order.quantity[i];
                System.out.printf("%-22s x %d = Rs. %.2f%n",
                        menu[i].name, order.quantity[i], amount);
            }
        }
        System.out.printf("Subtotal: Rs. %.2f%n", subtotal);
        System.out.printf("Tax (5%%): Rs. %.2f%n", tax);
        System.out.printf("Total:    Rs. %.2f%n", total);

        // CO5: Save the bill to a text file
        if (!order.saved) {
            try {
                FileWriter file = new FileWriter("restaurant_records.txt", true);
                file.write("Order " + order.id + ", Table " + order.table
                        + ", Total Rs. " + String.format("%.2f", total) + "\n");
                file.close();
                order.saved = true;
                order.total = total;
                System.out.println("Bill saved to restaurant_records.txt");
            } catch (IOException e) {
                System.out.println("Could not save the bill.");
            }
        } else {
            System.out.println("This bill has already been saved.");
        }
    }

    // CO5: String methods are used to search menu names
    static void searchFood() {
        System.out.print("Enter food name or part of the name: ");
        String word = sc.nextLine().trim().toLowerCase();
        boolean found = false;

        for (int i = 0; i < menu.length; i++) {
            if (menu[i].name.toLowerCase().contains(word)) {
                System.out.println(menu[i].name + " - Rs. " + menu[i].price);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching item found.");
        }
    }

    // CO4: Updates the status stored in the Order object
    static void updateKitchenStatus() {
        Order order = latestOrder();
        if (order == null) {
            System.out.println("Place an order first.");
            return;
        }

        System.out.println("1. Received");
        System.out.println("2. Preparing");
        System.out.println("3. Ready");
        System.out.println("4. Served");
        int statusChoice = readNumber("Choose status: ");

        switch (statusChoice) {
            case 1:
                order.status = "Received";
                break;
            case 2:
                order.status = "Preparing";
                break;
            case 3:
                order.status = "Ready";
                break;
            case 4:
                order.status = "Served";
                break;
            default:
                System.out.println("Invalid status choice.");
                return;
        }
        System.out.println("Kitchen status updated to " + order.status);
    }

    // CO6: Stream filters billed orders and adds their totals
    static void showSales() {
        double sales = orders.stream()
                .filter(order -> order.saved)
                .mapToDouble(order -> order.total)
                .sum();

        System.out.printf("Sales from saved bills this run: Rs. %.2f%n", sales);
        System.out.println("Note: Sales summary is for this program run only.");
    }

    // CO4: try-catch handles invalid number input
    static int readNumber(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }
}

// CO4: Class used to represent a menu item (class and object concept)
class MenuItem {
    String name;
    double price;

    MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

// CO4: Order class represents one customer's order
class Order {
    int id;
    int table;
    int[] quantity;
    String status;
    boolean saved;
    double total;

    Order(int id, int table, int[] quantity) {
        this.id = id;
        this.table = table;
        this.quantity = quantity;
        this.status = "Received";
        this.saved = false;
        this.total = 0;

    }
}
