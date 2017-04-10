import { Component, OnInit } from '@angular/core';
import { PlantListService } from "../plants/plant-list.service";
import { Plant } from "../plants/plant";
import {Params, ActivatedRoute, Router} from "@angular/router";


@Component({
    selector: 'garden-navbar-component',
    templateUrl: 'garden-navbar.component.html'
})

export class GardenNavbarComponent {
    public bed : string;
    public plants: Plant[];
    public locations: Plant[];

    constructor(private plantListService: PlantListService, private route: ActivatedRoute, private router: Router) {
        //Get the bed from the params of the route

    }

    ngOnInit(): void{
        this.router.events.subscribe((val) => {
            this.bed = this.route.snapshot.params["gardenLocation"];
            this.refreshInformation();
        });
    }

    refreshInformation() : void
    {
        this.plantListService.getGardenLocations().subscribe(
            locations => this.locations = locations,
            err => {
                console.log(err);
            }
        );
    }
}