package com.nermink.socket_to_me.socketServer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.ServerSocket;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

public class SocketServer {

    Gson gson = new Gson();

    public void initializeServer(){
        try {
            ServerSocket socket = new ServerSocket(6969);

            System.out.println("Server socket started at port 6969");

            while (true){
                var client = socket.accept();

                String fileContent = readData();
                List<Person> data = gson.fromJson(fileContent, new TypeToken<List<Person>>(){}.getType());

                var response = createResponse(data);

                PrintWriter pout = new PrintWriter(client.getOutputStream(), true);

                //pout.println(String.format("THREAD %s - %s", Thread.currentThread().getName(), Thread.currentThread().getId()));

                pout.close();
                client.close();

            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to start server socket");
        }
    }

    private String readData(){

        File file = null;
        try {
            var uri = Paths.get(ClassLoader.getSystemResource("static/data.json").toURI()).toUri();
            file = new File(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        try(FileReader fileReader = new FileReader(file)){

            char[] chars = new char[(int) file.length()];
            fileReader.read(chars);

            String fileContent = new String(chars);

            return fileContent;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "[]";
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
