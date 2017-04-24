import { Component, OnInit } from '@angular/core';
import { AdminService } from './admin.service';


@Component({
    selector: 'incorrect-account-component',
    templateUrl: 'incorrect-account.component.html',
})

export class IncorrectAccountComponent implements OnInit {
    url : String = API_URL;
    constructor() {
    }

    ngOnInit(): void {
    }
}