import { HttpInterceptorFn, HttpRequest, HttpHandlerFn, HttpErrorResponse } from '@angular/common/http';
import { inject, PLATFORM_ID } from '@angular/core'; // <--- Додай PLATFORM_ID
import { isPlatformBrowser } from '@angular/common'; // <--- Додай isPlatformBrowser
import { AuthService } from './auth.service';
import { catchError, switchMap, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn) => {
  const authService = inject(AuthService);
  const platformId = inject(PLATFORM_ID); // <--- Отримуємо ID платформи

  // Якщо ми на сервері — просто пропускаємо запит далі без змін
  if (!isPlatformBrowser(platformId)) {
    return next(req);
  }

  // Далі код такий самий, як був...
  const token = authService.getAccessToken();

  if (req.url.includes('/api/auth')) {
    return next(req);
  }

  let authReq = req;
  if (token) {
    authReq = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
  }

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 403) {
        return authService.refreshToken().pipe(
          switchMap((newTokens) => {
            const newAuthReq = req.clone({
              setHeaders: { Authorization: `Bearer ${newTokens.access_token}` }
            });
            return next(newAuthReq);
          }),
          catchError((refreshErr) => {
            authService.logout();
            return throwError(() => refreshErr);
          })
        );
      }
      return throwError(() => error);
    })
  );
};