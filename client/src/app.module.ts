import { NgModule }       from '@angular/core';
import { BrowserModule }  from '@angular/platform-browser';
import { HttpModule, JsonpModule } from '@angular/http';

import { AppComponent }         from './app/app.component';
import { NavbarComponent } from './app/navbar/navbar.component';
import { BedComponent } from './app/plants/bed.component';
import { PlantListComponent } from './app/plants/plant-list.component';
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




@NgModule({
    imports: [
        BrowserModule,
        HttpModule,
        JsonpModule,
        routing,
        FormsModule,
        PipeModule
    ],
    declarations: [
        AppComponent,
        NavbarComponent,
        PlantListComponent,
        PlantComponent,
        AdminComponent,
        ExportComponent,
        ImportComponent,
        FileUploadComponent,
        BedComponent

    ],
    providers: [ PlantListService, AdminService ],
    bootstrap: [ AppComponent ]
})

export class AppModule {}