import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class ClientConnection {
	private int port = 6066;															// establish the number of the port
	private Socket client;																// declare the socket for the client
	private DataOutputStream out;														// declare the data output stream object
	private DataInputStream in;															// declare the data input stream object
	private String incomingData;														// declare a string for the incoming data
	private DefaultListModel<String> chatStrings = new DefaultListModel<String>();		// Declare list model to store the chat history  
	private HashMap<String, String> usernameStatus = new HashMap<String, String>();		// Declare the hashMap to store the users and their status

	public ClientConnection(String serverName) throws IOException {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	ClientConnection(String serverName)
	//
	// Method parameters	:	String serverName
	//
	// Method return		:	void
	//
	// Synopsis				:   This method is the constructor of the client. 
	//
	// References			:   Johnson S.(2024) GreetingServer example. Interactive Media: Network Programming.
	//
	// Modifications		:
	//							Date			Developers				Notes
	//							----			---------				-----
	//							2024-02-20		V. Arias,               
	//											J. Silva,
	//											R. Useda
	//
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		try
	      {
	        client = new Socket(serverName, port);										// instantiate the socket to connect to server
	        OutputStream outToServer = client.getOutputStream();						// initialize the output stream
	        out = new DataOutputStream(outToServer);									// instantiate the data output stream
	        InputStream inFromServer = client.getInputStream(); 						// initialize the input stream
	        in = new DataInputStream(inFromServer);   									// instantiate the data input stream
	      }catch(IOException e)
	      {
	         e.printStackTrace();														// if there is an error print the stack trace
		  }
	}


	public void sendInformationToServer(String information) {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	sendInformationToServer(String information)
	//
	// Method parameters	:	String information
	//
	// Method return		:	void
	//
	// Synopsis				:   This method send information to server from client. 
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
		String substring;													// Declare substring variable to store a portion of the information 		
		String modifiedString;												// Declare a modifiedString variable
		String [] commaSeparated;											// Declare a comma separated string array 
		String [] parts;													// Declare a parts string array
		
		try
	      {
	         out.writeUTF(information); 									// send information out to the server 
	         incomingData = new String(in.readUTF());						// read the incoming drawing sequence from the server
       
	         if(incomingData.length() > 1) 									// Check if the incoming data has more than 1 character 			
	         {
	        	 substring = incomingData.substring(0, 2);					// Get the first 3 characters of the incomig data
	        	 if (substring.equals("[{")) 								// Check if the head of the chain is a json object
	        		 chatInput(incomingData);								// Chat input validation to decode the incoming data string   
	        	 else if(incomingData.substring(0, 1).equals("{"))			// In other case check if it is a JSON
	        	 {
		        		 modifiedString = incomingData.substring(1, incomingData.length() - 1);	 // Store a substring of the modified string 
		      	         commaSeparated = modifiedString.split(", ");		// Separated the modified string
		        		 
			      	       for (String item : commaSeparated) 				// Iterate through   
			      	       {
			      	            
			      	            parts = item.split("=");					// Split each item by "="	
		
			      	            if (parts.length == 2) 						// Check if the split result has 2 parts: a key and a value
			      	            {
			      	            	usernameStatus.put(parts[0], parts[1]);	// parts[0] is the key, parts[1] is the value
			      	            } 
			        	   }
	         }
	         client.close();  													// close the server connection
	         }
	      }
	      catch(Exception e)
	      {
	    	 JOptionPane.showMessageDialog(null, "No Server Connection.");		// display an error message if an exception occurs 
	         e.printStackTrace();												// and print the stack trace
	      }
	}
	
	public byte Login(String username, String ipAddress) {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	Login(String username, String ipAddress)
	//
	// Method parameters	:	String username, String ipAddress
	//
	// Method return		:	byte
	//
	// Synopsis				:   This method allow to login the user to the server. 
	//
	// References			:   Johnson S.(2024) GreetingServer example. Interactive Media: Network Programming.
	//
	// Modifications		:
	//							Date			Developers				Notes
	//							----			---------				-----
	//							2024-02-20		V. Arias,              
	//											J. Silva,
	//											R. Useda
	//
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		String incomingData;											// Declare incoming data variable
		String substring;												// Declare substring data variable
		
		try {		
			
			out.writeUTF(username);										// Use the write utf to send the instruction the server

			incomingData = in.readUTF();								// Store the data in the sequence string variable
			client.close();												// Close the client instance		

			if(incomingData.length() > 1)								// Check if the length is greater than 1 
			{
				substring = incomingData.substring(0, 2);				// Store the substring
				if (substring.equals("[{")) 							// Check if the substring is a JSON	
	        		 chatInput(incomingData);							// Pass the incoming data to the chat input method 
				return 0;												// Return 0 in this case
			}
			else  if(incomingData.equals("E"))							//In case of receive E means user is offline
				return 1;												//Return 1
			else
				return 2;												//Return 2 in case the user is used
			
																		//close the connection instance
		} catch (IOException e) {														//catch block
			JOptionPane.showMessageDialog(null,"Not connection established");			//show a dialog message if the client couldn't connect with server
		}
		return 2;														//Return in other case
	}
	
	public void chatInput(String input) {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	chatInput(String input)
	//
	// Method parameters	:	String input
	//
	// Method return		:	void
	//
	// Synopsis				:   This method decodes the JSON string input. 
	//
	// References			:   Johnson S.(2024) GreetingServer example. Interactive Media: Network Programming.
	//
	// Modifications		:
	//							Date			Developers				Notes
	//							----			---------				-----
	//							2024-02-20		V. Arias,              
	//											J. Silva,
	//											R. Useda
	//
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		 String substring;													// Declare substring variable to store a portion of the information 		
		 String [] commaSeparated;											// Declare a comma separated string array 
		 String username;													// Declare  a username variable to store the username come from JSON
		 String message;													// Declare  a message variable to store the message come from JSON
		 String chatEntry;													// Declare a variable to store the chat entry
		 int counter;														// Declare a counter for the loop			
		 						
		 try
		 {
			//JSON PARSING
		     substring = input.substring(0, 1);									// Get the first character of the input
		
		     if (substring.equals("["))											// Check if it is an array
		    	 substring = input.substring(1,input.length()-1);					// Overwrite the variable with the rest of the chain
		   	
			 commaSeparated = substring.split(", (?=\\{)");							// Separate each element of the input
		 
		     for (counter  = 0; counter < commaSeparated.length; counter++) {					// Loop through the commaSeparated array
			      
			      JSONObject jsonObjectdecode = new JSONObject(commaSeparated[counter]);		// Declarate a JSON object
			       																// Converting into Java Data type
			       																// format From Json is the step of Decoding.
			       username = (String)jsonObjectdecode.get("username");			// Store the username value of the json 
			       message = (String)jsonObjectdecode.get("message");			// Store the message value of the json
			       																// Check the long messages
			       if(message.length() > 40) 									// if its a long message, double space
			    	   chatEntry = username + ": " + message + "\n\n";			// Add a new space line at the end of the message	
			       else
			    	   chatEntry = username + ": "+message+"\n";				// In other case just one new line
		
			       chatStrings.addElement(chatEntry);							// Add the chat entry to the default model
		     }
		    
		 } catch (Exception e) {
			    System.err.println("Error parsing JSON - chatInput method ");
		 }
	}
		
	public ArrayList<String>  getUserStatus() {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	getUserStatus()
	//
	// Method parameters	:	none
	//
	// Method return		:	ArrayList<String>
	//
	// Synopsis				:   This method allows to get a list with the user status . 
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
		ArrayList<String> usersSession = new ArrayList<String>();									// Define a local  arraylist variable of users
		String tempString;																			// Declarate a temp String
		
		for (Map.Entry<String, String> usersList : usernameStatus.entrySet()) {						// Loop through the hashMap				
            tempString = usersList.getKey() + " - " + usersList.getValue()+"\n";					// Store the user information in the temp string
            usersSession.add(tempString);															// Add the tempString to the list				
        }
		return usersSession;																		// Return the arraylist
	}
	
	public DefaultListModel<String> getChatStrings() {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	getChatStrings()
	//
	// Method parameters	:	none
	//
	// Method return		:	DefaultListModel<String>
	//
	// Synopsis				:   This method allows to get the list with the chat history. 
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
		return chatStrings;																			//Return the chat history list
	}
		
}
