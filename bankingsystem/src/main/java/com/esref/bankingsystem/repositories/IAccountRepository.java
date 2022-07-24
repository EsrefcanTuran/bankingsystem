package com.esref.bankingsystem.repositories;

import java.util.ArrayList;

import com.esref.bankingsystem.models.Account;

public interface IAccountRepository {
	
	public Account create(String name, String surname, String email, String tc, String type);
	
	public Account update(Account a);
	
	public Account findByAccountNumber(long accountNumber);
	
	public boolean transfer(double amount, long ownerAccountNumber, long transferAccountNumber);
	
	public Account deposit(long accountNumber, double balance);
	
	public ArrayList<String> transactionLogs(long accountNumber);

}
