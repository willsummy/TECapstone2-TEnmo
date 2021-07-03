package com.techelevator.view;


import com.techelevator.tenmo.model.TransferModel;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.TransferService;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;

	public ConsoleService(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}

	public String getUserInput(String prompt) {
		out.print(prompt+": ");
		out.flush();
		return in.nextLine();
	}

	public Integer getUserInputInteger(String prompt) {
		Integer result = null;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}

	public void displayBalance(BigDecimal balance) {
		System.out.println("Your current account balance is : $" + balance);
	}



	public void displayUsers(List<User> users) {
		System.out.println("-------------------------------------------");
		System.out.println("Users");
		System.out.println("ID    Name");
		System.out.println("-------------------------------------------");
		for (User user : users) {
			System.out.println(user.getId() + "    " + user.getUsername());
		}
	}

	public void displayTransfers(TransferModel[] transfers, String senderName, String receiverName) {
		//get account from ID
		//get account to ID
		//get amount
		//get transfer ID

		//System.out.println("-------------------------------------------");
		//System.out.println("Transfers");


		for (TransferModel transfer : transfers) {
			Long transferId = transfer.getTransfer_id();
			Long sender = transfer.getAccount_from();
			Long receiver = transfer.getAccount_to();

			BigDecimal amount = transfer.getAmount();
			System.out.println("ID: " + transferId + " " + "From: " + senderName + " " + "To: " + receiverName + " " + amount);

		}



	}

	public void transferDetails(TransferModel, String senderName, String receiverName) {
		for (TransferModel transfer : transfers) {
			Long transferId = transfer.getTransfer_id();
			Long sender = transfer.getAccount_from();
			Long receiver = transfer.getAccount_to();
			Long transferType = transfer.getTransfer_type_id();
			Long transferStatus = transfer.getTransfer_status_id();
			BigDecimal amount = transfer.getAmount();

			System.out.println("ID: " + transferId + " " + "From: " + senderName + " " + "To: " + receiverName + " " + "Type: " + transferType + " " + "Status: " + transferStatus + " " + "Amount: " + amount);
		}
	}
}
