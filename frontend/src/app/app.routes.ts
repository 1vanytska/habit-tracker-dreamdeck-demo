import { Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';
import { Home } from './home/home';
import { Goal } from './goal/goal';
import { EditGoal } from './goal/edit-goal/edit-goal';
import { ComingSoonComponent } from './coming-soon/coming-soon.component';
import { authGuard } from './auth/auth.guard';
import { EditProfileComponent } from './user/edit-profile/edit-profile';

export const routes: Routes = [
    { path: 'login', component: Login },
    { path: 'register', component: Register },
    { path: 'home', component: Home, canActivate: [authGuard]},
    { path: 'goal/:id', component: Goal, canActivate: [authGuard] },
    { path: 'create-goal', component: EditGoal, canActivate: [authGuard] },
    { path: 'edit-goal/:id', component: EditGoal, canActivate: [authGuard] },
    { path: 'edit-profile', component: EditProfileComponent, canActivate: [authGuard] },
    { path: 'coming-soon', component: ComingSoonComponent, canActivate: [authGuard] },
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: '**', redirectTo: 'coming-soon' }
];