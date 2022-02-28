package com.nermink.socket_to_me;

import com.nermink.socket_to_me.client.SocketClient;

public class SocketClientApplication {

    public static void main(String[] args) {

        SocketClient socketClient = new SocketClient();
        socketClient.initializeConnections();
    }
}
