package com.nermink.socket_to_me.socketServer;

import lombok.Data;

import java.util.List;

@Data
public class SocketResponse {

    private Integer activeUsers;
    private Integer averageAge;
    private Float averageBalance;
    private Integer size;
    private List<Person> data;

}
