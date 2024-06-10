import java.net.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;
import org.json.JSONObject;

public class GreetingServer extends Thread									// Server is inherited from the Thread object
{
   private ServerSocket serverSocket;										// Declaration of the socket that the server will run on
   private ArrayList<String> users = new ArrayList<String>();			// Declaration of sequence array list to store the line directions
   private ArrayList<String> chatHistory = new ArrayList<String>();				// Declaration of extras array list to store the color and stroke of the line
   private HashMap<String, String> usernameStatus = new HashMap<String, String>();// Declaration of userNameStatus hashMap to store the users and their status
   
   public GreetingServer(int port) throws IOException
   {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	GreetingServer
	//
	// Method parameters	:	port
	//
	// Method return		:	void
	//
	// Synopsis				:   This method instantiates the server socket and sets a timeout for it. 
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
      serverSocket = new ServerSocket(port);								// Instantiates the server socket
      serverSocket.setSoTimeout(0);											// Sets the timeout for the socket. A timeout of 0 establishes
   }																		// an infinite timeout

   public void run()
   {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
				// Method				:run()
				//
				// Method parameters	: none
				//
				// Method return		: none
				//
				// Synopsis				:   This method to run the server to receive and send information to the client.
				//				
				// References			:   Johnson S.(2024) GreetingServer example. Interactive Media: Network Programming.
				//
		   		// Modifications		:
				//							Date			Developers				Notes
				//							----			---------				-----
				//							2024-02-21		Julian Silva			
				//											Valentina Arias
		   		//											Rosibel Useda
				// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
      while(true)
      {
         try
         {
            Socket server = serverSocket.accept();									// Wait for a connection from the client
            DataInputStream in =  new DataInputStream(server.getInputStream()); 	// Get the data from the client          
            DataOutputStream out =  new DataOutputStream(server.getOutputStream()); // Prepare the object for returning data to the client
            
            																		//RECEIVE USERNAME OR CHAT INPUT
            String clientInput = String.valueOf(in.readUTF());						//Read the instruction of the client									
            String substring = clientInput.substring(0, 3);							// Get a substring of the first 3 characters of the chain
            
            																		// Validate type of received input
            if (substring.equals("{\"m")) { 										// Check if the string is a message
            	chatHistory.add(clientInput);										// Add the username and message to the chat history arrayList 	
            	out.writeUTF(chatHistory.toString());								// Send the chat history to the client
            }
            else {
            																		//VALIDATE LOGIN
            	String[] dataReceived = clientInput.split(",", 2);					// Store the client input in a String array 
            	if(dataReceived.length > 1)											// Check if a user is trying to login
            	{
            		usernameStatus.put(dataReceived[0].toString(), dataReceived[1].toString()); // Add to the hash map the username and their status 

                	out.writeUTF(usernameStatus.toString());						// Sent the usernameStatus hashMap to the client	
            	}
            	else  																// Check if the client sent an update
            		if(!clientInput.equals("U$%")) {								
            		char validation = validateUsername(clientInput);				// Validate the user depends of the status	
            		
                    if(validation == 'G')											// If validation is equal G means is a new user in the chat
                    	out.writeUTF(chatHistory.toString());						// Send the chat history to the client
                    else if(validation == 'E') 										// If validation is equal E means that the user has been logged but can re enter to the chat	
                    	out.writeUTF("E"); 											// Sent E to the client
                    else															// In other case the user is currently online 
                    	out.writeUTF("O"); 											// Sent O to the client
            	}
            	else {
            		out.writeUTF(chatHistory.toString());							// In other cases update the chat history
            	}	
            }
            server.close();															// Shut down the server
         
         }catch(SocketTimeoutException s)
         {
            System.out.println("Socket timed out!");
            break;
         }catch(IOException e)
         {
            e.printStackTrace();
            break;
         }
      }
   }
   
   public char validateUsername(String username)
   {
	   // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
				// Method				:validateUsername(String username)
				//
				// Method parameters	: String username
				//
				// Method return		: char
				//
				// Synopsis				: This method check the user status and add to the list or return a character depends of the status.
				//				
				// References			:   Johnson S.(2024) GreetingServer example. Interactive Media: Network Programming.
				//
			   	// Modifications		:
				//							Date			Developer				Notes
				//							----			---------				-----
				//							2024-02-21		Julian Silva			
				//											Valentina Arias
		  		//											Rosibel Useda
				// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
       if(! usernameStatus.containsKey(username)) {									// Check if the user is not registered on the hashMap
       	users.add(username);														// Add the user to the arraylist users	
       	usernameStatus.put(username, "Online");										// Add the user and the status to the hashMap				
   		return 'G';																	// Return G in case of the new user
       }
       else {																		// In other cases
    	 String status = usernameStatus.get(username);								// Get the status of the user
    	 if(status.equals("Offline")) {												// Check if the user is offline	
    		 return 'E';															// Return E in case the user is offline	
    	 }
    	 else {																		// In other case
    		 return 'O';															// Return O that means the user is online or idle		
    	 } 	 
       }     
   }
   
   public static void main(String [] args)
   {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	GreetingServer
	//
	// Method parameters	:	none
	//
	// Method return		:	void
	//
	// Synopsis				:   This method instantiates the server socket and sets a timeout for it. 
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
	   
      int port = 6066;														// Establish port 6066 as a hard-coded port override
      try
      {
         Thread t = new GreetingServer(port);								// Instantiates a server on a separate thread
         t.start();															// Executes the server
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}
