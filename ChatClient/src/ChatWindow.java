import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONObject;
import javax.swing.JList;
import javax.swing.JOptionPane;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;


public class ChatWindow extends JFrame  implements ActionListener{
	private static final long serialVersionUID = 1L;						
	private JPanel contentPane;
	private JLabel lblServerIPAddress;
	private JLabel lblClientUsername;
	private JTextArea textAreaInput;
	private JLabel lblCharacters;
	private JTextPane textPaneOnlineUsers;
	private ClientConnection client;	
	private String username;													//Declare string variable to store the username
	private String ipAddress;													//Declare string variable to store the ipAddress
	private String status;														//Declare string variable to store the status
	private int pingCounter = 0;												//Declare int variable to store the pingCounter
	private static final Integer FRAMETIME = 1000;                              //Define the frame time that use in the timer
	private Timer tickTock = new Timer(FRAMETIME,this);							//Define a timer variable and instantiate the timer class
	private StyledDocument doc;													//Define StyleDocument to manage the style on the textPane
	private javax.swing.text.Style styleRed;									//Define variables to manage the color styles
	private javax.swing.text.Style styleGray;
	private javax.swing.text.Style styleGreen;
	private JList<String> listMessage = new JList<String>();					//Declare array to store the messages
	private DefaultListModel<String> chat;										//Declare Default list model to store the chat content
	

	public void SetUsernameIP(String username_t, String ipAddress_t, ClientConnection client_t, String status_t) {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	SetUsernameIP
	//
	// Method parameters	:	String username_t, String ipAddress_t, ClientConnection client_t, String status_t
	//
	// Method return		:	void
	//
	// Synopsis				:   This method initialise the public variables of the class.  
	//
	// References			:   Johnson S.(2024) GreetingServer example. Interactive Media: Network Programming.
	//
	// Modifications		:
	//							Date			Developers				Notes
	//							----			---------				-----
	//							2024-02-20		V. Arias,               Greeting Server
	//											J. Silva,
	//											R. Useda
	//
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		username = username_t;							//Initialized public variables
		ipAddress = ipAddress_t;
		client = client_t;
		status = status_t;
		
		lblServerIPAddress.setText(ipAddress_t);		//Update value of username and IpAddress labels
		lblClientUsername.setText(username_t);
	}

	public ChatWindow() {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Class				:	ChatWindow
	//
	// Method parameters	:	none
	//
	// Method return		:	void
	//
	// Synopsis				:   This class is where the graphic interface is created for the chat window.
	//
	// References			:   Johnson S.(2024) GreetingServer example. Interactive Media: Network Programming.
	//							Oracle (2024). Interface BoundedRangeModel. 	https://docs.oracle.com/javase/8/docs/api/javax/swing/BoundedRangeModel.html
	//							Oracle (2024). Class JScrollBar. 				https://docs.oracle.com/javase/8/docs/api/javax/swing/JScrollBar.html
	//							Oracle (2024). Interface AdjustmentListener. 	https://docs.oracle.com/javase/8/docs/api/java/awt/event/AdjustmentListener.html
	//							Jan Cuypers (2001). Text line wrapping in JLabel and JList. https://groups.google.com/g/comp.lang.java.gui/c/w-2cKUrMVXs?pli=1%20-%20Jan%20Cuypers
	//							Oracle (2024). Interface ListCellRenderer<E> 	https://docs.oracle.com/javase/8/docs/api/javax/swing/ListCellRenderer.html
	//							
	//
	// Modifications		:
	//							Date			Developers				Notes
	//							----			---------				-----
	//							2024-02-20		V. Arias,               Greeting Server
	//											J. Silva,
	//											R. Useda
	//
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				status = "Offline";
				try {
					client = new ClientConnection(ipAddress);
					String infoUpdate = username+","+status;								//Change status to offline
					client.sendInformationToServer(infoUpdate);
					textPaneOnlineUsers.setText("");
					JOptionPane.showMessageDialog(contentPane, "Closing the chat session");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		tickTock.start();
		setTitle("Strawberry Chat ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 530, 600);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(95, 95, 95));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblEnterIPAddress = new JLabel("IP ADDRESS: ");
		lblEnterIPAddress.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEnterIPAddress.setForeground(Color.LIGHT_GRAY);
		lblEnterIPAddress.setBounds(271, 22, 105, 14);
		contentPane.add(lblEnterIPAddress);
		
		JLabel lblUsername = new JLabel("USERNAME: ");
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblUsername.setForeground(Color.LIGHT_GRAY);
		lblUsername.setBounds(35, 22, 83, 14);
		contentPane.add(lblUsername);
		
		JLabel lblChar = new JLabel("Characters:");
		lblChar.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblChar.setForeground(Color.LIGHT_GRAY);
		lblChar.setBounds(35, 515, 70, 14);
		contentPane.add(lblChar);
		
		lblCharacters = new JLabel("");
		lblCharacters.setForeground(Color.LIGHT_GRAY);
		lblCharacters.setBounds(120, 515, 60, 14);
		contentPane.add(lblCharacters);

		lblClientUsername = new JLabel();
		lblClientUsername.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblClientUsername.setForeground(Color.WHITE);
		lblClientUsername.setBounds(125, 20, 125, 14);
		contentPane.add(lblClientUsername);
		
		lblServerIPAddress = new JLabel();
		lblServerIPAddress.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblServerIPAddress.setForeground(Color.WHITE);
		lblServerIPAddress.setHorizontalAlignment(SwingConstants.CENTER);
		lblServerIPAddress.setBounds(366, 22, 134, 14);
		contentPane.add(lblServerIPAddress);

		JButton btnSend = new JButton("SEND");
		btnSend.setForeground(new Color(0, 0, 0));
		btnSend.setBackground(new Color(50, 205, 50));
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int maxNum = 60;																//set the max number of characters to 90
				int charSize = textAreaInput.getText().length();
				if(charSize > 0 && charSize < maxNum + 1)		//Send the text to the chat area only if the user typed something
					sendChat();
			
			}
		});
		btnSend.setBounds(393, 455, 89, 49);
		contentPane.add(btnSend);
		
		textAreaInput= new JTextArea();
		textAreaInput.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textAreaInput.setForeground(new Color(0, 0, 0));
		textAreaInput.setBackground(Color.LIGHT_GRAY);
		textAreaInput.setLineWrap(true);
		textAreaInput.requestFocusInWindow();
		textAreaInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			    int maxNum = 60;																//set the max number of characters to 90
				int charSize = textAreaInput.getText().length();								//get the number of characters in the text area
				lblCharacters.setText(String.valueOf(maxNum-charSize));							//update the label to show the number of characters
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER && charSize> 0 && charSize < maxNum + 1) //only send if the the character size if between 0 to 89
					sendChat();	
				else if(charSize >= maxNum)
				{
					String charText = textAreaInput.getText();									//get the content of the text Area input
					textAreaInput.setText(charText.substring(0, maxNum - 1));					//remove the last char if the number of characters is above the maxNum
					
				}
			}
			@Override
		    public void keyReleased(KeyEvent e) {
				int charSize = textAreaInput.getText().length();		//check that there is no a prohibited character
				if (e.getKeyChar() == '\\') 							//get the number of characters in the text area
				{
					String charText = textAreaInput.getText();			//get the content of the text Area input
					charText = charText.substring(0, charSize-1);
					textAreaInput.setText(charText);					//remove the last char if the number of characters is above the maxNum
					
				}
		        if(e.getKeyCode() == KeyEvent.VK_ENTER){
		        	textAreaInput.setText("");							//reset the textAreaInput 																
		        }
		        int maxNum = 60;										//set the max number of characters to 90
				lblCharacters.setText(String.valueOf(maxNum-charSize));	
		        
		    }
		});
		textAreaInput.setBounds(96, 464, 5, 22);
	
		JScrollPane scrollPaneTextInput = new JScrollPane(textAreaInput);
		scrollPaneTextInput.setBounds(35, 455, 328, 49);
		scrollPaneTextInput.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(scrollPaneTextInput);
		
		textPaneOnlineUsers = new JTextPane();
		textPaneOnlineUsers.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textPaneOnlineUsers.setBackground(new Color(50, 50, 50));
		textPaneOnlineUsers.setEditable(false);
		textPaneOnlineUsers.setRequestFocusEnabled(false);
		textPaneOnlineUsers.setFocusable(false);
		textPaneOnlineUsers.setBounds(380, 48, 120, 384);
		contentPane.add(textPaneOnlineUsers);
		doc = textPaneOnlineUsers.getStyledDocument(); 								//Fetches the model associated with the editor.
		 // Style for red text
        styleRed = (javax.swing.text.Style) textPaneOnlineUsers.addStyle("", null); //Adds a new style into the logical style hierarchy. Style attributes resolve from bottom up so an attribute specified in a child will override an attribute specified in the parent.
        StyleConstants.setForeground(styleRed, Color.RED); 
        StyleConstants.setFontSize(styleRed, 14);

        // Style for blue text
        styleGray = (javax.swing.text.Style) textPaneOnlineUsers.addStyle("", null);
        StyleConstants.setForeground(styleGray, Color.LIGHT_GRAY); 
        StyleConstants.setFontSize(styleGray, 14);

        // Style for green text
        styleGreen = (javax.swing.text.Style) textPaneOnlineUsers.addStyle("", null);
        StyleConstants.setForeground(styleGreen, Color.GREEN);
        StyleConstants.setFontSize(styleGreen, 14);
		
        JScrollPane scrollPaneOnlineUsers = new JScrollPane(textPaneOnlineUsers);
        scrollPaneOnlineUsers.setBounds(380, 48, 120, 384);
        scrollPaneOnlineUsers.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(scrollPaneOnlineUsers);
        
		//two columns with two text panes for the display of each message
		listMessage.setBounds(35, 46, 328, 386);
		listMessage.setCellRenderer(new DefaultListCellRenderer() {
		    @Override
		    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		    	JTextArea listCell = new JTextArea(value.toString());
		    	listCell.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		    	listCell.setForeground(Color.WHITE);
		    	listCell.setBackground(new Color(50, 50, 50));
		        listCell.setLineWrap(true);
		        listCell.setWrapStyleWord(true);
		        return listCell;
		    }
		});
		listMessage.setBackground(new Color(50, 50, 50));
		contentPane.add(listMessage);
		
		JScrollPane scrollPaneText = new JScrollPane(listMessage);
		scrollPaneText.setBounds(35, 46, 328, 386);
		scrollPaneText.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		//Scroll bar adjustment
		scrollPaneText.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {								//Create the scroll pane 
		    BoundedRangeModel scrollPaneModel = scrollPaneText.getVerticalScrollBar().getModel();
		    boolean atBottom = (scrollPaneModel.getValue() + scrollPaneModel.getExtent() == scrollPaneModel.getMaximum());		
		    @Override
		    public void adjustmentValueChanged(AdjustmentEvent e) {
		        if (!scrollPaneModel.getValueIsAdjusting()) {
		        	if (atBottom) 
		        		scrollPaneModel.setValue(scrollPaneModel.getMaximum());
		            else 
		                atBottom = (scrollPaneModel.getValue() + scrollPaneModel.getExtent() == scrollPaneModel.getMaximum());
		        }  
		    }
		});
		contentPane.add(scrollPaneText);
	}

	//Action performance that is managed by the timer event
	public void actionPerformed(ActionEvent e) {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	actionPerformed
	//
	// Method parameters	:	ActionEvent e
	//
	// Method return		:	void
	//
	// Synopsis				:   This method is managed by the timer event. All the automatic updated with the server are done here.
	//
	// References			:   Johnson S.(2024) GreetingServer example. Interactive Media: Network Programming.
	// 							Oracle (2024). Class JTextPane. 		https://docs.oracle.com/javase/8/docs/api/javax/swing/JTextPane.html#JTextPane-javax.swing.text.StyledDocument-
	//							Oracle (2024). Java JTextPane. 			https://www.geeksforgeeks.org/java-jtextpane/
	//							
	//
	// Modifications		:
	//							Date			Developers				Notes
	//							----			---------				-----
	//							2024-02-20		V. Arias,               Greeting Server
	//											J. Silva,
	//											R. Useda
	//
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=		
		
		int maxNum = 60;																//set the max number of characters to 90
		int charSize = textAreaInput.getText().length();								//get the number of characters in the text area
		lblCharacters.setText(String.valueOf(maxNum-charSize));	
		
		if(charSize >= maxNum)
		{
			String charText = textAreaInput.getText();									//get the content of the text Area input
			textAreaInput.setText(charText.substring(0, maxNum - 1));					//remove the last char if the number of characters is above the maxNum
			
		}
		
		try {
				pingCounter++;														//control the time that has passed since the last time that sent a chat
				if(pingCounter > 10 && status == "Online") 							//change status to idle if the user is not sending text
					status = "Idle";
				client = new ClientConnection(ipAddress);							//Create a new client 
				String infoUpdate = username+","+status;							//Declare string to store the name of the user with the status
				client.sendInformationToServer(infoUpdate);							//sent to the server the user name and status to update the status on the server
				
				if(status != "Offline")
				{
					ArrayList<String> usersSession = client.getUserStatus();		//get the users on session to update the list of the users
					//String panelStatusData = "";
					//clear text panel 
					textPaneOnlineUsers.setText("");
					
					for(short counter = 0; counter < usersSession.size(); counter++)	//set colors to each user based on the status
					{
						String[] status = usersSession.get(counter).split("-", 2);
						if(status[1].trim().equals("Idle"))								//idle is gray
							doc.insertString(doc.getLength(), usersSession.get(counter).toString(), styleGray);
						else if(status[1].trim().equals("Online"))						//online is green
							doc.insertString(doc.getLength(), usersSession.get(counter).toString(), styleGreen);
						else if(status[1].trim().equals("Offline"))						//offline is red
							doc.insertString(doc.getLength(), usersSession.get(counter).toString(), styleRed);
					}
				}
				client = new ClientConnection(ipAddress);						
				client.sendInformationToServer("U$%");							//send update to the server to confirm that the client is connected
				chat = client.getChatStrings();									//Update the chat screen
				listMessage.setModel(chat);										
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
	}
	
	public void sendChat() {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	sendChat
	//
	// Method parameters	:	none
	//
	// Method return		:	void
	//
	// Synopsis				:   This method sends the message typed by the user to the server to store it with the username so, on the next update 
	//							it will be displayed on the chat screen.
	//
	// References			:   Johnson S.(2024) GreetingServer example. Interactive Media: Network Programming.
	//							Oracle (2024). How to remove the blank lines at the end of the JTextArea. 
	//								https://forums.oracle.com/ords/apexds/post/how-to-remove-the-blank-lines-at-the-end-of-the-jtextarea-0949
	//							Geek for geeks (ND). Working with JSON Data in Java 	
	//								https://www.geeksforgeeks.org/working-with-json-data-in-java/
	//
	// Modifications		:
	//							Date			Developers				Notes
	//							----			---------				-----
	//							2024-02-20		V. Arias,               Greeting Server
	//											J. Silva,
	//											R. Useda
	//
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		String chatInput;												//Declare string to store the chat input username and message
		String chatInputString;											//Declare string to store the Json object text
		
		try {
			pingCounter = 0;											//reset the ping counter if the user send a message
			status = "Online";										
			client = new ClientConnection(ipAddress);					//CReate new client to send information to the server
			
			chatInput = textAreaInput.getText().trim();					//get the text from the text area
			
			JSONObject chatInputJson = 									//CReate JSon object to send to the server
					new JSONObject("{ \"username\" : \"" + username + "\" , \"message\" : \"" + chatInput +"\"}");
		
			chatInputString = String.valueOf(chatInputJson);			//JSon object stored in a string variable
			
			client.sendInformationToServer(chatInputString);			//send the JSon object converted to string
			
			chat = client.getChatStrings();								//update the chat screen 
			listMessage.setModel(chat);     
			//empty chat input area
			textAreaInput.setText("");									//clean the text area after updating the chat screen
		} catch (IOException e1) {
		// TODO Auto-generated catch block
		//e1.printStackTrace();
			System.out.println("Error parsing JSON - sendChat method");
	}	
		//count number of characters
		lblCharacters.setText(String.valueOf("60"));					//update the label of the max number of characters
	}
}
