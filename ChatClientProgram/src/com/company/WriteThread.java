package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/* This thread is responsible for reading user's input and send it to the server
in runs in an infinite loop until the user types "bye" to quit
 */
public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private Main client;

    public WriteThread(Socket socket, Main client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    String userName;
    public void run() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\nEnter your name:  ");
        userName = sc.nextLine();
        writer.println(userName);
        client.setUserName(client.getUserName());

        String text;
       do {
           text = sc.nextLine();
           writer.println(text);
       }
           while (!text.equals(("bye"))) ;

           try {
            socket.close();
            }
            catch (IOException ex) {
            System.out.println("Error writing to server: " + ex.getMessage());
            }
    }
}
