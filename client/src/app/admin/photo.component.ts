import { Component, OnInit } from '@angular/core';
import { AdminService } from './admin.service';


@Component({
    // selector: 'photo-component',
    templateUrl: 'photo.component.html',
})
export class PhotoComponent implements OnInit {



    constructor (private adminService: AdminService) {
        
    }

    ngOnInit(): void{

    }
}