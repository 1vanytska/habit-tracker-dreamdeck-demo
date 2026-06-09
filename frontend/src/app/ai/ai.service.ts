import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import {
  SmartGoalSuggestionRequest,
  SmartGoalSuggestionResponse,
  StepsSuggestionRequest,
  StepsSuggestionResponse
} from './ai.model';

@Injectable({
  providedIn: 'root'
})
export class AiService {
  private apiUrl = `${environment.apiUrl}/ai`;

  constructor(private http: HttpClient) {}

  getSmartGoalSuggestion(request: SmartGoalSuggestionRequest): Observable<SmartGoalSuggestionResponse> {
    return this.http.post<SmartGoalSuggestionResponse>(`${this.apiUrl}/smart-goal-suggestion`, request);
  }

  getStepsSuggestion(request: StepsSuggestionRequest): Observable<StepsSuggestionResponse> {
    return this.http.post<StepsSuggestionResponse>(`${this.apiUrl}/steps-suggestion`, request);
  }
}
