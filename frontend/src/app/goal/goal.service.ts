import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Goal, Step } from './goal.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class GoalService {
  private apiUrl = `${environment.apiUrl}/goals`;
  private stepsUrl = `${environment.apiUrl}/steps`;

  constructor(private http: HttpClient) { }

  getGoalById(id: string): Observable<Goal> {
    return this.http.get<Goal>(`${this.apiUrl}/${id}`);
  }

  updateGoal(id: string, goal: Goal): Observable<Goal> {
    return this.http.put<Goal>(`${this.apiUrl}/${id}`, goal);
  }

  createGoal(goal: Goal): Observable<Goal> {
    return this.http.post<Goal>(this.apiUrl, goal);
  }

  getGoalsByUserId(userId: string): Observable<Goal[]> {
    return this.http.get<Goal[]>(`${this.apiUrl}/user/${userId}`);
  }

  addStep(goalId: string, description: string): Observable<Step> {
    return this.http.post<Step>(`${this.stepsUrl}/${goalId}`, { description });
  }

  toggleStep(stepId: string): Observable<Step> {
    return this.http.put<Step>(`${this.stepsUrl}/${stepId}/toggle`, {});
  }
  
  deleteStep(stepId: string): Observable<void> {
    return this.http.delete<void>(`${this.stepsUrl}/${stepId}`);
  }
}