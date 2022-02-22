package com.nermink.socket_to_me;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

@SpringBootApplication
public class SocketToMeApplication {

	public static void main(String[] args) {

		SpringApplication.run(SocketToMeApplication.class, args);

		try {
			ServerSocket socket = new ServerSocket(6969);

			System.out.println("Server socket started at port 6969");

			while (true){
				Socket client = socket.accept();

				PrintWriter pout = new PrintWriter(client.getOutputStream(), true);

				pout.println("Hello from server");

				client.close();
			}
		}catch (Exception e){
			System.out.println("Failed to start server socket");
		}
	}

}
