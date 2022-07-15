package com.viktor.vano.telegram.forwarding.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileManager {
    public static String readOrCreateFile(String filename)
    {
        File file = new File(filename);

        try
        {
            //Create the file
            if (file.createNewFile())
            {
                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
            }

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String data;
            data = reader.readLine();
            reader.close();
            System.out.println("Reading successful.");

            if(data==null && filename.equals("telegramForwardingServerPort.txt"))
            {
                data="8765";
                writeToFile(filename, data);
            }else if(data==null && filename.equals("telegram_chat_id.txt"))
            {
                data="0000000000";
                writeToFile(filename, data);
            }else if(data==null && filename.equals("telegram_token.txt"))
            {
                data="0000000000:aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
                writeToFile(filename, data);
            }

            return data;
        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
            return null;
        }
    }

    public static boolean writeToFile(String filename, String data)
    {
        File file = new File(filename);

        try
        {
            //Create the file
            if (file.createNewFile())
            {
                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
            }

            //Write Content
            FileWriter writer = new FileWriter(file);
            writer.write(data);
            writer.close();
            System.out.println("File write successful.");
            return true;
        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
            return false;
        }
    }
}
