import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { GoalService } from '../goal.service';
import { Goal, Step } from '../goal.model';
import { CategoryService } from '../../category/category.service';
import { Category } from '../../category/category.model';
import { UserService } from '../../user/user.service';
import { compressImageFile } from '../../shared/image-compression.utils';

@Component({
  selector: 'app-edit-goal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-goal.html',
  styleUrl: './edit-goal.css'
})
export class EditGoal implements OnInit {

  readonly GOAL_TITLE_MAX_LENGTH = 25;
  readonly GOAL_DESCRIPTION_MAX_LENGTH = 150;
  
  readonly DEFAULT_GOAL_IMAGE = '/assets/images/placeholder-goal.png';

  goal: Goal = {
    goalId: '',
    userId: '',
    title: '',
    picture: this.DEFAULT_GOAL_IMAGE,
    description: '',
    categoryId: '',
    isPublic: true,
    startDate: new Date().toISOString().split('T')[0],
    deadline: '',
    status: 'IN_PROGRESS',
    isArchived: false,
    archivingTime: null,
    steps: []
  };

  currentImageDisplay: string = this.DEFAULT_GOAL_IMAGE;
  isLoading: boolean = true;
  isSaving: boolean = false;
  isEditMode: boolean = false;
  categories: Category[] = [];

  draggedStepIndex: number | null = null;
  dragOverIndex: number | null = null;

  constructor(
    private goalService: GoalService,
    private categoryService: CategoryService,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadCategories();
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.isEditMode = true;
      this.loadGoal(id);
    } else {
      this.isEditMode = false;
      this.userService.getCurrentUser().subscribe({
        next: (user) => {
          if (user.id) {
            this.goal.userId = user.id;
          }
          this.isLoading = false;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Не вдалося завантажити користувача:', err);
          this.isLoading = false;
          this.cdr.detectChanges();
        }
      });
    }
  }

  loadCategories(): void {
    this.categoryService.getAllCategories().subscribe({
      next: (data) => {
        this.categories = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Не вдалося завантажити категорії:', err)
    });
  }

  loadGoal(id: string): void {
    this.goalService.getGoalById(id).subscribe({
      next: (data) => {
        this.goal = data;
        if (!this.goal.steps) {
          this.goal.steps = [];
        }
        
        if (!this.goal.picture || this.goal.picture.trim() === '') {
          this.currentImageDisplay = this.DEFAULT_GOAL_IMAGE;
        } else {
          this.currentImageDisplay = this.goal.picture;
        }
        
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Помилка завантаження:', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  private getCategoryName(): string {
    const found = this.categories.find(c => c.id === this.goal.categoryId);
    return found ? found.name : '';
  }

  addStep(): void {
    this.goal.steps.push({
      stepId: '',
      description: '',
      isCompleted: false
    });
    this.cdr.detectChanges();
  }

  removeStep(index: number): void {
    this.goal.steps.splice(index, 1);
    this.cdr.detectChanges();
  }

  onStepDragStart(index: number): void {
    this.draggedStepIndex = index;
  }

  onStepDragOver(event: DragEvent, index: number): void {
    event.preventDefault();
    this.dragOverIndex = index;
  }

  onStepDragLeave(): void {
    this.dragOverIndex = null;
  }

  onStepDrop(event: DragEvent, targetIndex: number): void {
    event.preventDefault();
    if (this.draggedStepIndex === null || this.draggedStepIndex === targetIndex) {
      this.resetDragState();
      return;
    }

    const steps = [...this.goal.steps];
    const [moved] = steps.splice(this.draggedStepIndex, 1);
    steps.splice(targetIndex, 0, moved);
    this.goal.steps = steps;
    this.resetDragState();
    this.cdr.detectChanges();
  }

  onStepDragEnd(): void {
    this.resetDragState();
  }

  private resetDragState(): void {
    this.draggedStepIndex = null;
    this.dragOverIndex = null;
  }

  saveGoal(): void {
    if (!this.goal.userId) {
      alert('Помилка: Не вдалося визначити користувача. Спробуйте оновити сторінку.');
      return;
    }
    if (!this.goal.title.trim()) {
      alert('Помилка: Введіть назву цілі!');
      return;
    }
    if (this.goal.title.trim().length > this.GOAL_TITLE_MAX_LENGTH) {
      alert(`Помилка: Заголовок не може перевищувати ${this.GOAL_TITLE_MAX_LENGTH} символів.`);
      return;
    }
    if (!this.goal.categoryId) {
      alert('Помилка: Виберіть категорію!');
      return;
    }
    if (this.goal.description.trim().length > this.GOAL_DESCRIPTION_MAX_LENGTH) {
      alert(`Помилка: Опис не може перевищувати ${this.GOAL_DESCRIPTION_MAX_LENGTH} символів.`);
      return;
    }
    if (!this.goal.startDate) {
      alert('Помилка: Вкажіть дату початку!');
      return;
    }
    if (!this.goal.deadline) {
      alert('Помилка: Вкажіть дедлайн!');
      return;
    }
    if (this.goal.startDate > this.goal.deadline) {
      alert('Помилка: Дедлайн не може бути раніше дати початку!');
      return;
    }

    this.goal.steps = this.goal.steps.filter(s => s.description.trim());
    
    if (this.goal.picture === this.DEFAULT_GOAL_IMAGE) {
      this.goal.picture = null; 
    }

    this.isSaving = true;

    if (this.isEditMode) {
      this.goalService.updateGoal(this.goal.goalId, this.goal).subscribe({
        next: () => {
          this.isSaving = false;
          this.router.navigate(['/goal', this.goal.goalId]);
        },
        error: (err) => {
          console.error('Помилка оновлення:', err);
          alert('Не вдалося оновити ціль.');
          this.isSaving = false;
          this.cdr.detectChanges();
        }
      });
    } else {
      this.goalService.createGoal(this.goal).subscribe({
        next: (created) => {
          this.isSaving = false;
          this.router.navigate(['/goal', created.goalId]);
        },
        error: (err) => {
          console.error('Помилка створення:', err);
          alert('Не вдалося створити ціль.');
          this.isSaving = false;
          this.cdr.detectChanges();
        }
      });
    }
  }

  archiveGoal(): void {
    if (confirm('Ви впевнені, що хочете архівувати цю ціль?')) {
      this.goal.isArchived = true;
      this.goal.archivingTime = new Date().toISOString().split('T')[0];
      this.saveGoal();
    }
  }

  unarchiveGoal(): void {
    if (confirm('Розархівувати цю ціль? Вона знову стане активною.')) {
      this.goal.isArchived = false;
      this.goal.archivingTime = null;
      this.saveGoal();
    }
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) {
      return;
    }

    compressImageFile(file)
      .then((compressed) => {
        this.currentImageDisplay = compressed;
        this.goal.picture = compressed;
        this.cdr.detectChanges();
      })
      .catch((err) => {
        console.error('Помилка стиснення зображення:', err);
        alert('Не вдалося обробити зображення.');
      });
  }

  trackStep(index: number, step: Step): string {
    return step.stepId || `new-${index}`;
  }
}