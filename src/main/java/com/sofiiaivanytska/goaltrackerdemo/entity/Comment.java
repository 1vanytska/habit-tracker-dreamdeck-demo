//package com.sofiiaivanytska.goaltrackerdemo.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.time.ZonedDateTime;
//
//@Data
//@Entity
//@Table(name = "Comment")
//public class Comment {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(nullable = false)
//    private int commentId;
//
//    @ManyToOne
//    @JoinColumn(name = "goal_id", nullable = false)
//    private Goal goal;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @Column(nullable = false)
//    private String content;
//
//    @Column(nullable = false)
//    private ZonedDateTime date;
//}
