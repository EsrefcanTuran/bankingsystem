package com.esref.bankingsystem;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
	@Autowired
    private AccountService accountService;
	
    @PostMapping("/accounts/{accountNumber}")
    public ResponseEntity<?> transferMoney(@PathVariable long accountNumber,@RequestBody TransferRequest request) {
        return this.accountService.transferMoney(accountNumber, request);
    }
    
    @PatchMapping(path="/accounts/{accountNumber}")
	public ResponseEntity<?> deposit(@PathVariable long accountNumber,@RequestBody AccountDepositRequest request){
		return this.accountService.deposit(accountNumber, request);
	}
    
    @PostMapping("/accounts")
    public ResponseEntity<?> createAccount(@RequestBody AccountCreateRequest request) {
    	return this.accountService.createAccount(request);
    }
    
    @GetMapping(path="/accounts/{accountNumber}")
	public ResponseEntity<?> accountDetail(@PathVariable long accountNumber) {
		return this.accountService.accountDetail(accountNumber);
	}
    
    @CrossOrigin(origins={"http://localhost"})
	@GetMapping(path="/accounts/logs/{accountNumber}")
	public ResponseEntity<?> transactionLogs(@PathVariable long accountNumber) {
		try {
			return this.accountService.transactionLogs(accountNumber);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
