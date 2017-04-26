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
import {AuthGuard} from "./admin/auth-guard";
import {IncorrectAccountComponent} from "./admin/incorrect-account.component";
import {PhotoComponent} from "./admin/photo.component";
import {SlowLoginComponent} from "./admin/slow-login.component";

// Route Configuration
export const routes: Routes = [
    { path: '', component: HomepageComponent },
    { path: 'plants/:plantID', component: PlantComponent },
    { path: 'admin', component: AdminComponent, canActivate: ['CanAlwaysActivateGuard', AuthGuard]},
    { path: 'admin/exportData', component: ExportComponent, canActivate: ['CanAlwaysActivateGuard', AuthGuard]},
    { path: 'admin/importData', component: ImportComponent, canActivate: ['CanAlwaysActivateGuard', AuthGuard]},
    { path: 'admin/deleteData', component: DeleteComponent, canActivate: ['CanAlwaysActivateGuard', AuthGuard]},
    { path: 'admin/incorrectAccount', component: IncorrectAccountComponent},
    { path: 'admin/slowLogin', component: SlowLoginComponent},
    { path: 'bed/:gardenLocation', component: BedComponent },
    { path: 'admin/PhotoComponent', component: PhotoComponent },
    { path: '**', component: NotFoundComponent},
];

export const routing: ModuleWithProviders = RouterModule.forRoot(routes);