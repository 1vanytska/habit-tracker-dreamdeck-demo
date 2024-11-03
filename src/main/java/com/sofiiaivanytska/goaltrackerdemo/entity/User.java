//package com.sofiiaivanytska.goaltrackerdemo.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.time.ZonedDateTime;
//import java.util.List;
//
//@Data
//@Entity
//@Table(name = "User")
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(name = "user_id", nullable = false)
//    private int userId;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Goal> goals;
//
//    @OneToMany(mappedBy = "friendship", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Friendship> friendships;
//
//    @OneToMany(mappedBy = "complaint", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Complaint> complaints;
//
//    @Column(nullable = false)
//    private String email;
//
//    @Column(nullable = false)
//    private String nickname;
//
//    @Column(nullable = false)
//    private String password;
//
//    @Column(nullable = false)
//    private ZonedDateTime registrationDate;
//
//    private String profile_picture;
//
//    @Column(nullable = false)
//    private String role;
//
//    @Column(nullable = false)
//    private String status;
//
//}
