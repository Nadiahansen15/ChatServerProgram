package com.company;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

//This is the chat Client Program
public class Main {
    private static String hostname;
    private static int port = 6767;
    private static String userName;

    public Main(String hostname, int port) // Constructer
    {
        this.hostname = hostname;
        this.port = port;
    }

    public void execute() {  //Method to connect to server specified by hostname/ip and port number
                            // When connected starts the threads
        try {
            Socket socket = new Socket(hostname, port);

            System.out.println("Connected to the chat server");

            new ReadThread(socket,this).start();
            new WriteThread(socket, this).start();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " +ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }
    //Getter and Setter
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return this.userName;
    }

    public static void main(String[] args) {
        Main client = new Main(hostname, port);
        client.execute();
    }
}
