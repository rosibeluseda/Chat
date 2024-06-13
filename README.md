# Chat
A basic chat application built with Java that leverages network sockets to enable real-time communication. This application allows multiple users to connect to an online server, facilitating the exchange of messages in a shared chat room visible to all participants. The chat interface displays the status of each user, indicating whether they are idle, online, or offline. Additionally, it supports features such as login with own session and the ability to reconnect after disconnecting. 

<p align="center">
     <img src="https://github.com/rosibeluseda/Chat/assets/145386489/4c81e5ba-aa38-483d-82ab-8986be31c8df" alt="" width="400" > 
</p>

Our team members:
- Rosibel Useda - Programmer
- Julian Silva - Programmer
- Valentina Arias - Programmer

# My Contributions
- Network and chat functionality programming.
- Brainstorming functionality ideas.
- Testing.

# Connection between the client and the server
Communication between the server and clients is established using sockets in Java. This allows for real-time data exchange and ensures a robust and reliable connection. The server listens for incoming connections, while clients initiate the communication. Once connected, they can send and receive messages, facilitating seamless interaction.
```java
 while(true)
      {
         try
         {
            Socket server = serverSocket.accept();					// Wait for a connection from the client
            DataInputStream in =  new DataInputStream(server.getInputStream()); 	// Get the data from the client          
            DataOutputStream out =  new DataOutputStream(server.getOutputStream());	// Prepare the object for returning data to the client

            String clientInput = String.valueOf(in.readUTF());				//Read the instruction of the client									
            ....
	
            }
            server.close();								// Shut down the server
         
         }
      }
```
```java
 client = new Socket(serverName, port);							// instantiate the socket to connect to server
	        OutputStream outToServer = client.getOutputStream();			// initialize the output stream
	        out = new DataOutputStream(outToServer);				// instantiate the data output stream
	        InputStream inFromServer = client.getInputStream(); 			// initialize the input stream
	        in = new DataInputStream(inFromServer);   				// instantiate the data input stream
```
