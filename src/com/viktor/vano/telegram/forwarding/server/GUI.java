package com.viktor.vano.telegram.forwarding.server;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import static com.viktor.vano.telegram.forwarding.server.FileManager.*;

public class GUI extends Application {
    private static String TOKEN = "";
    private static String CHAT_ID = "";
    private final String version = "20220715";
    private final int width = 400;
    private final int height = 120;

    private Server server;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        String portString = readOrCreateFile("telegramForwardingServerPort.txt");
        int port;
        try{
            port = Integer.parseInt(portString);
        }catch (Exception e)
        {
            e.printStackTrace();
            port = 8765;
            writeToFile("telegramForwardingServerPort.txt", String.valueOf(port));
        }
        TOKEN = readOrCreateFile("telegram_token.txt");
        CHAT_ID = readOrCreateFile("telegram_chat_id.txt");
        server = new Server(TOKEN, CHAT_ID, port);
        server.start();

        Pane pane = new Pane();

        Scene scene = new Scene(pane, width, height);

        stage.setTitle("Telegram Forwarding Server " + version);
        stage.setScene(scene);
        stage.show();
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
        stage.setMaxWidth(stage.getWidth());
        stage.setMaxHeight(stage.getHeight());
        stage.setResizable(false);

        Label labelPort = new Label("Port: " + port);
        labelPort.setFont(Font.font("Arial", 24));
        labelPort.setLayoutX(130);
        labelPort.setLayoutY(50);
        pane.getChildren().add(labelPort);

        try
        {
            Image icon = new Image(getClass().getResourceAsStream("telegram.jpg"));
            stage.getIcons().add(icon);
            System.out.println("Icon loaded on the first attempt...");
        }catch(Exception e)
        {
            try
            {
                Image icon = new Image("telegram.jpg");
                stage.getIcons().add(icon);
                System.out.println("Icon loaded on the second attempt...");
            }catch(Exception e1)
            {
                System.out.println("Icon failed to load...");
            }
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        server.stopServer();
    }
}
