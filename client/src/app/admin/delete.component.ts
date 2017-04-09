import { Component, OnInit } from '@angular/core';
import { AdminService } from './admin.service';


@Component({
    selector: 'delete-component',
    templateUrl: 'delete.component.html',
})

export class DeleteComponent implements OnInit {

    // private url: string = API_URL + "deleteData/";

    private uploadIds: string[];
    private liveUploadId: string;

    constructor(private adminService: AdminService) {

    }

    delete(uploadID : string)
    {
        this.adminService.deleteUploadId(uploadID)
            .subscribe(x => this.adminService.getUploadIds()
                .subscribe(result => this.uploadIds = result, err => console.log(err)));
    }

    ngOnInit(): void {
        this.adminService.getUploadIds()
            .subscribe(result => this.uploadIds = result, err => console.log(err));
        this.adminService.getLiveUploadId()
            .subscribe(result => this.liveUploadId = result, err => console.log(err));

    }
}