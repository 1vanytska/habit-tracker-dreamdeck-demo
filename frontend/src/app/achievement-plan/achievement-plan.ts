import { Component, OnInit, PLATFORM_ID, inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Goal } from '../goal/goal.model';
import { GoalService } from '../goal/goal.service';
import { UserService } from '../user/user.service';
import { getGoalImageUrl } from '../shared/image.utils';

interface TrailGoal extends Goal {
  priority: number;
}

@Component({
  selector: 'app-achievement-plan',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './achievement-plan.html',
  styleUrl: './achievement-plan.css'
})
export class AchievementPlan implements OnInit {
  private platformId = inject(PLATFORM_ID);
  private readonly storageKey = 'dreamdesk_goal_priorities';

  isLoading = true;
  trailGoals: TrailGoal[] = [];
  userId: string | null = null;

  constructor(
    private goalService: GoalService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe({
      next: (user) => {
        this.userId = user.id ?? null;
        if (this.userId) {
          this.loadGoals(this.userId);
        } else {
          this.isLoading = false;
        }
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  getGoalImage(goal: Goal): string {
    return getGoalImageUrl(goal.picture);
  }

  getDaysLeft(deadline: string): number {
    const diff = new Date(deadline).getTime() - Date.now();
    return Math.max(0, Math.ceil(diff / (1000 * 60 * 60 * 24)));
  }

  moveUp(index: number): void {
    if (index <= 0) return;
    this.swap(index, index - 1);
  }

  moveDown(index: number): void {
    if (index >= this.trailGoals.length - 1) return;
    this.swap(index, index + 1);
  }

  private swap(a: number, b: number): void {
    const items = [...this.trailGoals];
    [items[a], items[b]] = [items[b], items[a]];
    this.trailGoals = items.map((g, i) => ({ ...g, priority: i + 1 }));
    this.savePriorities();
  }

  private loadGoals(userId: string): void {
    this.goalService.getGoalsByUserId(userId).subscribe({
      next: (goals) => {
        const active = goals.filter(g => !g.isArchived);
        const ordered = this.applyStoredOrder(active);
        this.trailGoals = ordered.map((g, i) => ({ ...g, priority: i + 1 }));
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  private applyStoredOrder(goals: Goal[]): Goal[] {
    const stored = this.readPriorities();
    if (!stored.length) {
      return [...goals].sort((a, b) => a.deadline.localeCompare(b.deadline));
    }

    const map = new Map(goals.map(g => [g.goalId, g]));
    const ordered: Goal[] = [];

    for (const id of stored) {
      const goal = map.get(id);
      if (goal) {
        ordered.push(goal);
        map.delete(id);
      }
    }

    const rest = [...map.values()].sort((a, b) => a.deadline.localeCompare(b.deadline));
    return [...ordered, ...rest];
  }

  private savePriorities(): void {
    if (!isPlatformBrowser(this.platformId)) return;
    const ids = this.trailGoals.map(g => g.goalId);
    localStorage.setItem(this.storageKey, JSON.stringify(ids));
  }

  private readPriorities(): string[] {
    if (!isPlatformBrowser(this.platformId)) return [];
    try {
      const raw = localStorage.getItem(this.storageKey);
      return raw ? JSON.parse(raw) : [];
    } catch {
      return [];
    }
  }
}
