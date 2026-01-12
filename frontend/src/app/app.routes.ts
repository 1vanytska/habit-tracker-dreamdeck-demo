import { Routes } from '@angular/router';
import { Home } from './home/home';
import { Goal } from './goal/goal';
import { EditGoal } from './goal/edit-goal/edit-goal';

export const routes: Routes = [
    { path: '', redirectTo: 'home', pathMatch: 'full' },
    { path: 'home', component: Home},
    { path: 'goal/:id', component: Goal },
    { path: 'create-goal', component: EditGoal },
    { path: 'edit-goal/:id', component: EditGoal}
];
