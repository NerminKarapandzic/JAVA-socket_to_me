package com.nermink.socket_to_me;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketClient {
    public static void main(String[] args) {
        int connections = 8000;

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 1; i < connections; i++){
            executorService.execute(() -> {

                var startTime = System.currentTimeMillis();
                Socket socket = null;
                try {
                    socket = new Socket("127.0.0.1", 6969);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try (InputStream inputStream = socket.getInputStream()){

                    String input = new String(inputStream.readAllBytes());
                    var endTime = System.currentTimeMillis();

                    String outputMsg = input.isEmpty()
                            ? String.valueOf(endTime - startTime)
                            : String.format("%s %s", input, (endTime - startTime));

                    System.out.println(outputMsg);

                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
