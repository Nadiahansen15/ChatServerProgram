package com.company;

import java.io.*;
import java.net.Socket;

/*
When a new client gets connected, an instance of Userthread is created to serve that client.
//Makes the server able to handle multiple clients at the same time.
*/

public class UserThread extends Thread {

    private Socket socket;
    private Main server;
    private PrintWriter writer;


public UserThread(Socket socket, Main server)
{
    this.socket = socket;
    this.server = server;
}

public void run(){
    try {
        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        OutputStream output = socket.getOutputStream();
        writer = new PrintWriter(output,true);

        printUsers();

        String userName;
        userName = reader.readLine();            //If username already exist
        if (server.getUserNames().contains(userName)){
            writer.println("Already exist, try new: ");
            userName = reader.readLine();
            server.addUserName(userName);


        } else if (!server.getUserNames().contains(userName)) {
            server.addUserName(userName);
        }
        //server.addUserName(userName);

        String serverMessage = "New user connected: " + userName;
        server.boardcast(serverMessage, this);


        String clientMessage;
        do{ //do this as long as clientmessage is not bye! // keep reading messages and sending to others.
            clientMessage = reader.readLine();
            serverMessage = "<<" + userName + ">>: " +clientMessage;
            server.boardcast(serverMessage, this);

            // if Clientmessage is bye , do this // Notifies the others and closes the connection.
        }
        while (!clientMessage.equals("bye"));
        server.removeUser(userName,this);
        socket.close();

        serverMessage = "<<" + userName + ">> has quitted";
        server.boardcast(serverMessage,this);
        } catch (IOException ex) {
        System.out.println("Error in UserThread: " + ex.getMessage());
    }
}
public void printUsers() { // sends a list of online users to the newly connected user.
    if (server.hasUsers()){ //if it is true and there is more users
        writer.println("connected users: " +server.getUserNames());
    } else { // if there are no more users
        writer.println("no other users connected");
    }
}

public void sendMessage(String message){ // Sends a message to the client
    writer.println(message);
    }
}
