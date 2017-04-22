import {Component, OnInit, ViewChild} from '@angular/core';
import { AdminService } from './admin.service';


@Component({
    selector: 'import-component',
    templateUrl: 'import.component.html',
})

export class ImportComponent implements OnInit {

    @ViewChild('fu') fu;

    filename:string;
    uploadStarted:boolean = false;
    uploadAttempted:boolean = false;

    handleUpload(){
        this.uploadStarted = true;
        this.fu.upload().subscribe(
            response => {
                this.filename = response.json();
                this.uploadAttempted = true;
            },
            err => {
                this.uploadAttempted = true;
            }

        );
    }

    ngOnInit(): void {

    }
}
