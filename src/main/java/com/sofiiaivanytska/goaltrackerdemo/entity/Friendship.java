//package com.sofiiaivanytska.goaltrackerdemo.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Data
//@Entity
//@Table(name = "Friendship")
//public class Friendship {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(name = "friendship_id", nullable = false)
//    private int friendshipId;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "friend_id", nullable = false)
//    private User friend;
//
//    @Column(nullable = false)
//    private String status;
//}
