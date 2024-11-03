//package com.sofiiaivanytska.goaltrackerdemo.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.time.ZonedDateTime;
//
//@Data
//@Entity
//@Table(name = "Complaint")
//public class Complaint {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(name = "complaint_id", nullable = false)
//    private int complaintId;
//
//    @ManyToOne
//    @JoinColumn(name = "complainant_id", nullable = false)
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "reported_user_id", nullable = false)
//    private User userReported;
//
//    @Column(nullable = false)
//    private String description;
//
//    @Column(nullable = false)
//    private String status;
//
//    @Column(nullable = false)
//    private ZonedDateTime date;
//}
