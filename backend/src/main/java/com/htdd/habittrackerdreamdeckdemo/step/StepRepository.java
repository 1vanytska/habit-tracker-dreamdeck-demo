package com.htdd.habittrackerdreamdeckdemo.step;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface StepRepository extends JpaRepository<Step, UUID> {
}