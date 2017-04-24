// Imports
import { ModuleWithProviders }  from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {PlantComponent} from "./plants/plant.component";
import {AdminComponent} from "./admin/admin.component";
import {ExportComponent} from "./admin/export.component";
import {ImportComponent} from "./admin/import.component";
import {BedComponent} from "./plants/bed.component";
import {HomepageComponent} from "./homepage/homepage.component";
import {DeleteComponent} from "./admin/delete.component";
import {NotFoundComponent} from "./not-found.component";
import {PhotoComponent} from "./admin/photo.component";

// Route Configuration
export const routes: Routes = [
    { path: '', component: HomepageComponent },
    { path: 'plants/:plantID', component: PlantComponent },
    { path: 'admin', component: AdminComponent},
    { path: 'admin/exportData', component: ExportComponent},
    { path: 'admin/importData', component: ImportComponent},
    { path: 'admin/deleteData', component: DeleteComponent },
    { path: 'bed/:gardenLocation', component: BedComponent },
    { path: 'admin/PhotoComponent', component: PhotoComponent },
    { path: '**', component: NotFoundComponent},
];

export const routing: ModuleWithProviders = RouterModule.forRoot(routes);