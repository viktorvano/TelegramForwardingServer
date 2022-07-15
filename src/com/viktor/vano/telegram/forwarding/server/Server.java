package com.viktor.vano.telegram.forwarding.server;

import java.io.*;
import java.net.*;

public class Server extends Thread{
    private int port;
    private boolean run = true;
    private String message = "";
    private String token = "";
    private String chat_id = "";

    //initialize socket and input stream
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in  = null;

    public Server(String token, String chat_id, int port)
    {
        this.token = token;
        this.chat_id = chat_id;
        this.port = port;
    }

    public void stopServer()
    {
        this.run = false;
        try {
            if(socket!=null)
                socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(server!=null)
                server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(in!=null)
                in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        while (run)
        {
            socket = null;
            server = null;
            in = null;

            boolean send = true;
            // starts server and waits for a connection
            try
            {
                server = new ServerSocket(port);
                System.out.println("Server started");

                System.out.println("Waiting for a client ...");

                socket = server.accept();
                System.out.println("Client accepted");
                socket.setSoTimeout(10000);

                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

                try
                {
                    this.message = in.readUTF();
                } catch (IOException e) {
                    send = false;
                    e.printStackTrace();
                }

            }
            catch(Exception e)
            {
                send = false;
                System.out.println(e);
            }
            System.out.println("Closing connection");


            try {
                if(socket!=null)
                    socket.close();
            } catch (Exception e) {
                send = false;
                e.printStackTrace();
            }

            try {
                if(server!=null)
                    server.close();
            } catch (Exception e) {
                send = false;
                e.printStackTrace();
            }

            try {
                if(in!=null)
                    in.close();
            } catch (Exception e) {
                send = false;
                e.printStackTrace();
            }

            if(send)
            {
                if(sendMessage(this.message, this.token, this.chat_id))
                    System.out.println("Message was sent successfully.");
                else
                    System.out.println("Error sending a message.");
            }
        }
        System.out.println("Text server stopped successfully.");
    }

    boolean sendMessage(String message, String token, String chat_id)
    {
        if(message != null && message.length() > 0)
        {
            if(message.contains(" "))
            {
                message = message.replace(" ", "%20");
            }

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("https://api.telegram.org/bot");
            stringBuilder.append(token);
            stringBuilder.append("/sendMessage?chat_id=");
            stringBuilder.append(chat_id);
            stringBuilder.append("&text=");
            stringBuilder.append(message);

            try{
                URL url = new URL(stringBuilder.toString());
                URLConnection yc = url.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                yc.getInputStream()));
                String inputLine;

                stringBuilder = new StringBuilder();
                while ((inputLine = in.readLine()) != null)
                    stringBuilder.append(inputLine);
                in.close();



                if(stringBuilder.toString().contains("\"ok\":true"))
                    return true;
            }catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}