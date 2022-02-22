package com.nermink.socket_to_me;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 6969);

            InputStream inputStream = socket.getInputStream();
            BufferedReader socketInput  = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while(  (line = socketInput.readLine()) != null ){
                System.out.println(line);

                socket.close();
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
