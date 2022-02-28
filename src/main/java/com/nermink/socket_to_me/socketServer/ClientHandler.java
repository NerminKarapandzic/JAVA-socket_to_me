package com.nermink.socket_to_me.socketServer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler{

    Gson gson = new Gson();
    Socket connection;

    ClientHandler(Socket socket){
        this.connection = socket;
    }

    public void run(){
        try {
            processConnection(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processConnection(Socket connection) throws IOException {

        try (var input = connection.getInputStream()){
            while (input.readAllBytes() != null){
                String fileContent = readData();
                List<Person> data = gson.fromJson(fileContent, new TypeToken<List<Person>>(){}.getType());

                var response = createResponse(data);

                PrintWriter pout = new PrintWriter(connection.getOutputStream(), true);

                //pout.println(String.format("THREAD %s - %s", Thread.currentThread().getName(), Thread.currentThread().getId()));

                pout.close();
                connection.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


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
