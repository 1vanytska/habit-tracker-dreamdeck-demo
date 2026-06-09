import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { Home } from './home';
import { GoalService } from '../goal/goal.service';
import { UserService } from '../user/user.service';

describe('Home', () => {
  let component: Home;
  let fixture: ComponentFixture<Home>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Home],
      providers: [
        provideRouter([]),
        {
          provide: GoalService,
          useValue: {
            getGoalsByUserId: () => of([])
          }
        },
        {
          provide: UserService,
          useValue: {
            getCurrentUser: () => of({ id: 'user-1', username: 'test', email: 't@t.com' })
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Home);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
