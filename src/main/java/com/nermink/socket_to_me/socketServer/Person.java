package com.nermink.socket_to_me.socketServer;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Person {
    private String _id;
    private int index;
    private String guid;
    private boolean isActive;
    private String balance;
    private String picture;
    private int age;
    private String eyeColor;
    private String name;
    private String gender;
    private String company;
    private String email;
    private String phone;
    private String address;
    private String about;
    private String registered;
    private double latitude;
    private double longitude;
    private List<String> tags;
    private List<Map<String, Object>> friends;
    private String greeting;
    private String favoriteFruit;
}
