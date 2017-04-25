import { Component, OnInit, ViewChild } from '@angular/core';
import { PlantListService } from "./plant-list.service";
import { Plant } from "./plant";
import { FilterBy } from "../plants/filter.pipe";
import {Params, ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'bed-component',
    templateUrl: 'bed.component.html',
    providers: [ FilterBy ]
})

export class BedComponent implements OnInit {
    public bed : string;
    public plants: Plant[] = [];
    @ViewChild('nav') nav;

    constructor(private plantListService: PlantListService, public route: ActivatedRoute, private router: Router) {
        // this.plants = this.plantListService.getPlants()

        //Get the bed from the params of the route
        this.router.events.subscribe((val) => {
            this.bed = this.route.snapshot.params["gardenLocation"];
            this.refreshInformation();
        });
    }


    ngOnInit(): void{
    }

    refreshInformation() : void
    {
        this.plantListService.getFlowersByBed(this.bed).subscribe (
            plants => this.plants = plants,
            err => {
                console.log(err);
            }
        );

    }

    emptySearch() {
        this.nav.currentCN = "";
    }
}
