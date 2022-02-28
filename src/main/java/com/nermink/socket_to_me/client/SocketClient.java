package com.nermink.socket_to_me.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketClient {

    List<Socket> socketPool = new ArrayList<>();
    List<String> responseArr = Collections.synchronizedList(new ArrayList<>());

    int connections = 200;
    ExecutorService executorService = Executors.newCachedThreadPool();
    long poolStartTime;
    int responseCount = 0;

    private void createConnectionPool(){
        for (int i = 1; i <= connections; i++){
            socketPool.add(new Socket());
        }
    }

    public void initializeConnections(){
        createConnectionPool();

        for (Socket socket : socketPool){
            establishConnection(socket);
        }
    }

    private void establishConnection(Socket socket){
        executorService.execute(() -> {

            SocketAddress address = new InetSocketAddress("127.0.0.1", 6969);
            try {
                socket.connect(address, 2 * 1000);
                processConnection(socket);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    private void processConnection(Socket connection){

        try {
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(1);
        }catch (Exception e){
            e.printStackTrace();
        }

        try (InputStream inputStream = connection.getInputStream()){

            var startTime = System.currentTimeMillis();
            String input = new String(inputStream.readAllBytes());
            var endTime = System.currentTimeMillis();

            String outputMsg = input.isEmpty()
                    ? String.valueOf(endTime - startTime)
                    : String.format("%s %s", input, (endTime - startTime));

            recordResponse(outputMsg);
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void recordResponse(String outputMsg){
        responseCount += 1;
        System.out.println(responseCount);

        synchronized (responseArr){
            responseArr.add(outputMsg);
        }

        if(responseCount == connections){
            long poolEndTime = System.currentTimeMillis();
            long poolExecutionTime = poolEndTime - poolStartTime;
            System.out.println("Pool execution time "+ poolExecutionTime);
            System.out.println(responseArr);
        }
    }
}
