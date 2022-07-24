package com.esref.bankingsystem;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.esref.bankingsystem.models.Account;
import com.esref.bankingsystem.repositories.IAccountRepository;
import com.esref.bankingsystem.requests.AccountCreateRequest;
import com.esref.bankingsystem.responses.AccountCreateSuccessResponse;
import com.esref.bankingsystem.requests.DepositRequest;
import com.esref.bankingsystem.requests.TransferRequest;
import com.esref.bankingsystem.responses.AccountCreateInvalidTypeResponse;

@RestController
public class AccountController {
	
	@Autowired
    private IAccountRepository accountRepository;
	
	@Autowired
    private KafkaTemplate<String, String> producer;
	
	@PostMapping(path = "/accounts")
    public ResponseEntity<?> create(@RequestBody AccountCreateRequest request) {
		Account createdAccount = this.accountRepository.create(request.getName(), request.getSurname(), request.getEmail(), request.getTc(), request.getType());
		if (createdAccount == null) {
			AccountCreateInvalidTypeResponse resp = new AccountCreateInvalidTypeResponse();
			resp.setMessage("Invalid Account Type: " + request.getType());
			return ResponseEntity.badRequest().body(resp);
		} else {
			AccountCreateSuccessResponse resp = new AccountCreateSuccessResponse();
			resp.setMessage("Account Created");
			resp.setAccountNumber(createdAccount.getAccountNumber());
			return ResponseEntity.ok().body(resp);
		}
    }
	
	@GetMapping(path = "/accounts/{accountNumber}")
	public ResponseEntity<?> detail(@PathVariable long accountNumber) {
		Account a = this.accountRepository.findByAccountNumber(accountNumber);
		return ResponseEntity.ok().lastModified(a.getLastModified()).body(a);
	}
	
	@PatchMapping(path = "/accounts/{accountNumber}")
	public ResponseEntity<?> deposit(@PathVariable long accountNumber,@RequestBody DepositRequest request){
		Account a = this.accountRepository.deposit(accountNumber, request.getAmount());
		String logMessage = accountNumber + " deposit amount:" + request.getAmount();
    	producer.send("logs", logMessage);
    	return ResponseEntity.ok().body(a);
	}
	
    @PostMapping(path = "/accounts/{accountNumber}")
    public ResponseEntity<?> transfer(@PathVariable long accountNumber,@RequestBody TransferRequest request) {
        boolean result = this.accountRepository.transfer(request.getAmount(), accountNumber, request.getTransferredAccountNumber());
        if (result) {
        	String logMessage = accountNumber + " transfer amount:" + request.getAmount() + ",transferred_account:" + request.getTransferredAccountNumber();
        	producer.send("logs", logMessage);
        	return ResponseEntity.ok().body("Transferred Successfully");
        }
        return ResponseEntity.badRequest().body("Insufficient Balance");
    }
    
    @CrossOrigin(origins = {"http://localhost"})
	@GetMapping(path = "/accounts/logs/{accountNumber}")
	public ResponseEntity<?> transactionLogs(@PathVariable long accountNumber) {
		ArrayList<String> arr = this.accountRepository.transactionLogs(accountNumber);
		return ResponseEntity.ok().body(arr);
	}

}
