//package com.sofiiaivanytska.goaltrackerdemo.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//@Data
//@Entity
//@Table(name = "Goal")
//public class Goal {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(name = "goal_id", nullable = false)
//    private int goalId;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @Column(nullable = false)
//    private String title;
//
//    private String picture;
//
//    private String description;
//
//    @ManyToOne
//    @JoinColumn(name = "category_id", nullable = false)
//    private Category category;
//
//    @Column(nullable = false)
//    private boolean isPublic;
//
//    private LocalDate deadline;
//
//    @Column(nullable = false)
//    private String status;
//
//    @Column(nullable = false)
//    private Boolean isArchived;
//
//    private LocalDateTime archivationTime;
//}
