import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { FormsModule } from '@angular/forms'; 
import { ActivatedRoute, Router } from '@angular/router';
import { GoalService } from './goal.service';
import { Goal as GoalModel, Step } from './goal.model';
import { CategoryService } from '../category/category.service';

interface CalendarDay {
  dayNumber: number | null;
  isToday: boolean;
  isWeekend: boolean;
  isInPeriod: boolean;
}

@Component({
  selector: 'app-goal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './goal.html',
  styleUrl: './goal.css'
})
export class Goal implements OnInit {
  
  goal: GoalModel | null = null;
  categoryName: string = 'Завантаження...';
  daysLeft: number = 0;
  progressPercentage: number = 0;

  isAddingStep: boolean = false;
  newStepDescription: string = '';
  aiSuggestion: string = '';

  currentDate: Date = new Date();
  displayDate: Date = new Date();
  calendarDays: CalendarDay[] = [];
  currentMonthName: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private goalService: GoalService,
    private categoryService: CategoryService
  ) {}

  ngOnInit(): void {
    this.generateCalendar(); 
    const id = this.route.snapshot.paramMap.get('id');
    if (id) this.loadGoal(id);
  }

  loadGoal(id: string): void {
    this.goalService.getGoalById(id).subscribe({
      next: (data) => {
        this.goal = data;
        if (!this.goal.steps) this.goal.steps = [];
        this.calculateMetrics();
        this.loadCategoryName(data.categoryId);
        this.generateCalendar(); 
      },
      error: (err) => console.error('Помилка:', err)
    });
  }

  startAddingStep(): void {
    this.isAddingStep = true;
    this.newStepDescription = '';
    this.generateSmartSuggestion();
  }

  cancelAddingStep(): void {
    this.isAddingStep = false;
    this.newStepDescription = '';
  }

  saveNewStep(): void {
    if (!this.newStepDescription.trim() || !this.goal) return;

    this.goalService.addStep(this.goal.goalId, this.newStepDescription).subscribe({
      next: (step) => {
        this.goal?.steps.push(step);
        this.isAddingStep = false;
        this.newStepDescription = '';
      },
      error: (err) => console.error('Помилка:', err)
    });
  }

  generateSmartSuggestion(): void {
    if (!this.goal) return;
    const title = this.goal.title.toLowerCase();

    if (title.includes('спорт') || title.includes('тренув') || title.includes('біг')) {
      this.aiSuggestion = 'Купити абонемент у зал / знайти тренера';
    } else if (title.includes('англійськ') || title.includes('мов')) {
      this.aiSuggestion = 'Вивчити 10 нових слів сьогодні';
    } else if (title.includes('книг') || title.includes('чита')) {
      this.aiSuggestion = 'Прочитати перші 20 сторінок';
    } else if (title.includes('грош') || title.includes('фінанс')) {
      this.aiSuggestion = 'Відкласти перші 100 грн';
    } else {
      this.aiSuggestion = 'Визначити перший маленький крок...';
    }
  }

  toggleStep(stepId: string, index: number): void {
    this.goalService.toggleStep(stepId).subscribe({
      next: (updatedStep) => {
        if (this.goal && this.goal.steps[index]) {
            this.goal.steps[index].isCompleted = updatedStep.isCompleted;
        }
      }
    });
  }

  deleteStep(stepId: string, index: number): void {
    this.goalService.deleteStep(stepId).subscribe({
        next: () => {
            this.goal?.steps.splice(index, 1);
        }
    });
  }

  loadCategoryName(categoryId: string): void {
    this.categoryService.getAllCategories().subscribe(cats => {
        const found = cats.find(c => c.id === categoryId);
        this.categoryName = found ? found.name : 'Без категорії';
    });
  }

  calculateMetrics(): void {
    if (!this.goal) return;
    const start = new Date(this.goal.startDate);
    const end = new Date(this.goal.deadline);
    const today = new Date();
    const diffTime = end.getTime() - today.getTime();
    this.daysLeft = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    if (this.daysLeft < 0) this.daysLeft = 0;
    const totalDuration = end.getTime() - start.getTime();
    const timePassed = today.getTime() - start.getTime();
    if (totalDuration <= 0) {
        this.progressPercentage = 100;
    } else {
        this.progressPercentage = Math.round((timePassed / totalDuration) * 100);
        if (this.progressPercentage > 100) this.progressPercentage = 100;
        if (this.progressPercentage < 0) this.progressPercentage = 0;
    }
  }

  goToEdit(): void {
    if (this.goal) {
      this.router.navigate(['/edit-goal', this.goal.goalId]);
    }
  }

  generateCalendar(): void {
    const year = this.displayDate.getFullYear();
    const month = this.displayDate.getMonth();
    this.currentMonthName = new Intl.DateTimeFormat('uk-UA', { month: 'long', year: 'numeric' }).format(this.displayDate);
    this.currentMonthName = this.currentMonthName.charAt(0).toUpperCase() + this.currentMonthName.slice(1);
    const firstDay = new Date(year, month, 1);
    const daysInMonth = new Date(year, month + 1, 0).getDate();
    let startDayOfWeek = firstDay.getDay() - 1; 
    if (startDayOfWeek === -1) startDayOfWeek = 6;
    this.calendarDays = [];
    for (let i = 0; i < startDayOfWeek; i++) {
      this.calendarDays.push({ dayNumber: null, isToday: false, isWeekend: false, isInPeriod: false });
    }
    const today = new Date();
    today.setHours(0,0,0,0);
    let goalStart: Date | null = null;
    let goalEnd: Date | null = null;
    if (this.goal) {
        goalStart = new Date(this.goal.startDate);
        goalEnd = new Date(this.goal.deadline);
        goalStart.setHours(0,0,0,0);
        goalEnd.setHours(0,0,0,0);
    }
    for (let i = 1; i <= daysInMonth; i++) {
      const currentIteratedDate = new Date(year, month, i);
      currentIteratedDate.setHours(0,0,0,0);
      const isToday = currentIteratedDate.getTime() === today.getTime();
      const currentDayOfWeek = currentIteratedDate.getDay();
      const isWeekend = currentDayOfWeek === 0 || currentDayOfWeek === 6;
      let isInPeriod = false;
      if (goalStart && goalEnd) {
          isInPeriod = currentIteratedDate >= goalStart && currentIteratedDate <= goalEnd;
      }
      this.calendarDays.push({ 
        dayNumber: i, isToday: isToday, isWeekend: isWeekend, isInPeriod: isInPeriod 
      });
    }
  }

  changeMonth(offset: number): void {
    const newDate = new Date(this.displayDate);
    newDate.setMonth(newDate.getMonth() + offset);
    this.displayDate = newDate;
    this.generateCalendar();
  }
}