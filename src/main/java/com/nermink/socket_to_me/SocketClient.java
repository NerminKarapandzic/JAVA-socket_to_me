package com.nermink.socket_to_me;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketClient {
    public static void main(String[] args) {
        int connections = 200;

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 1; i < connections; i++){
            executorService.execute(() -> {

                var startTime = System.currentTimeMillis();
                Socket socket = null;
                try {
                    socket = new Socket("159.89.102.139", 6969);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try (InputStream inputStream = socket.getInputStream()){
                    BufferedReader socketInput  = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while(  (line = socketInput.readLine()) != null ){
                        var endTime = System.currentTimeMillis();
                        System.out.println(line + " " + (endTime - startTime));

                        socketInput.close();
                        socket.close();
                    }
                } catch (IOException e) {
                }
            });
        }
    }
}
