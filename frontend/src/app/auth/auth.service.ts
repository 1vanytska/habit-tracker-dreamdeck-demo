import { Injectable, Inject, PLATFORM_ID } from '@angular/core'; // <--- Додай Inject та PLATFORM_ID
import { isPlatformBrowser } from '@angular/common'; // <--- Додай це
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { AuthResponse, LoginRequest, RegisterRequest } from './auth.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8081/api/auth';
  
  // Початкове значення false, щоб не лізти в localStorage до конструктора
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(
    private http: HttpClient, 
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object // <--- Інжектуємо ID платформи
  ) {
    // Перевіряємо токен тільки якщо ми в браузері
    if (isPlatformBrowser(this.platformId)) {
      this.isAuthenticatedSubject.next(this.hasToken());
    }
  }

  // --- Основні дії ---

  register(data: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, data).pipe(
      tap(response => this.saveTokens(response))
    );
  }

  login(data: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, data).pipe(
      tap(response => this.saveTokens(response))
    );
  }

  logout(): void {
    // Виконуємо тільки в браузері
    if (isPlatformBrowser(this.platformId)) {
      const refreshToken = localStorage.getItem('refresh_token');
      if (refreshToken) {
        this.http.post(`${this.apiUrl}/logout`, { token: refreshToken }).subscribe();
      }
      this.clearTokens();
    }
    this.router.navigate(['/login']);
  }

  // --- Робота з токенами (безпечна) ---

  private saveTokens(response: AuthResponse): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem('access_token', response.access_token);
      localStorage.setItem('refresh_token', response.refresh_token);
      this.isAuthenticatedSubject.next(true);
    }
  }

  private clearTokens(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('access_token');
      localStorage.removeItem('refresh_token');
      this.isAuthenticatedSubject.next(false);
    }
  }

  private hasToken(): boolean {
    if (isPlatformBrowser(this.platformId)) {
      return !!localStorage.getItem('access_token');
    }
    return false; // На сервері ми завжди "не залогінені"
  }

  getAccessToken(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem('access_token');
    }
    return null;
  }

  getRefreshToken(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem('refresh_token');
    }
    return null;
  }

  refreshToken(): Observable<AuthResponse> {
    const token = this.getRefreshToken();
    // Якщо токена немає (або ми на сервері), повертаємо помилку або пустий потік
    if (!token) {
        // Тут можна кинути помилку, яку спіймає інтерцептор
        throw new Error("No refresh token"); 
    }
    
    return this.http.post<AuthResponse>(`${this.apiUrl}/refresh`, { token }).pipe(
      tap(response => this.saveTokens(response))
    );
  }
}