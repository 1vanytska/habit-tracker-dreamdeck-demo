import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Goal } from '../goal/goal.model';
import { GoalService } from '../goal/goal.service';
import { UserService } from '../user/user.service';

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {
  
  readonly DEFAULT_GOAL_IMAGE = '/assets/images/placeholder-goal.png';

  goals: Goal[] = [];

  constructor(
    private goalService: GoalService,
    private userService: UserService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadUserGoals();
  }

  getGoalImage(goal: Goal): string {
    if (goal.picture && goal.picture.trim() !== '') {
      return goal.picture;
    }
    return this.DEFAULT_GOAL_IMAGE;
  }

  loadUserGoals(): void {
    this.userService.getCurrentUser().subscribe({
      next: (user) => {
        if (!user || !user.id) {
          console.error('ID користувача не знайдено. Перевірте, чи бекенд повертає поле id.');
          return;
        }
        
        this.goalService.getGoalsByUserId(user.id).subscribe({
          next: (data) => {
            this.goals = data;
            console.log('Цілі завантажено:', this.goals);
            this.cdr.detectChanges();
          },
          error: (err) => {
            console.error('Помилка при завантаженні цілей:', err);
            this.cdr.detectChanges();
          }
        });
      },
      error: (err) => {
        console.error('Помилка при завантаженні користувача:', err);
      }
    });
  }
}