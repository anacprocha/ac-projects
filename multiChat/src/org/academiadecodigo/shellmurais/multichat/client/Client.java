package org.academiadecodigo.shellmurais.multichat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements Runnable {

    private Socket clientSocket;
    private String name;
    private BufferedReader in;
    private BufferedReader fromConsole;
    private PrintWriter out;


    public Client() {
        createSocket(9000);
        fromConsole = new BufferedReader(new InputStreamReader(System.in));
        name = setName();
        out.println("/create:" + name + " entered this room");

    }

    public int getPort() {
        BufferedReader portGetter = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Port: ");

        String port = "";
        try {
            port = portGetter.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Integer.parseInt(port);
    }

    public String setName() {
        BufferedReader nameGetter = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Name: ");

        try {
            return name = nameGetter.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public boolean isBound() {
        return clientSocket.isBound();
    }

    public void sendInfo() {
        try {
            String message = fromConsole.readLine();

            String[] words = message.split(" ");

            switch (words[0]) {
                case "/quit":
                    out.println("/quit:" + name + " left this chat");
                    System.exit(0);
                    break;

                case "/name":
                    out.println("/name:" + name + " changed name to :" + setName());
                    break;

                case "/crtGroup":
                    createGroup();
                    //createSocket();
                    break;

                case "/chgGroup":
                    changeGroup();
                    break;

                case "/help":
                    System.out.println("/quit         leave chat\n" +
                            "/name         change name\n" +
                            "/crtGroup     create a new group\n" +
                            "/chgGroup     change group\n" +
                            "/gen message  send message to all participants\n" +
                            "/whisper      send private message");
                    break;

                case "/gen":
                    generalSend(message);
                    break;

                case "/whisper":
                    whisper();
                    break;

                case "/whois":
                    out.println("/whois:");
                    break;

                case "/groups":
                    out.println("/groups:");
                    break;

                default:
                    out.println(name + ": " + message);
                    break;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void receiveInfo() {
        try {
            String serverMessage = in.readLine();

            if (serverMessage == null) {
                System.out.println("Connection closed.");
                System.exit(0);
            }

            System.out.println(serverMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            sendInfo();
        }
    }

    public void createSocket(int port) {
        try {
            clientSocket = new Socket(InetAddress.getLocalHost(), port);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            if (name != null) {
                out.println(name + ": entered this room");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void whisper() {
        BufferedReader privateMessage = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please write according to the example: \n" +
                "personName:message");
        try {
            out.println("/whisper:" + privateMessage.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createGroup() {
        BufferedReader room = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter the name of the new group:");
        String roomName = null;
        try {
            roomName = room.readLine();
            out.println("/crtGroup:" + roomName);
            System.out.println("You created a new group, " + roomName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeGroup() {
        BufferedReader roomName = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter the name of group:");
        String newRoom = "";
        try {
            newRoom = roomName.readLine();
            out.println("/chgGroup:" + newRoom);
            System.out.println("You moved to group " + newRoom);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generalSend(String message) {
        out.println("/gen:" + message.substring(message.indexOf(" ") + 1));
    }

}


