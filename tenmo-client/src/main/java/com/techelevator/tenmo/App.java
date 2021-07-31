package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferModel;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.view.ConsoleService;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.Map;

public class App {

	private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		AccountService accountService = new AccountService(API_BASE_URL, currentUser);
		BigDecimal balance;
		try {
			balance = accountService.getBalance();
			console.displayBalance(balance);
		} catch (RestClientException re) {
			System.out.println("No balance to view.");
		} catch (Exception e) {
			System.out.println("No accessible funds in account");
		}

		
	}

	private void viewTransferHistory() {
		TransferService transferService = new TransferService(API_BASE_URL, currentUser);
		AccountService accountService = new AccountService(API_BASE_URL, currentUser);

		TransferModel[] transfers;

		try {
			transfers = transferService.listTransfers();
			Long user_account_id = accountService.getAccountIdFromUser(currentUser.getUser().getId());
			console.displayTransfers(transfers, user_account_id);
			System.out.println();
		} catch (RestClientException re) {
			System.out.println("Issue with the Rest API");
		} catch (Exception e) {
			System.out.println("Issue with transfer history in App class");
		}

		// prompt user if they want to see transfer details
		while (true) {

			String userInput = console.getUserInput("Enter ID of transfer for details (X to return to menu)");

			if (userInput.equalsIgnoreCase("X")) return;

			Long transfer_id;

			try {
				transfer_id = Long.parseLong(userInput);
			} catch (NumberFormatException ne) {
				System.out.println("Please enter valid ID.");
				continue;
			} catch (Exception e) {
				System.out.println("An error occured");
				continue;
			}

			TransferModel transfer = transferService.getTransferDetails(transfer_id);

			if (transfer == null) {
				System.out.println("Please enter valid ID");
				continue;
			}

			console.transferDetails(transfer);

		}
	}

	private void viewPendingRequests() {
		TransferService transferService = new TransferService(API_BASE_URL, currentUser);
		AccountService accountService = new AccountService(API_BASE_URL, currentUser);

		TransferModel[] transfers;

		Long user_account_id = null;

		try {
			transfers = transferService.listPendingTransfers();
			user_account_id = accountService.getAccountIdFromUser(currentUser.getUser().getId());
			console.displayPendingTransfers(transfers, user_account_id);
			System.out.println();
		} catch (RestClientException re) {
			System.out.println("Issue with the Rest API");
		} catch (Exception e) {
			System.out.println("Issue with transfer history in App class");
		}

		// prompt user if they want to see transfer details
		while (true) {

			String userInput = console.getUserInput("Enter ID of transfer for details, and to accept/reject. \n" +
					"Enter X to return to menu");

			if (userInput.equalsIgnoreCase("X")) return;

			Long transfer_id;

			try {
				transfer_id = Long.parseLong(userInput);
			} catch (NumberFormatException ne) {
				System.out.println("Please enter valid ID.");
				continue;
			} catch (Exception e) {
				System.out.println("An error occured");
				continue;
			}

			TransferModel transfer = transferService.getTransferDetails(transfer_id);

			if (transfer == null) {
				System.out.println("Please enter valid ID");
				continue;
			}

			console.transferDetails(transfer);

			// give user option to approve transfer if transfer is to them
			// if the account_from matches user account_id, they can approve

			if (transfer.getSender_account().equals(user_account_id)) {

				while (true) {
					String userChoice = console.getUserInput("Would you like to (a)pprove, (r)eject this request, or e(x)it?");

					if (userChoice.equalsIgnoreCase("X")) return;

					else if (userChoice.equalsIgnoreCase("A")) {
						if (transferService.approveRequest(transfer)) {
							System.out.println("Approved");
							return;
						} else {
							System.out.println("You don't have the appropriate funds.");
							return;
						}
					}

					else if (userChoice.equalsIgnoreCase("R")) {
						if (transferService.rejectRequest(transfer)) {
							System.out.println("Rejected");
							return;
						} else {
							System.out.println("An error occured");
							return;
						}

					}

					else {
						System.out.println("Please enter a, r, or x");
						System.out.println();
						continue;
					}

				}

			}


		}


		
	}

	private void sendBucks() {
		TransferService transferService = new TransferService(API_BASE_URL, currentUser);
		AccountService accountService = new AccountService(API_BASE_URL, currentUser);

		console.displayUsers(accountService.getUsers(), currentUser);
		System.out.println();

		while (true) {
			TransferModel transfer = collectSendTransferInformation();
			if (transfer == null) return;

			if (transferService.sendBucks(transfer)) {
				System.out.println("Success \n");
				break;
			} else System.out.println("Failed \n");
		}

	}

	private void requestBucks() {
		TransferService transferService = new TransferService(API_BASE_URL, currentUser);
		AccountService accountService = new AccountService(API_BASE_URL, currentUser);

		console.displayUsers(accountService.getUsers(), currentUser);
		System.out.println();

		while (true) {
			TransferModel transfer = collectRequestTransferInformation();
			if (transfer == null) return;

			if (transferService.requestBucks(transfer)) {
				System.out.println("Success \n");
				break;
			} else System.out.println("Failed \n");
		}
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}

	// helper method for sendBucks()
	private TransferModel collectSendTransferInformation() {
		AccountService accountService = new AccountService(API_BASE_URL, currentUser);

		TransferModel transfer = new TransferModel();

		transfer.setTransfer_status_id(2L); // 2 represents approved status
		transfer.setTransfer_type_id(2L); // 2 represents sent type



		while (true) {
			String userToSendTo = console.getUserInput("Enter User ID to send to (Enter X to quit screen)");
			System.out.println();
			try {

				// give the user the option to leave this menu if they don't want to send money
				if (userToSendTo.equalsIgnoreCase("X")) return null;

				transfer.setSender_account(accountService.getAccountIdFromUser(currentUser.getUser().getId()));
				transfer.setReceiver_account(accountService.getAccountIdFromUser(Long.parseLong(userToSendTo)));
				if (userToSendTo.equals(currentUser.getUser().getId().toString())) {
					System.out.println("Cannot send money to self \n");
					continue;
				}
				break;
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}

		while (true) {
			String amount = console.getUserInput("Enter amount to send");
			System.out.println();

			try {
				BigDecimal amountToSend = BigDecimal.valueOf(Double.parseDouble(amount));
				transfer.setAmount(amountToSend);
				break;
			} catch (Exception e) {
				System.out.println("Please enter a money amount (ex. 10.00)\n");

				continue;
			}
		}

		return transfer;

	}

	private TransferModel collectRequestTransferInformation() {
		AccountService accountService = new AccountService(API_BASE_URL, currentUser);

		TransferModel transfer = new TransferModel();

		transfer.setTransfer_status_id(1L); // 1 represents pending status
		transfer.setTransfer_type_id(1L); // 1 represents request type



		while (true) {
			String otherUser = console.getUserInput("Enter User ID to request from (Enter X to quit screen)");
			System.out.println();
			try {

				// give the user the option to leave this menu if they don't want to send money
				if (otherUser.equalsIgnoreCase("X")) return null;

				transfer.setReceiver_account(accountService.getAccountIdFromUser(currentUser.getUser().getId()));
				transfer.setSender_account(accountService.getAccountIdFromUser(Long.parseLong(otherUser)));
				if (otherUser.equals(currentUser.getUser().getId().toString())) {
					System.out.println("Cannot request money from self \n");
					continue;
				}
				break;
			} catch (Exception e) {
				System.out.println("Please enter valid ID \n");
				continue;
			}
		}

		while (true) {
			String amount = console.getUserInput("Enter amount to request");
			System.out.println();

			try {
				BigDecimal amountToSend = BigDecimal.valueOf(Double.parseDouble(amount));
				transfer.setAmount(amountToSend);
				break;
			} catch (Exception e) {
				System.out.println("Please enter a money amount (ex. 10.00)\n");

				continue;
			}
		}


		return transfer;

	}
}
