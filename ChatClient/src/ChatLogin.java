import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;

public class ChatLogin extends JFrame {

	private JPanel contentPane;											// declare the content panel for the application  
	private JTextField textFieldIPAddress;								// declare the text field to receive the ip address
	private JTextField textFieldUsername;								// declare the text field to receive the username
	private ClientConnection client;									// declare a client to handle the server connection
	private ChatWindow chat;											// declare a chat window instance to change screens

	public static void main(String[] args) {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	main
	//
	// Method parameters	:	String[] args
	//
	// Method return		:	void
	//
	// Synopsis				:   This is the main method of the application, it instantiates the frame and sets it 
	//							to visible.
	//
	// References			:   Johnson S.(2024) GreetingServer example. Interactive Media: Network Programming.
	//
	// Modifications		:
	//							Date			Developers				Notes
	//							----			---------				-----
	//							2024-02-20		V. Arias,               Main
	//											J. Silva,
	//											R. Useda
	//
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatLogin frame = new ChatLogin();					// instantiate the chat login frame 
					frame.setVisible(true);								// set the application frame to visible
				} catch (Exception e) {
					e.printStackTrace();								// if there is an error, print the stack trace
				}
			}
		});
	}

	public ChatLogin() {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	ChatLogin
	//
	// Method parameters	:	none
	//
	// Method return		:	void
	//
	// Synopsis				:   This method handles the graphic elements of the application interface.
	//
	// References			:   none
	//
	// Modifications		:
	//							Date			Developers				Notes
	//							----			---------				-----
	//							2024-02-20		V. Arias,               Chat Login
	//											J. Silva,
	//											R. Useda
	//
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);						// define the settings of the content panel
		setBounds(100, 100, 450, 340);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(95, 95, 95));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblIPAddress = new JLabel("IP ADDRESS: ");					// declare and instantiate the ip address label
		lblIPAddress.setFont(new Font("Tahoma", Font.BOLD, 12));			// define the settings of the label
		lblIPAddress.setForeground(Color.WHITE);
		lblIPAddress.setBounds(73, 121, 112, 14);
		contentPane.add(lblIPAddress);
		
		JLabel lblUsername = new JLabel("USERNAME: ");						// declare and instantiate the username label
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 12));				// define the settings of the label
		lblUsername.setForeground(Color.WHITE);
		lblUsername.setBounds(73, 167, 106, 14);
		contentPane.add(lblUsername);
		
		JLabel lblChatTitle = new JLabel("Strawberry Chat");				// declare and instantiate the chat title label
		lblChatTitle.setFont(new Font("Tahoma", Font.BOLD, 18));			// define the settings of the label
		lblChatTitle.setForeground(Color.WHITE);
		lblChatTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblChatTitle.setBounds(178, 23, 164, 85);
		contentPane.add(lblChatTitle);
		
		JLabel lblInstructions = new JLabel("Please enter the server IP Address and a username with letters ");				// declare and instantiate the instructions label
		lblInstructions.setHorizontalAlignment(SwingConstants.CENTER);														// define the settings of the label
		lblInstructions.setForeground(Color.LIGHT_GRAY);
		lblInstructions.setBounds(10, 258, 414, 14);
		contentPane.add(lblInstructions);
		
		JLabel lblInstructionsContinued = new JLabel("and/or numbers that contains from 3 to 6 characters.");				// declare and instantiate the instructions continued label
		lblInstructionsContinued.setHorizontalAlignment(SwingConstants.CENTER);												// define the settings of the label
		lblInstructionsContinued.setForeground(Color.LIGHT_GRAY);
		lblInstructionsContinued.setBounds(10, 276, 414, 14);
		contentPane.add(lblInstructionsContinued);
		
		JLabel lblChatLogo = new JLabel("");																				// declare and instantiate the chat logo label
		lblChatLogo.setIcon(new ImageIcon(ChatLogin.class.getResource("/Media/Logo.png")));									// define the settings of the label
		lblChatLogo.setBounds(94, 24, 70, 70);
		contentPane.add(lblChatLogo);
		
		textFieldIPAddress = new JTextField();																				// declare and instantiate the IP Address text field 
		textFieldIPAddress.setFont(new Font("Tahoma", Font.BOLD, 12));														// define the settings of the text field 
		textFieldIPAddress.setBackground(Color.LIGHT_GRAY);
		textFieldIPAddress.setForeground(Color.BLACK);
		textFieldIPAddress.setBounds(189, 118, 169, 20);
		contentPane.add(textFieldIPAddress);
		textFieldIPAddress.setColumns(10);
		
		textFieldUsername = new JTextField();																				// declare and instantiate the username text field
		textFieldUsername.setFont(new Font("Tahoma", Font.BOLD, 12));														// define the settings of the text field 
		textFieldUsername.setForeground(Color.BLACK);
		textFieldUsername.setBackground(Color.LIGHT_GRAY);
		textFieldUsername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					enterAction();																							// detect the pressing of the enter key and call 
																															// the enter action method
			}
		});
		textFieldUsername.setBounds(189, 164, 169, 20);
		contentPane.add(textFieldUsername);
		textFieldUsername.setColumns(10);
		
		JButton btnEnter = new JButton("Enter Chat");																		// declare and instantiate the enter chat button
		btnEnter.setFont(new Font("Tahoma", Font.BOLD, 11));																// define the settings of the text field
		btnEnter.setBackground(new Color(50, 205, 50));
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enterAction();																								// if the button is pressed, call the enter action method
			}
		});
		btnEnter.setBounds(158, 213, 106, 23);
		contentPane.add(btnEnter);		
	}
	
	public boolean isValidInput(String input, String pattern) {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	isValidInput
	//
	// Method parameters	:	input, pattern
	//
	// Method return		:	boolean
	//
	// Synopsis				:   This method compares the input given to a pattern to determine if they match.
	//
	// References			:   Ilakkuvaselvi M. (2023) Regex to validate IP address - Java. Medium.
	//							https://medium.com/@ilakk2023/regex-to-validate-ip-address-java-d56d450d679c#:~:text=In%20the%20case%20of%20the,%5C%5C							
	//
	// Modifications		:
	//							Date			Developers				Notes
	//							----			---------				-----
	//							2024-02-20		V. Arias,               Is Valid Input
	//											J. Silva,
	//											R. Useda
	//
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	    Pattern ipPattern = Pattern.compile(pattern);							// declare and set the pattern to the parameter passed
	    Matcher matcher = ipPattern.matcher(input);								// declare and set the matcher to the input with the pattern
	    return matcher.matches();												// return true or false if the pattern does or does not match
	}
	
	public void enterAction(){
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	enterAction
	//
	// Method parameters	:	none
	//
	// Method return		:	void
	//
	// Synopsis				:   This method controls the login actions according to the actions performed by the user.
	//
	// References			:   Johnson S.(2024) GreetingServer example. Interactive Media: Network Programming.
	//							Ilakkuvaselvi M. (2023) Regex to validate IP address - Java. Medium.
	//							https://medium.com/@ilakk2023/regex-to-validate-ip-address-java-d56d450d679c#:~:text=In%20the%20case%20of%20the,%5C%5C
	//							Gianni S. (2011) Regular Expression in Java for validating username.
	//							https://stackoverflow.com/questions/6782437/regular-expression-in-java-for-validating-username
	//
	// Modifications		:
	//							Date			Developers				Notes
	//							----			---------				-----
	//							2024-02-20		V. Arias,               Enter Action
	//											J. Silva,
	//											R. Useda
	//
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		String ipAddress = textFieldIPAddress.getText();					// declare and set the ip address string to the text from the ip address texfield
		String username = textFieldUsername.getText();						// declare and set the username string to the text from the username texfield
		String patternIp = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
		String patternUser = "[a-zA-Z0-9\\._\\-]{3,6}";						// declare and set the pattern the ip and the username must follow
		byte optionLogin;													// declare a byte to store the type of login action to be performed
		int option; 														// declare an integer to store the restoring session option chosen
		
		if(isValidInput(ipAddress,patternIp) && isValidInput(username,patternUser)) {
			try {
				client = new ClientConnection(ipAddress);															// instantiate a client connection to the server with the given ip address
				optionLogin = client.Login(username,ipAddress);														// call the login method from the client and store the option returned
				if(optionLogin == 0) {																				// if the option is zero, it's a new user
					dispose();																						// dispose of the chat login window
					chat = new ChatWindow();																		// instantiate the chat window
					chat.setVisible(true);																			// set the chat window to visible
					chat.SetUsernameIP(username, ipAddress, client, "Online");										// call the setUsernameIP method to set the initial status of the client
				}
				else if(optionLogin == 1){
					option = JOptionPane.showConfirmDialog(null, "Do you want to resume your session?");			// store the option chosen from the resume session option pane
					if(option == JOptionPane.YES_OPTION) {
						dispose();																					// dispose of the chat login window
						chat = new ChatWindow();																	// instantiate the chat window
						chat.setVisible(true);																		// set the chat window to visible
						chat.SetUsernameIP(username, ipAddress, client, "Online");									// call the setUsernameIP method to set the initial status of the client
					}
				}
				else{
					JOptionPane.showMessageDialog(null,"Username is already taken.");								// otherwise if the user is taken, display a message
				};
			} catch (IOException e1) {
				e1.printStackTrace();																				// print the stack trace
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "Please enter a valid Username and IP Address.");					// if the input is not valid, display an error message
		}
	}
}
