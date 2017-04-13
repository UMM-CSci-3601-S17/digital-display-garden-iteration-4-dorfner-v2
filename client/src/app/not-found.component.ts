import { Component, OnInit } from '@angular/core';


@Component({
    selector: 'not-found-component',
    templateUrl: 'not-found.component.html',
})

export class NotFoundComponent implements OnInit {
    url : String = API_URL;
    constructor() {

    }

    ngOnInit(): void {

    }
}