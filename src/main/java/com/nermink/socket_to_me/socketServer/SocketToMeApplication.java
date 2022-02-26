package com.nermink.socket_to_me;

import com.nermink.socket_to_me.socketServer.SocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SocketToMeApplication {

	public static void main(String[] args) {

		SpringApplication.run(SocketToMeApplication.class, args);

		SocketServer socketServer = new SocketServer();
		socketServer.initializeServer();

	}

}
