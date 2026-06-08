import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Goal } from '../goal/goal.model';
import { GoalService } from '../goal/goal.service';
import { UserService } from '../user/user.service';

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  templateUrl: './home.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './home.css'
})

export class Home implements OnInit {
  goals: Goal[] = [];

  constructor(
    private goalService: GoalService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.loadUserGoals();
  }

  loadUserGoals(): void {
    this.userService.getCurrentUser().subscribe({
      next: (user) => {
        if (!user.id) {
          console.error('ID користувача не знайдено');
          return;
        }
        this.goalService.getGoalsByUserId(user.id).subscribe({
          next: (data) => {
            this.goals = data;
            console.log('Цілі завантажено:', this.goals);
          },
          error: (err) => {
            console.error('Помилка при завантаженні цілей:', err);
          }
        });
      },
      error: (err) => {
        console.error('Помилка при завантаженні користувача:', err);
      }
    });
  }
}
