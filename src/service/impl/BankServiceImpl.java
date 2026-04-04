package service.impl;

import domain.Account;
import domain.Customer;
import domain.Transaction;
import domain.Type;
import repository.AccountRespository;
import repository.CustomerRepository;
import repository.TransactionRepository;
import service.BankService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {
    private final AccountRespository accountRespository =new AccountRespository();
    private final TransactionRepository transactionRepository=new TransactionRepository();
    private final CustomerRepository customerRepository=new CustomerRepository();
    @Override
    public String openAccount(String name, String email, String accountType) {
        String customerId= UUID.randomUUID().toString();
        //String accountNumber=UUID.randomUUID().toString();
        String accountNumber = getAccountNumber();
        Account account=new Account(accountNumber,customerId,(double)0,accountType);
        //save
        accountRespository.save(account);
        return accountNumber;
    }

    @Override
    public List<Account> listAccounts() {
        return accountRespository.findAll().stream()
                .sorted(Comparator.comparing(Account::getAccountNumber))
                .collect(Collectors.toList());
             }

    @Override
    public void deposit(String accountNumber, Double amount, String note) {
        Account account=accountRespository.findByNumber(accountNumber).
                orElseThrow(()->new RuntimeException("Account not Found: " + accountNumber));
        account.setBalance(account.getBalance() + amount);
        Transaction transaction=new Transaction(UUID.randomUUID().toString(), Type.DEPOSIT,account.getAccountNumber(),amount, LocalDateTime.now(),note);
        transactionRepository.add(transaction);

    }

    @Override
    public void withdraw(String accountNumber, Double amount, String note) {
        Account account=accountRespository.findByNumber(accountNumber).
                orElseThrow(()->new RuntimeException("Account not Found: " + accountNumber));
        if(account.getBalance().compareTo(amount)<0){
            throw new RuntimeException("Insufficient Balance");
        }
        account.setBalance(account.getBalance()-amount);
        Transaction transaction=new Transaction(UUID.randomUUID().toString(), Type.WITHDRAW,
                account.getAccountNumber(),amount, LocalDateTime.now(),note);
        transactionRepository.add(transaction);
    }

    private String getAccountNumber() {
        int size=accountRespository.findAll().size()+1;
        String accountNumber= String.format("AC%06d",size);
        return accountNumber;
    }

    @Override
    public void transfer(String fromAcc, String toAcc, Double amount, String note) {
        if(fromAcc.equals(toAcc)){
            throw new RuntimeException("Cannot transfer to your own account ");
        }
        Account from=accountRespository.findByNumber(fromAcc)
                .orElseThrow(()-> new RuntimeException("Account not found"));
        Account to=accountRespository.findByNumber(toAcc)
                .orElseThrow(()-> new RuntimeException("Account not found"));
        if(from.getBalance().compareTo(amount)<0){
            throw new RuntimeException("Insufficient Balance");
        }
        from.setBalance(from.getBalance()-amount);
        to.setBalance(to.getBalance()+amount);
        Transaction fromtransaction=new Transaction(UUID.randomUUID().toString(),
                Type.TRANSFER_OUT,fromAcc,amount,LocalDateTime.now(),note);
        transactionRepository.add(fromtransaction);

        Transaction totransaction=new Transaction(UUID.randomUUID().toString(),
                Type.TRANSFER_IN,toAcc,amount,LocalDateTime.now(),note);
        transactionRepository.add(totransaction);

    }

    @Override
    public List<Transaction> getStatement(String account) {
        return transactionRepository.findByAccount(account)
                .stream().sorted(Comparator.comparing(Transaction::getTimeStamp))
                .collect(Collectors.toList());
    }

    @Override
    public List<Account> searchAccountsByCustomerName(String q) {
        String query=(q==null)? "": q.toLowerCase();
        List<Account> result=new ArrayList<>();
        for(Customer c : customerRepository.findAll()){
            if(c.getName().toLowerCase().contains(query)){
                result.addAll(accountRespository.findByCustomerId(c.getId()));
            }
        }
        result.sort(Comparator.comparing(Account :: getAccountNumber));
        return result;
    }


}
