export interface SmartGoalSuggestionRequest {
  title: string;
  categoryName?: string;
  description?: string;
  startDate?: string;
  deadline?: string;
}

export interface SmartGoalSuggestionResponse {
  suggestedTitle?: string;
  description: string;
  steps: string[];
  fromAi: boolean;
}

export interface StepsSuggestionRequest {
  title: string;
  categoryName?: string;
  description?: string;
  startDate?: string;
  deadline?: string;
  existingSteps?: string[];
}

export interface StepsSuggestionResponse {
  steps: string[];
  fromAi: boolean;
}
