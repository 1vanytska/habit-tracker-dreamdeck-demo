import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CategoryService } from '../category.service';
import { Category } from '../category.model';

@Component({
  selector: 'app-category-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './category-form.html',
  styleUrl: './category-form.css'
})
export class CategoryFormComponent implements OnInit {
  categoryName: string = '';
  isSaving: boolean = false;
  categories: Category[] = [];

  constructor(
    private categoryService: CategoryService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.categoryService.getAllCategories().subscribe({
      next: (data) => {
        this.categories = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Помилка завантаження категорій:', err)
    });
  }

  saveCategory(): void {
    if (!this.categoryName.trim()) {
      alert('Помилка: Введіть назву категорії!');
      return;
    }

    this.isSaving = true;

    this.categoryService.createCategory({ name: this.categoryName }).subscribe({
      next: () => {
        this.isSaving = false;
        this.categoryName = '';
        this.loadCategories(); 
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Помилка створення категорії:', err);
        alert('Не вдалося створити категорію.');
        this.isSaving = false;
        this.cdr.detectChanges();
      }
    });
  }

  deleteCategory(id: string): void {
    if (confirm('Ви впевнені, що хочете видалити цю категорію?')) {
      this.categoryService.deleteCategory(id).subscribe({
        next: () => {
          this.loadCategories();
        },
        error: (err) => {
          console.error('Помилка видалення:', err);
          alert('Не вдалося видалити категорію.');
        }
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/home']);
  }
}