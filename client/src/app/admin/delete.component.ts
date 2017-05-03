import { Component, OnInit } from '@angular/core';
import { AdminService } from './admin.service';
import {Positioning} from "angular2-bootstrap-confirm/position";
import {ConfirmOptions, Position} from 'angular2-bootstrap-confirm';


@Component({
    selector: 'delete-component',
    templateUrl: 'delete.component.html',
    providers: [
        ConfirmOptions,
        {provide: Position, useClass: Positioning}],
})

export class DeleteComponent implements OnInit {

    // private url: string = API_URL + "deleteData/";
    authorized: boolean;
    uploadIDs: string[];
    private liveUploadID: string;

    constructor(private adminService: AdminService) {

    }

    delete(uploadID : string)
    {
        this.adminService.deleteUploadID(uploadID)
            .subscribe(
                response => {
                    if (response.success === true) {
                        this.uploadIDs = response.uploadIDs;
                    }
                },
                err => {
                    if(err.status === 403) {
                        window.location.reload();
                    }
                }
            );
    }

    ngOnInit(): void {
        this.adminService.getUploadIDs()
            .subscribe(result => this.uploadIDs = result, err => console.log(err));
        this.adminService.getLiveUploadID()
            .subscribe(result => this.liveUploadID = result, err => console.log(err));
        this.adminService.authorized().subscribe(authorized => this.authorized = authorized);


    }
}