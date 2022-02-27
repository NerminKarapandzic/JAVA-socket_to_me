package com.nermink.socket_to_me.socketServer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {

    Gson gson = new Gson();

    public void initializeServer(){
        try {
            ServerSocket socket = new ServerSocket(6969);

            System.out.println("Server socket started at port 6969");

            ExecutorService executorService = Executors.newFixedThreadPool(5);

            executorService.execute(() -> {
                while (true){
                    try {
                        acceptConnection(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to start server socket");
        }
    }

    private void acceptConnection(ServerSocket socket) throws IOException {
        var client = socket.accept();

        String fileContent = readData();
        List<Person> data = gson.fromJson(fileContent, new TypeToken<List<Person>>(){}.getType());

        var response = createResponse(data);

        PrintWriter pout = new PrintWriter(client.getOutputStream(), true);

        //pout.println(String.format("THREAD %s - %s", Thread.currentThread().getName(), Thread.currentThread().getId()));

        pout.close();
        client.close();
    }

    private String readData(){

        InputStream in = SocketServer.class.getClassLoader().getResourceAsStream("static/data.json");

        try {
            byte[] buffer = in.readAllBytes();

            String fileContent = new String(buffer);
            in.close();
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
            return "[]";
        }
    }

    private SocketResponse createResponse(List<Person> data){

        Integer activeUsers = 0;
        Float totalBalance = 0f;
        Integer totalAge = 0;
        for (int i = 1; i < data.size(); i++){
            var p = data.get(i);
            if(p.isActive()){
                activeUsers += 1;
            }
            totalBalance += Float.parseFloat(p.getBalance().substring(1).replace(",", ""));
            totalAge += p.getAge();
        }

        SocketResponse socketResponse = new SocketResponse();
        socketResponse.setActiveUsers(activeUsers);
        socketResponse.setAverageBalance(totalBalance / data.size());
        socketResponse.setAverageAge(totalAge / data.size());
        socketResponse.setSize(data.size());
        return socketResponse;
    }
}
