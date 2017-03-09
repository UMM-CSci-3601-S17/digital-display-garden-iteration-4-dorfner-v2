// Imports
import { ModuleWithProviders }  from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { KittensComponent } from './kittens/kittens.component';
import {UserListComponent} from "./users/user-list.component";
import {PlantListComponent} from "./plants/plant-list.component";
import {DialogComponent} from "./dialog/dialog.component";
// Route Configuration
export const routes: Routes = [
    { path: '', component: PlantListComponent }
];

export const routing: ModuleWithProviders = RouterModule.forRoot(routes);