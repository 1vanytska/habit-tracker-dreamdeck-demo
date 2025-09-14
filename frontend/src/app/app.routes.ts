import { Routes } from '@angular/router';
import { Home } from './home/home';
import { Goal } from './goal/goal';
import { EditGoal } from './edit-goal/edit-goal';

export const routes: Routes = [
    { path: 'home', component: Home},
    { path: 'goal/:id', component: Goal},
    { path: 'edit-goal/:id', component: EditGoal}
];
