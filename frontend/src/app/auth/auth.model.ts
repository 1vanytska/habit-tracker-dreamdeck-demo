export interface AuthResponse {
  access_token: string;
  refresh_token: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

export interface LoginRequest {
  login: string;
  password: string;
}

export interface User {
  id: string;
  email: string;
  username: string;
  role: string;
  // Додай інші поля, якщо треба (profilePicture тощо)
}