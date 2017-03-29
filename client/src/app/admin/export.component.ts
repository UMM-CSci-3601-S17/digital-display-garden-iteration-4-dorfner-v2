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

    constructor(private adminService: AdminService) {

    }

    ngOnInit(): void {
        this.adminService.getUploadIds()
            .subscribe(result => this.uploadIds = result, err => console.log(err));
        this.adminService.getLiveUploadId()
            .subscribe(result => this.liveUploadId = result, err => console.log(err));
    }
}