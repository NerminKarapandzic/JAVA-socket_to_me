package com.nermink.socket_to_me.socketServer;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {

    ExecutorService executorService = Executors.newCachedThreadPool();

    public void initializeServer(){
        try {
            ServerSocket socket = new ServerSocket(6969);

            System.out.println("Server socket started at port 6969");

            while (true){
                try{
                    var connection = socket.accept();

                    ClientHandler connectionHandler = new ClientHandler(connection);
                    executorService.execute(connectionHandler::run);
                }catch (Exception e){
                    System.out.println("Couldn't establish connection");
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to start server socket");
        }
    }

}
