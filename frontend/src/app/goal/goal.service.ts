import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Goal } from './goal.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class GoalService {
  private apiUrl = `${environment.apiUrl}/goals`;

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
}