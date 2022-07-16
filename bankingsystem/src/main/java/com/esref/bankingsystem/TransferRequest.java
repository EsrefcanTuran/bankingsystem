package com.esref.bankingsystem;

import lombok.Data;

@Data
public class TransferRequest {
	
	private long transferredAccountNumber;
	private double amount;

}
