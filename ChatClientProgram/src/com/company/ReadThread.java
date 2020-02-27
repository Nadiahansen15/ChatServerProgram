package com.company;

/* This thread is responsible for reading server's input and printing it to the console.
It runs in an infinite loop until the client disconnects from the server.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private Main client;

    public ReadThread(Socket socket, Main client) {
        this.socket = socket;
        this.client = client;


        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: "+ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run () {
        while (true){
            try {
                String response = reader.readLine();
                System.out.println("\n"+response);

                //Prints the username after displaying the server's message
                if (client.getUserName() != null) {
                    System.out.println("[" + client.getUserName() + "]: ");
                }
            } catch (IOException ex) {
                System.out.println("You have left the chat");
                break;
            }
        }
    }
}
