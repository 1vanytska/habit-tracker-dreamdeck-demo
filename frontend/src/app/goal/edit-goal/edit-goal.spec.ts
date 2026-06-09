import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { EditGoal } from './edit-goal';
import { GoalService } from '../goal.service';
import { CategoryService } from '../../category/category.service';
import { UserService } from '../../user/user.service';

describe('EditGoal', () => {
  let component: EditGoal;
  let fixture: ComponentFixture<EditGoal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditGoal],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: () => null
              }
            }
          }
        },
        {
          provide: GoalService,
          useValue: jasmine.createSpyObj('GoalService', ['getGoalById', 'createGoal', 'updateGoal'])
        },
        {
          provide: CategoryService,
          useValue: {
            getAllCategories: () => of([])
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

    fixture = TestBed.createComponent(EditGoal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
