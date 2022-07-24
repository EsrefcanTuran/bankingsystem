package com.esref.bankingsystem.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class Account implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String surname;
	private String email;
	private String tc;
	private String type;
	private double balance;
	private long accountNumber;
	private long lastModified;
	
	public String toFileFormat() {
		return accountNumber + "," + name + "," + surname + "," + email + "," + tc + "," + type + "," + balance + "," + System.currentTimeMillis();
	}
	
}
