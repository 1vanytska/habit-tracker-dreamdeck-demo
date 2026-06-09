import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';
import { Register } from './register';
import { AuthService } from '../auth.service';

describe('Register', () => {
  let component: Register;
  let fixture: ComponentFixture<Register>;
  let authService: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    authService = jasmine.createSpyObj('AuthService', ['register']);
    authService.register.and.returnValue(of({ access_token: 'a', refresh_token: 'b' }));

    await TestBed.configureTestingModule({
      imports: [Register],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Register);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should show password error only after invalid submit', () => {
    component.registerData.password = 'short';
    component.onSubmit();
    fixture.detectChanges();

    expect(component.passwordError).toContain('12 символів');
    expect(authService.register).not.toHaveBeenCalled();
  });

  it('should register when password is valid', () => {
    component.registerData = {
      username: 'user',
      email: 'user@example.com',
      password: 'validpassword!'
    };

    component.onSubmit();

    expect(component.passwordError).toBeNull();
    expect(authService.register).toHaveBeenCalled();
  });
});
