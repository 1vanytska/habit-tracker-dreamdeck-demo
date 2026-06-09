import { Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';
import { Home } from './home/home';
import { Goal } from './goal/goal';
import { EditGoal } from './goal/edit-goal/edit-goal';
import { CategoryFormComponent } from './category/category-form/category-form';
import { ComingSoonComponent } from './coming-soon/coming-soon.component';
import { authGuard } from './auth/auth.guard';
import { EditProfileComponent } from './user/edit-profile/edit-profile';
import { About } from './about/about';
import { Contacts } from './contacts/contacts';
import { AchievementPlan } from './achievement-plan/achievement-plan';

export const routes: Routes = [
    { path: 'login', component: Login },
    { path: 'register', component: Register },
    { path: 'home', component: Home, canActivate: [authGuard]},
    { path: 'goal/:id', component: Goal, canActivate: [authGuard] },
    { path: 'create-goal', component: EditGoal, canActivate: [authGuard] },
    { path: 'edit-goal/:id', component: EditGoal, canActivate: [authGuard] },
    { path: 'edit-profile', component: EditProfileComponent, canActivate: [authGuard] },
    { path: 'categories/new', component: CategoryFormComponent, canActivate: [authGuard] },
    { path: 'about', component: About, canActivate: [authGuard] },
    { path: 'contacts', component: Contacts, canActivate: [authGuard] },
    { path: 'achievement-plan', component: AchievementPlan, canActivate: [authGuard] },
    { path: 'services', component: ComingSoonComponent, canActivate: [authGuard] },
    { path: 'coming-soon', component: ComingSoonComponent, canActivate: [authGuard] },
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: '**', redirectTo: 'coming-soon' }
];