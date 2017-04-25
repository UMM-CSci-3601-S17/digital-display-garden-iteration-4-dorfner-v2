import { NgModule }       from '@angular/core';
import { BrowserModule }  from '@angular/platform-browser';
import { HttpModule, JsonpModule } from '@angular/http';

import { AppComponent }         from './app/app.component';
import { NavbarComponent } from './app/navbar/navbar.component';
import { GardenNavbarComponent } from './app/navbar/garden-navbar.component';
import { BedComponent } from './app/plants/bed.component';
import { PlantComponent } from './app/plants/plant.component';
import { PlantListService } from './app/plants/plant-list.service';
import { routing } from './app/app.routes';
import { FormsModule } from '@angular/forms';

import { PipeModule } from './pipe.module';
import {AdminComponent} from "./app/admin/admin.component";
import {ExportComponent} from "./app/admin/export.component";
import {AdminService} from "./app/admin/admin.service";
import {ImportComponent} from "./app/admin/import.component";
import {FileUploadComponent} from "./app/admin/file-upload.component";
import {HomepageComponent} from "./app/homepage/homepage.component";
import {DeleteComponent} from "./app/admin/delete.component";
import {NotFoundComponent} from "./app/not-found.component"
import {PhotoComponent} from "./app/admin/photo.component";

import {ConfirmOptions, Position, ConfirmModule} from 'angular2-bootstrap-confirm';
import {Positioning} from 'angular2-bootstrap-confirm/position';
import {AuthGuard} from "./app/admin/auth-guard";
import {IncorrectAccountComponent} from "./app/admin/incorrect-account.component";




@NgModule({
    imports: [
        ConfirmModule,
        BrowserModule,
        HttpModule,
        JsonpModule,
        routing,
        FormsModule,
        PipeModule,
    ],
    declarations: [
        AppComponent,
        NavbarComponent,
        GardenNavbarComponent,
        PlantComponent,
        AdminComponent,
        ExportComponent,
        ImportComponent,
        DeleteComponent,
        FileUploadComponent,
        BedComponent,
        HomepageComponent,
        NotFoundComponent,
        IncorrectAccountComponent,
        PhotoComponent,

    ],
    providers: [ PlantListService, AdminService, {provide: 'CanAlwaysActivateGuard', useClass: AuthGuard}, AuthGuard ],
    bootstrap: [ AppComponent ]
})

export class AppModule {}