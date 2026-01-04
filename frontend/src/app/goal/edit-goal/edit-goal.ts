import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { GoalService } from '../goal.service';
import { Goal } from '../goal.model';
import { CategoryService } from '../../category/category.service';
import { Category } from '../../category/category.model';

@Component({
  selector: 'app-edit-goal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-goal.html',
  styleUrl: './edit-goal.css'
})
export class EditGoal implements OnInit {
  
  goal: Goal = {
    goalId: '',
    userId: 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
    title: '',
    picture: null,
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

  currentImageDisplay: string = 'assets/images/placeholder-goal.png';
  isLoading: boolean = true;
  isEditMode: boolean = false;
  categories: Category[] = [];

  constructor(
    private goalService: GoalService,
    private categoryService: CategoryService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadCategories();
    const id = this.route.snapshot.paramMap.get('id');
    
    if (id) {
      this.isEditMode = true;
      this.loadGoal(id);
    } else {
      this.isEditMode = false;
      this.isLoading = false;
    }
  }

  loadCategories(): void {
    this.categoryService.getAllCategories().subscribe({
      next: (data) => {
        this.categories = data;
      },
      error: (err) => console.error('Не вдалося завантажити категорії:', err)
    });
  }

  loadGoal(id: string): void {
    this.goalService.getGoalById(id).subscribe({
      next: (data) => {
        this.goal = data;
        if (this.goal.picture) {
          this.currentImageDisplay = this.goal.picture;
        }
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Помилка завантаження:', err);
        this.isLoading = false;
      }
    });
  }

  saveGoal(): void {
    console.log('Спроба зберегти ціль...', this.goal);

    if (!this.goal.title.trim()) {
      alert('Помилка: Введіть назву цілі!');
      return;
    }
    if (!this.goal.categoryId) {
      alert('Помилка: Виберіть категорію!');
      return;
    }
    if (!this.goal.deadline) {
      alert('Помилка: Вкажіть дедлайн!');
      return;
    }
    if (!this.goal.startDate) {
      alert('Помилка: Вкажіть дату початку!');
      return;
    }

    if (this.isEditMode) {
      this.goalService.updateGoal(this.goal.goalId, this.goal).subscribe({
        next: () => {
          alert('Ціль оновлено!');
          this.router.navigate(['/home']);
        },
        error: (err) => {
          console.error('Помилка оновлення:', err);
          alert('Не вдалося оновити ціль. Див. консоль.');
        }
      });
    } else {
      this.goalService.createGoal(this.goal).subscribe({
        next: () => {
          alert('Ціль створено!');
          this.router.navigate(['/home']);
        },
        error: (err) => {
          console.error('Помилка створення:', err);
          alert('Не вдалося створити ціль. Див. консоль.');
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

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      
      reader.onload = (e: any) => {
        const base64String = e.target.result;

        this.currentImageDisplay = base64String;
        this.goal.picture = base64String; 
      };
      
      reader.readAsDataURL(file);
    }
  }
}