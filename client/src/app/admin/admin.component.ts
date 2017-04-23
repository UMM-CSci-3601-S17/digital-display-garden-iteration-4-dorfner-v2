import { Component, OnInit } from '@angular/core';
import { AdminService } from './admin.service';


@Component({
    selector: 'admin-component',
    templateUrl: 'admin-component.html',
})

export class AdminComponent implements OnInit {
    url : String = API_URL;
    constructor(private adminService: AdminService) {
        this.adminService.authorized().subscribe(authorized => {
            if (!authorized) {
                // redirect to the backend for authorization
                window.location.href = this.url + "authorize?originating_url="+window.location.href;
            }
        });
    }

    ngOnInit(): void {

    }
}