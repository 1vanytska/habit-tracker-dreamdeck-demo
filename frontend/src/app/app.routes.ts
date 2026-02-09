import { Routes } from '@angular/router';
import { Login } from './auth/login/login'; 
import { Register } from './auth/register/register';
import { Home } from './home/home';
import { Goal } from './goal/goal';
import { EditGoal } from './goal/edit-goal/edit-goal';
import { authGuard } from './auth/auth.guard';

export const routes: Routes = [
    { path: 'login', component: Login },
    { path: 'register', component: Register },
    { path: 'home', component: Home},
    { path: 'goal/:id', component: Goal, canActivate: [authGuard] },
    { path: 'create-goal', component: EditGoal, canActivate: [authGuard] },
    { path: 'edit-goal/:id', component: EditGoal, canActivate: [authGuard] },
    { path: '', redirectTo: 'home', pathMatch: 'full' },
    { path: '**', redirectTo: 'login' } 
];