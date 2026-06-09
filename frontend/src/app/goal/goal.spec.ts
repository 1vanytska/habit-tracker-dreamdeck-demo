import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { Goal } from './goal';
import { Goal as GoalModel } from './goal.model';

describe('Goal', () => {
  let component: Goal;
  let fixture: ComponentFixture<Goal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Goal],
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
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Goal);
    component = fixture.componentInstance;
    component.goal = {
      goalId: '1',
      userId: 'user-1',
      title: 'Запустити проект',
      description: 'Почати роботу над новим сервісом та підготувати реліз',
      categoryId: 'cat-1',
      picture: null,
      isPublic: true,
      startDate: '2026-06-01',
      deadline: '2026-06-30',
      status: 'IN_PROGRESS',
      isArchived: false,
      archivingTime: null,
      steps: []
    } as GoalModel;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should toggle description hover state and apply expanded class', () => {
    const wrapper = fixture.nativeElement.querySelector('.goal-description-wrapper');

    wrapper.dispatchEvent(new Event('mouseenter'));
    fixture.detectChanges();

    expect(component.isDescriptionHovered).toBeTrue();
    expect(fixture.nativeElement.querySelector('.goal-header').classList).toContain('description-expanded');

    wrapper.dispatchEvent(new Event('mouseleave'));
    fixture.detectChanges();

    expect(component.isDescriptionHovered).toBeFalse();
  });
});
