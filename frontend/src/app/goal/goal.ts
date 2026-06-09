import { Component, OnInit, inject, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { FormsModule } from '@angular/forms'; 
import { ActivatedRoute, Router } from '@angular/router';
import { GoalService } from './goal.service';
import { Goal as GoalModel } from './goal.model';
import { CategoryService } from '../category/category.service';
import { NoteService, Note } from './note/note.service';
import { AiService } from '../ai/ai.service';
import { StepsSuggestionResponse } from '../ai/ai.model';

interface CalendarDay {
  dayNumber: number | null;
  fullDate: Date | null;
  isToday: boolean;
  isWeekend: boolean;
  isInPeriod: boolean;
  hasNote: boolean;
}

@Component({
  selector: 'app-goal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './goal.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './goal.css'
})
export class Goal implements OnInit {
  
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private goalService = inject(GoalService);
  private categoryService = inject(CategoryService);
  private noteService = inject(NoteService);
  private aiService = inject(AiService);

  goal: GoalModel | null = null;
  categoryName: string = 'Завантаження...';
  daysLeft: number = 0;
  progressPercentage: number = 0;

  isAddingStep: boolean = false;
  newStepDescription: string = '';

  isAiLoading: boolean = false;
  aiStepsSuggestion: StepsSuggestionResponse | null = null;
  showAiStepsSuggestion: boolean = false;

  draggedStepIndex: number | null = null;
  dragOverIndex: number | null = null;

  currentDate: Date = new Date();
  displayDate: Date = new Date();
  calendarDays: CalendarDay[] = [];
  currentMonthName: string = '';

  notes: Note[] = [];
  recentNotes: Note[] = [];
  
  isModalOpen: boolean = false;
  selectedDate: Date | null = null;
  currentNoteContent: string = '';
  editingNoteId: string | null = null; 

  constructor() {}

  ngOnInit(): void {
    this.generateCalendar();

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadGoal(id);
    }
  }

  loadGoal(id: string): void {
    this.goalService.getGoalById(id).subscribe({
      next: (data) => {
        this.goal = data;
        
        this.generateCalendar(); 

        if (!this.goal.steps) this.goal.steps = [];
        this.calculateMetrics();
        this.loadCategoryName(data.categoryId);
        
        this.loadNotes(id);
      },
      error: (err) => console.error('Помилка завантаження цілі:', err)
    });
  }

  loadNotes(goalId: string): void {
    this.noteService.getNotesByGoal(goalId).subscribe({
      next: (data) => {
        this.notes = data;
        this.updateRecentNotes();
        this.generateCalendar(); 
      },
      error: (err) => {
        console.error('Помилка завантаження нотаток:', err);
        this.generateCalendar(); 
      }
    });
  }

  private updateRecentNotes(): void {
    this.recentNotes = [...this.notes]
      .sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())
      .slice(0, 5);
  }

  startAddingStep(): void {
    this.isAddingStep = true;
    this.newStepDescription = '';
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

  requestAiSteps(): void {
    if (!this.goal) return;

    this.isAiLoading = true;
    this.aiStepsSuggestion = null;
    this.showAiStepsSuggestion = false;

    this.aiService.getStepsSuggestion({
      title: this.goal.title,
      categoryName: this.categoryName,
      description: this.goal.description,
      startDate: this.goal.startDate,
      deadline: this.goal.deadline,
      existingSteps: this.goal.steps.map(s => s.description)
    }).subscribe({
      next: (suggestion) => {
        this.aiStepsSuggestion = suggestion;
        this.showAiStepsSuggestion = true;
        this.isAiLoading = false;
      },
      error: (err) => {
        console.error('Помилка AI-рекомендації кроків:', err);
        alert('Не вдалося згенерувати кроки. Спробуйте ще раз.');
        this.isAiLoading = false;
      }
    });
  }

  acceptAiSteps(): void {
    if (!this.goal || !this.aiStepsSuggestion) return;

    this.goalService.addStepsBatch(this.goal.goalId, this.aiStepsSuggestion.steps).subscribe({
      next: (steps) => {
        this.goal?.steps.push(...steps);
        this.rejectAiSteps();
      },
      error: (err) => {
        console.error('Помилка додавання кроків:', err);
        alert('Не вдалося додати кроки.');
      }
    });
  }

  rejectAiSteps(): void {
    this.showAiStepsSuggestion = false;
    this.aiStepsSuggestion = null;
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
    if (!this.goal || this.draggedStepIndex === null || this.draggedStepIndex === targetIndex) {
      this.resetDragState();
      return;
    }

    const steps = [...this.goal.steps];
    const [moved] = steps.splice(this.draggedStepIndex, 1);
    steps.splice(targetIndex, 0, moved);
    this.goal.steps = steps;

    const orderedIds = steps.map(s => s.stepId);
    this.goalService.reorderSteps(this.goal.goalId, orderedIds).subscribe({
      next: (reordered) => {
        if (this.goal) {
          this.goal.steps = reordered;
        }
      },
      error: (err) => {
        console.error('Помилка зміни порядку кроків:', err);
        this.loadGoal(this.goal!.goalId);
      }
    });

    this.resetDragState();
  }

  onStepDragEnd(): void {
    this.resetDragState();
  }

  private resetDragState(): void {
    this.draggedStepIndex = null;
    this.dragOverIndex = null;
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
      this.calendarDays.push({ 
        dayNumber: null, fullDate: null, isToday: false, isWeekend: false, isInPeriod: false, hasNote: false 
      });
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

      const dateStr = this.formatDate(currentIteratedDate);
      const hasNote = this.notes.some(n => n.date === dateStr);

      this.calendarDays.push({ 
        dayNumber: i, 
        fullDate: currentIteratedDate,
        isToday: isToday, 
        isWeekend: isWeekend, 
        isInPeriod: isInPeriod,
        hasNote: hasNote 
      });
    }
  }

  changeMonth(offset: number): void {
    const newDate = new Date(this.displayDate);
    newDate.setMonth(newDate.getMonth() + offset);
    this.displayDate = newDate;
    this.generateCalendar();
  }

  openNoteModal(day: CalendarDay): void {
    if (!day.fullDate) return;

    this.selectedDate = day.fullDate;
    const dateStr = this.formatDate(this.selectedDate);
    
    const existingNote = this.notes.find(n => n.date === dateStr);
    
    if (existingNote) {
      this.currentNoteContent = existingNote.content;
      this.editingNoteId = existingNote.noteId || null;
    } else {
      this.currentNoteContent = '';
      this.editingNoteId = null;
    }

    this.isModalOpen = true;
  }

  closeModal(): void {
    this.isModalOpen = false;
    this.selectedDate = null;
    this.currentNoteContent = '';
  }

  saveNote(): void {
    if (!this.selectedDate || !this.goal) return;
    
    const dateStr = this.formatDate(this.selectedDate);
    const content = this.currentNoteContent.trim();

    if (!content) {
        alert("Нотатка не може бути порожньою");
        return; 
    }

    const noteToSave: Note = {
        goalId: this.goal.goalId,
        date: dateStr,
        content: content
    };

    this.noteService.saveNote(noteToSave).subscribe({
        next: (savedNote) => {
            this.notes = this.notes.filter(n => n.date !== dateStr);
            this.notes.push(savedNote);
            
            this.updateRecentNotes();
            this.generateCalendar(); 
            this.closeModal();
        },
        error: (err) => {
            console.error('Помилка збереження:', err);
            alert('Не вдалося зберегти нотатку');
        }
    });
  }

  private formatDate(date: Date): string {
    const offset = date.getTimezoneOffset();
    const dateLocal = new Date(date.getTime() - (offset * 60 * 1000));
    return dateLocal.toISOString().split('T')[0];
  }

  openNoteFromList(note: Note): void {
    const dateParts = note.date.split('-');
    const newDate = new Date(
      parseInt(dateParts[0]), 
      parseInt(dateParts[1]) - 1, 
      parseInt(dateParts[2])
    );

    this.selectedDate = newDate;
    this.currentNoteContent = note.content;
    this.editingNoteId = note.noteId || null;
    this.isModalOpen = true;
  }

  deleteNote(): void {
    if (!this.editingNoteId) {
      this.closeModal();
      return;
    }

    if (confirm('Видалити цю нотатку назавжди?')) {
      this.noteService.deleteNote(this.editingNoteId).subscribe({
        next: () => {
          this.notes = this.notes.filter(n => n.noteId !== this.editingNoteId);
          
          this.updateRecentNotes();
          this.generateCalendar();
          this.closeModal();
        },
        error: (err) => {
          console.error('Помилка видалення:', err);
          alert('Не вдалося видалити нотатку');
        }
      });
    }
  }
}