package com.nermink.socket_to_me.socketServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SocketServer {

    List<Socket> acceptedConnections = Collections.synchronizedList(new ArrayList<>());
    int connectionLimit = 200;
    int connectionCount = 0;
    public void initializeServer(){
        try {
            ServerSocket socket = new ServerSocket(6969);

            System.out.println("Server socket started at port 6969");

            while (true){
                try{
                    var connection = socket.accept();

                    acceptedConnections.add(connection);

                    connectionCount += 1;
                    System.out.println(connectionCount);

                    if(acceptedConnections.size() == connectionLimit){
                        break;
                    }

                    ClientHandler connectionHandler = new ClientHandler(connection);
                    connectionHandler.start();
                }catch (Exception e){
                    System.out.println("Couldn't establish connection");
                    e.printStackTrace();
                }

            }

            System.out.println(acceptedConnections);

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to start server socket");
        }
    }

}
