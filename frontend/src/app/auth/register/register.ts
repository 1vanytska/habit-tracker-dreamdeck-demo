import { Component, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../auth/auth.service';
import { RegisterRequest } from '../../auth/auth.model';
import { validatePassword } from '../../shared/password.utils';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrls: ['../auth-shared.css']
})
export class Register {
  registerData: RegisterRequest = { username: '', email: '', password: '' };
  isLoading = false;
  passwordError: string | null = null;

  constructor(
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  onSubmit() {
    this.passwordError = validatePassword(this.registerData.password);
    if (this.passwordError) {
      this.cdr.markForCheck();
      return;
    }

    this.isLoading = true;
    this.authService.register(this.registerData).subscribe({
      next: () => {
        this.router.navigate(['/home']);
      },
      error: (err) => {
        console.error(err);
        alert(err.error?.message || 'Помилка реєстрації! Можливо, такий email вже існує.');
        this.isLoading = false;
      }
    });
  }
}
