package com.nermink.socket_to_me.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketClient {

    Map<Socket, String> socketPool = Collections.synchronizedMap(new HashMap<>());

    int connections = 200;
    ExecutorService executorService = Executors.newCachedThreadPool();
    long poolStartTime;
    int responseCount = 0;

    private void createConnectionPool(){
        for (int i = 1; i <= connections; i++){
            socketPool.put(new Socket(), null);
        }
    }

    public void initializeConnections(){
        createConnectionPool();

        for (Socket socket : socketPool.keySet()){
            establishConnection(socket);
        }
    }

    private void establishConnection(Socket socket){
        executorService.execute(() -> {

            SocketAddress address = new InetSocketAddress("127.0.0.1", 6969);
            try {
                socket.connect(address, 2000);

                processConnection(socket);
            } catch (Exception e) {

            }

        });
    }

    private void processConnection(Socket connection){

        try (InputStream inputStream = connection.getInputStream()){

            var startTime = System.currentTimeMillis();
            String input = new String(inputStream.readAllBytes());
            var endTime = System.currentTimeMillis();

            String outputMsg = input.isEmpty()
                    ? String.valueOf(endTime - startTime)
                    : String.format("%s %s", input, (endTime - startTime));

            recordResponse(connection ,outputMsg);
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void recordResponse(Socket socket, String outputMsg){
        responseCount += 1;
        //System.out.println(outputMsg);
        synchronized (socketPool){
            var val = socketPool.put(socket, outputMsg);
        }
        System.out.println(responseCount);
        System.out.println(socketPool);

        if(responseCount == connections){
            long poolEndTime = System.currentTimeMillis();
            long poolExecutionTime = poolEndTime - poolStartTime;
            System.out.println("Pool execution time "+ poolExecutionTime);
            System.out.println(socketPool);
        }
    }
}
