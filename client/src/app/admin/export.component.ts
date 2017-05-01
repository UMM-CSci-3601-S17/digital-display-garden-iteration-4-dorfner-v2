import { Component, OnInit } from '@angular/core';
import { AdminService } from './admin.service';


@Component({
    selector: 'export-component',
    templateUrl: 'export.component.html',
})

export class ExportComponent implements OnInit {

    private url: string = API_URL + "export?uploadId=";

    private uploadIds: string[];
    private liveUploadId: string;
    authorized: boolean;

    constructor(private adminService: AdminService) {

    }

    ngOnInit(): void {
        this.adminService.getUploadIDs()
            .subscribe(result => this.uploadIds = result, err => console.log(err));
        this.adminService.getLiveUploadID()
            .subscribe(result => this.liveUploadId = result, err => console.log(err));
        this.adminService.authorized().subscribe(authorized => this.authorized = authorized);

    }
}