package app;

import service.BankService;
import service.impl.BankServiceImpl;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner=new Scanner(System.in);
        boolean running=true;
        BankService bankService=new BankServiceImpl();
        System.out.println("Welcome to Console Bank");
        while(running){
            System.out.println("""
                1) Open Account
                2) Deposit
                3) Withdraw
                4) Transfer
                5) Account Statement
                6) List Accounts
                7) Search Accounts by Customer Name
                0) Exit
                """);
            System.out.print("CHOOSE: ");
            String choice=scanner.nextLine().trim();
            System.out.println("CHOICE: "+ choice);
            switch (choice){
                case "1" -> openAccount(scanner,bankService);
                case "2" -> deposit(scanner,bankService);
                case "3" -> withdraw(scanner,bankService);
                case "4" -> transfer(scanner,bankService);
                case "5" -> statement(scanner,bankService);
                case "6" -> listAccounts(scanner,bankService);
                case "7" -> searchAccounts(scanner,bankService);
                case "0" -> running=false;


            }
        }



    }

    private static void openAccount(Scanner scanner, BankService bankService) {
         System.out.println("Customer name: ");
         String name=scanner.nextLine().trim();
         System.out.println("Customer email: ");
         String email=scanner.nextLine().trim();
         System.out.println("Account Type (SAVINGS/CURRENT): ");
         String type=scanner.nextLine().trim();
         System.out.println("Initial deposit (optional/blank for 0): ");
         String amountStr=scanner.nextLine().trim();
         Double initial =Double.valueOf(amountStr);
         String accountNumber=bankService.openAccount(name,email,type);
         if(initial > 0){
             bankService.deposit(accountNumber,initial,"Initial deposit");
         }
         System.out.println("Account Opened: " + accountNumber);
    }

    private static void deposit(Scanner scanner,BankService bankService) {
        System.out.println("Account number: ");
        String accountNumber=scanner.nextLine().trim();
        System.out.println("Amount: ");
        Double amount=Double.valueOf(scanner.nextLine().trim());
        bankService.deposit(accountNumber,amount,"Deposited");
        System.out.println("Deposited");
    }

    private static void withdraw(Scanner scanner,BankService bankService) {
        System.out.println("Account number: ");
        String accountNumber=scanner.nextLine().trim();
        System.out.println("Amount: ");
        Double amount=Double.valueOf(scanner.nextLine().trim());
        bankService.withdraw(accountNumber,amount,"Deposited");
        System.out.println("withdrawn");
    }

    private static void transfer(Scanner scanner,BankService bankService) {
        System.out.println("From Account: ");
        String from=scanner.nextLine().trim();
        System.out.println("To Account: ");
        String to=scanner.nextLine().trim();
        System.out.println("Amount: ");
        Double amount=Double.valueOf(scanner.nextLine().trim());
        bankService.transfer(from,to,amount,"transfer");
    }

    private static void statement(Scanner scanner,BankService bankService) {
        System.out.println("Account: ");
        String account=scanner.nextLine().trim();
        bankService.getStatement(account).forEach(t->{
            System.out.println(t.getTimeStamp() + " | " + t.getType() + " | " + t.getAmount() + " | " + t.getNote() );
        });
    }

    private static void listAccounts(Scanner scanner,BankService bankService) {
        bankService.listAccounts().forEach(a->{
            System.out.println(a.getAccountNumber() + " | " + a.getAccountType() + " | " + a.getBalance());
        });
    }

    private static void searchAccounts(Scanner scanner,BankService bankService) {
        System.out.println("Customer name contains: ");
        String q=scanner.nextLine().trim();
        bankService.searchAccountsByCustomerName(q);

    }
}
