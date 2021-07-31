package com.techelevator.view;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferModel;
import com.techelevator.tenmo.model.User;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Map;
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
		String printBalance = NumberFormat.getCurrencyInstance().format(balance);
		System.out.println("Your current account balance is : " + printBalance);
	}

	public void displayUsers(User[] users, AuthenticatedUser currentUser) {
		System.out.println("-------------------------------------------");
		System.out.println("Users");
		System.out.printf("%-10s", "ID");
		System.out.println("Name");
		System.out.println("-------------------------------------------");
		for (User user : users) {
			if (user.getUsername().equals(currentUser.getUser().getUsername())) continue;
			// formatting left aligns and pads to the right
			System.out.printf("%-10s", user.getId().toString());
			System.out.println(user.getUsername());
		}
	}

	public void displayTransfers(TransferModel[] transfers, Long user_account_id) {
		//get account from ID
		//get account to ID
		//get amount
		//get transfer ID

		System.out.println("-------------------------------------------");
		System.out.println("Transfers");
		System.out.printf("%-10s", "ID");
		System.out.printf("%-15s", "From/To");
		System.out.println("Amount");
		System.out.println("-------------------------------------------");
		
		for (TransferModel transfer : transfers) {

			// don't display pending transfers
			if (transfer.getTransfer_status_id() == 1 || transfer.getTransfer_status_id() == 3) { // 1 representing pending
				continue;
			}

			Long transferId = transfer.getTransfer_id();
			Long senderId = transfer.getSender_account(); // account id
			Long receiverId = transfer.getReceiver_account(); // account id
			String senderName = transfer.getSendername();
			String receiverName = transfer.getReceivername();

			String amount = NumberFormat.getCurrencyInstance().format(transfer.getAmount());
			//BigDecimal amount = transfer.getAmount();

			// check if user is sender or receiver
			// print only the other user involved

			/*
			formatting the print
			%-12s specifies left alignment and padding
			%n specifies newline
			 */
			if (transfer.getSender_account().equals(user_account_id)) {
				System.out.printf("%-10s", transferId);
				System.out.printf("%-15s", "To: " + receiverName);
				System.out.printf("%-15s%n", amount);
			} else if (transfer.getSender_account().equals(user_account_id)) {
				System.out.printf("%-10s", transferId);
				System.out.printf("%-15s", "From: " + senderName);
				System.out.printf("%-15s%n", amount);
			} else System.out.println("Issue in ConsoleService displayTransfers method.");




		}



	}

	public void displayPendingTransfers(TransferModel[] transfers, Long user_account_id) {
		//get account from ID
		//get account to ID
		//get amount
		//get transfer ID

		System.out.println("-------------------------------------------");
		System.out.println("Transfers");
		System.out.printf("%-10s", "ID");
		System.out.printf("%-15s", "From/To");
		System.out.println("Amount");
		System.out.println("-------------------------------------------");

		for (TransferModel transfer : transfers) {

			Long transferId = transfer.getTransfer_id();
			Long senderId = transfer.getSender_account(); // account id
			Long receiverId = transfer.getReceiver_account(); // account id
			String senderName = transfer.getSendername();
			String receiverName = transfer.getReceivername();

			String amount = NumberFormat.getCurrencyInstance().format(transfer.getAmount());
			//BigDecimal amount = transfer.getAmount();

			// check if user is sender or receiver
			// print only the other user involved

			/*
			formatting the print
			%-12s specifies left alignment and padding
			%n specifies newline
			 */
			if (transfer.getSender_account().equals(user_account_id)) {
				System.out.printf("%-10s", transferId);
				System.out.printf("%-15s", "To: " + receiverName);
				System.out.printf("%-15s%n", amount);
			} else if (transfer.getReceiver_account().equals(user_account_id)) {
				System.out.printf("%-10s", transferId);
				System.out.printf("%-15s", "From: " + senderName);
				System.out.printf("%-15s%n", amount);
			} else System.out.println("Issue in ConsoleService displayTransfers method.");




		}
	}



	public void transferDetails(TransferModel transfer) {

		Long transferId = transfer.getTransfer_id();
		String senderName = transfer.getSendername(); // not sure why these needs to be a string
		String receiverName = transfer.getReceivername();
		String amount = NumberFormat.getCurrencyInstance().format(transfer.getAmount()); // formats with $ and two point digits

		// Transfer type and status need to be strings, based on id\
		String transferType;
		String transferStatus;

		if (transfer.getTransfer_type_id() == 1L) transferType = "Request";
		else if (transfer.getTransfer_type_id() == 2L) transferType = "Send";
		else transferType = "Error";

		if (transfer.getTransfer_status_id() == 1L) transferStatus = "Pending";
		else if (transfer.getTransfer_status_id() == 2L) transferStatus = "Approved";
		else if (transfer.getTransfer_status_id() == 3L) transferStatus = "Rejected";
		else transferStatus = "Error";


		System.out.println("-------------------------------------------");
		System.out.println("Transfer Details");
		System.out.println("-------------------------------------------");


		System.out.println("ID: " + transferId + "\nFrom: " + senderName + "\nTo: " + receiverName + "\nType: " + transferType + "\nStatus: " + transferStatus + "\nAmount: " + amount);

	}

}
