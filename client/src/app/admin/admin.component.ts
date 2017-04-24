import { Component, OnInit } from '@angular/core';
import { AdminService } from './admin.service';


@Component({
    selector: 'admin-component',
    templateUrl: 'admin-component.html',
})

export class AdminComponent implements OnInit {
    authorized : boolean;
    url : String = API_URL;
    constructor(private adminService: AdminService ) {
    }

    ngOnInit(): void {
        this.adminService.authorized().subscribe(authorized => this.authorized = authorized);
    }
}