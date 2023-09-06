package Project1;

import java.util.*;
import java.util.stream.Collectors;

class User {
    private String username;
    private String password;
    private List<Account> accounts;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.accounts = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }
}

class Account {
    private String accountType;
    private double balance;
    private List<Transaction> transactions;

    public Account(String accountType) {
        this.accountType = accountType;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }

    public String getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void deposit(double amount) {
        balance += amount;
        transactions.add(new Transaction("Deposit", amount));
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactions.add(new Transaction("Withdrawal", amount));
        } else {
            System.out.println("Insufficient funds.");
        }
    }
}

class Transaction {
    private String type;
    private double amount;
    private Date timestamp;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.timestamp = new Date();
    }

    @Override
    public String toString() {
        return "Type: " + type + ", Amount: Rs." + amount + ", Time: " + timestamp;
    }
}

public class BankManagement {
    private static List<User> users = new ArrayList<>();
    private static User currentUser;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Register\n2. Login\n3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    registerUser(scanner);
                    break;
                case 2:
                    loginUser(scanner);
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void registerUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User newUser = new User(username, password);
        users.add(newUser);
        System.out.println("Registration successful.");
    }

    private static void loginUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Optional<User> userOptional = users.stream()
                .filter(user -> user.getUsername().equals(username) && user.authenticate(password))
                .findFirst();

        if (userOptional.isPresent()) {
            currentUser = userOptional.get();
            showMainMenu(scanner);
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private static void showMainMenu(Scanner scanner) {
        System.out.println("Welcome, " + currentUser.getUsername() + "!");
        while (true) {
            System.out.println("\n1. View Account Details\n2. View Transaction History\n3. Deposit\n4. Withdraw\n5. Add Account\n6. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    viewAccountDetails();
                    break;
                case 2:
                    viewTransactionHistory();
                    break;
                case 3:
                    deposit(scanner);
                    break;
                case 4:
                    withdraw(scanner);
                    break;
                case 5:
                    addAccount(scanner);
                    break;
                case 6:
                    currentUser = null;
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewAccountDetails() {
        List<Account> accounts = currentUser.getAccounts();
        if (!accounts.isEmpty()) {
            System.out.println("Account Details:");
            for (Account account : accounts) {
                System.out.println("Account Type: " + account.getAccountType());
                System.out.println("Balance: Rs." + account.getBalance());
            }
        } else {
            System.out.println("No accounts found.");
        }
    }

    private static void viewTransactionHistory() {
        List<Account> accounts = currentUser.getAccounts();
        if (!accounts.isEmpty()) {
            for (Account account : accounts) {
                List<Transaction> transactions = account.getTransactions();
                if (!transactions.isEmpty()) {
                    System.out.println("Transaction History for " + account.getAccountType() + ":");
                    for (Transaction transaction : transactions) {
                        System.out.println(transaction);
                    }
                } else {
                    System.out.println("No transactions found for " + account.getAccountType());
                }
            }
        } else {
            System.out.println("No accounts found.");
        }
    }

    private static void deposit(Scanner scanner) {
        System.out.print("Enter the account type to deposit into: ");
        String accountType = scanner.nextLine();
        List<Account> accounts = currentUser.getAccounts();

        Optional<Account> accountOptional = accounts.stream()
                .filter(account -> account.getAccountType().equalsIgnoreCase(accountType))
                .findFirst();

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            System.out.print("Enter the amount to deposit: Rs.");
            double amount = scanner.nextDouble();
            account.deposit(amount);
            System.out.println("Deposit successful.");
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void withdraw(Scanner scanner) {
        System.out.print("Enter the account type to withdraw from: ");
        String accountType = scanner.nextLine();
        List<Account> accounts = currentUser.getAccounts();

        Optional<Account> accountOptional = accounts.stream()
                .filter(account -> account.getAccountType().equalsIgnoreCase(accountType))
                .findFirst();

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            System.out.print("Enter the amount to withdraw: Rs.");
            double amount = scanner.nextDouble();
            account.withdraw(amount);
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void addAccount(Scanner scanner) {
        System.out.print("Enter the account type to add: ");
        String accountType = scanner.nextLine();
        List<Account> accounts = currentUser.getAccounts();

        // Check if the user already has an account of the same type
        boolean accountExists = accounts.stream()
                .anyMatch(account -> account.getAccountType().equalsIgnoreCase(accountType));

        if (accountExists) {
            System.out.println("You already have an account of this type.");
        } else {
            Account newAccount = new Account(accountType);
            currentUser.addAccount(newAccount);
            System.out.println("Account added successfully.");
        }
    }
}
