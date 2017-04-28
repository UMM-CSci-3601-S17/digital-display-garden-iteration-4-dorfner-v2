import {Component, OnInit, ViewChild} from '@angular/core';
import { AdminService } from './admin.service';


@Component({
    selector: 'import-component',
    templateUrl: 'import.component.html',
})

export class ImportComponent implements OnInit {

    @ViewChild('fu') fu;

    authorized: boolean;
    filename:string;
    uploadStarted:boolean = false;
    uploadAttempted:boolean = false;

    constructor(private adminService: AdminService){
    }

    handleUpload(){
        this.uploadStarted = true;
        this.fu.upload().subscribe(
            response => {
                this.filename = response.json();
                this.uploadAttempted = true;
            },
            err => {
                this.uploadAttempted = true;
                if (err.status === 403) {
                    window.location.reload();
                }
            }

        );
    }

    fileSelected(): void {
        this.filename = undefined;
        this.uploadAttempted = false;
        this.uploadStarted = false
    }

    ngOnInit(): void {
        this.adminService.authorized().subscribe(authorized => this.authorized = authorized);

    }
}
