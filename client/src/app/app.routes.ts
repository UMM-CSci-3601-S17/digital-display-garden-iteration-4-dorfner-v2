// Imports
import { ModuleWithProviders }  from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {PlantListComponent} from "./plants/plant-list.component";
import {PlantComponent} from "./plants/plant.component";
import {BedComponent} from "./plants/bed.component";

// Route Configuration
export const routes: Routes = [
    { path: '', component: PlantListComponent },
    { path: 'plants/:plantID', component: PlantComponent },
    { path: 'bed/:gardenLocation', component: BedComponent }
];

export const routing: ModuleWithProviders = RouterModule.forRoot(routes);