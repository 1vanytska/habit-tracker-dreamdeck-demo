import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Goal } from '../goal/goal.model';
import { GoalService } from '../goal/goal.service';

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  templateUrl: './home.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './home.css'
})

export class Home implements OnInit {
  goals: Goal[] = [];

  currentUserId: string = 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'; 

  constructor(private goalService: GoalService) {}

  ngOnInit(): void {
    this.loadUserGoals();
  }

  loadUserGoals(): void {
    this.goalService.getGoalsByUserId(this.currentUserId).subscribe({
      next: (data) => {
        this.goals = data;
        console.log('Цілі завантажено:', this.goals);
      },
      error: (err) => {
        console.error('Помилка при завантаженні цілей:', err);
      }
    });
  }
}
