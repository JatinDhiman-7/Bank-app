package repository;

import domain.Account;

import java.util.*;

public class AccountRespository {
   private final Map<String, Account> accountByNumber=new HashMap<>();

   public void save(Account account){
       accountByNumber.put(account.getAccountNumber(),account);
   }
   public List<Account> findAll(){
       return new ArrayList<>(accountByNumber.values());
   }

    public Optional<Account> findByNumber(String accountNumber) {
           return Optional.ofNullable(accountByNumber.get(accountNumber));
    }

    public List<Account> findByCustomerId(String customerId) {
       List<Account> result=new ArrayList<>();
       for(Account a: accountByNumber.values()){
           if(a.getCustomerId().equals(customerId))
               result.add(a);
       }
       return result;
    }
}
