//package com.sofiiaivanytska.goaltrackerdemo.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.time.LocalDate;
//
//@Data
//@Entity
//@Table(name = "GoalStep")
//public class GoalStep {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(nullable = false)
//    private int note_id;
//
//    @ManyToOne
//    @JoinColumn(name = "goal_id", nullable = false)
//    private Goal goal;
//
//    @Column(nullable = false)
//    private String description;
//
//    @Column(nullable = false)
//    private boolean is_done;
//
//    private LocalDate completion_date;
//}
