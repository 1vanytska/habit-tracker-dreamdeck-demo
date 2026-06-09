import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService, UserProfile } from '../user.service';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-edit-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-profile.html',
  styleUrls: ['./edit-profile.css']
})
export class EditProfileComponent implements OnInit {

  readonly DEFAULT_AVATAR = '/assets/images/user.svg';

  userProfile: UserProfile = {
    username: '',
    email: '',
    description: '',
    profilePicture: null
  };

  currentImageDisplay: string = this.DEFAULT_AVATAR;
  isLoading: boolean = true;
  isSaving: boolean = false;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadUserProfile();
  }

  loadUserProfile(): void {
    this.userService.getCurrentUser().subscribe({
      next: (user) => {
        if (!user) {
          console.warn('Користувач не знайдений');
          this.currentImageDisplay = this.DEFAULT_AVATAR;
          this.isLoading = false;
          this.cdr.detectChanges();
          return;
        }

        this.userProfile = user;
        
        if (!this.userProfile.profilePicture || this.userProfile.profilePicture.trim() === '') {
          this.userProfile.profilePicture = this.DEFAULT_AVATAR;
        }
        
        this.currentImageDisplay = this.userProfile.profilePicture;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Помилка завантаження профілю:', err);
        alert('Не вдалося завантажити профіль');
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  saveProfile(): void {
    if (!this.userProfile.username.trim()) {
      alert('Помилка: Введіть ім\'я користувача!');
      return;
    }

    this.isSaving = true;
    this.cdr.detectChanges();

    const pictureToSave = this.userProfile.profilePicture === this.DEFAULT_AVATAR 
                          ? null 
                          : this.userProfile.profilePicture;

    const updateData = {
      username: this.userProfile.username,
      description: this.userProfile.description,
      profilePicture: pictureToSave
    };

    this.userService.updateProfile(updateData).subscribe({
      next: () => {
        alert('Профіль успішно оновлено! Оскільки username змінився, будь ласка, увійдіть знову.');
        this.isSaving = false;
        this.cdr.detectChanges();
        
        this.authService.logout(); 
      },
      error: (err) => {
        console.error('Помилка оновлення:', err);
        alert(err.error?.message || 'Не вдалося оновити профіль.');
        this.isSaving = false;
        this.cdr.detectChanges();
      }
    });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      if (file.size > 2 * 1024 * 1024) {
        alert('Файл занадто великий. Максимальний розмір - 2MB');
        return;
      }

      const reader = new FileReader();
      reader.onload = (e: any) => {
        const base64String = e.target.result;
        this.currentImageDisplay = base64String;
        this.userProfile.profilePicture = base64String;
        this.cdr.detectChanges();
      };
      reader.readAsDataURL(file);
    }
  }
}