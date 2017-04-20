import { Component, OnInit } from '@angular/core';
import { AdminService } from './admin.service';


@Component({
    // selector: 'photo-component',
    templateUrl: 'photo.component.html',
})
export class PhotoComponent implements OnInit {
public id: string;


    constructor (private adminService: AdminService) {
        
    }

    public getPlant(){
        this.id = (<HTMLInputElement>document.getElementById("plantID")).value;
    }


    ngOnInit(): void{

    }
}