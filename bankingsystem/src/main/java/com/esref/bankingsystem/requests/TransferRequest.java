package com.esref.bankingsystem.requests;

import lombok.Data;

@Data
public class TransferRequest {
	
	private long transferredAccountNumber;
	private double amount;

}
