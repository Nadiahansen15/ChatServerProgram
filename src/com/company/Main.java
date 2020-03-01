package com.company;

import com.sun.tools.classfile.Opcode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

//this is the Chat SERVER Program
public class Main {
    private static int port = 6767;
    private Set<String> userNames = new HashSet<String>(); // Collection Set - no repeating usernames
    private Set<UserThread> userThreads = new HashSet<UserThread>();

    public Main (int port) {
        this.port=port;
    }

    public void execute(){ //Starts the Server
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Chat Server is Listening on port " + port);
            while (true){
                Socket socket = serverSocket.accept(); //accept the incoming request - checks the address, port and localport
                System.out.println("New user connected and accepted");

                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();     // Starts Thread
            }
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        Main server = new Main(port);
        server.execute(); //connects to clients
    }

    public void boardcast(String message, UserThread excludeUser){ //Delivers a message from one to others ( broadcasting)
        for (UserThread aUser : userThreads) { //for-each loop - itorates through the userThreads
            if (aUser != excludeUser) { // not equal to// excludeUser?
                aUser.sendMessage(message);
            }
        }
    }
    public void addUserName(String userName){ //Stores username of connected client in Set.
        userNames.add(userName);
    }
    public void removeUser(String userName, UserThread aUser) { //When Client disconnect, remove associated user(name and thread)

        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser); //close the Thread
            System.out.println("the user: <<" + userName + ">> quitted");
        }
    }
    public Set<String> getUserNames() { //gets the Set
        return this.userNames;
    }
    public boolean hasUsers(){ //Returns true if there are other users connected (does not count the currently user)
        return !this.userNames.isEmpty();
    }
}
