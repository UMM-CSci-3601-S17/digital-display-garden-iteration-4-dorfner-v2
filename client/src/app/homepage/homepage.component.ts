import { Component, OnInit } from '@angular/core';
import { PlantListService } from "../plants/plant-list.service";
import { Plant } from "../plants/plant";
import {Params, ActivatedRoute, Router} from "@angular/router";
import { FilterBy } from "../plants/filter.pipe";

@Component({
    selector: 'homepage-component',
    templateUrl: 'homepage.component.html',
    providers: [ FilterBy ]
})

export class HomepageComponent implements OnInit {
    public bed : string;
    public plants: Plant[]; // not used - so delete it
    public locations: Plant[];
    public currentCN: string;
    public currentList: Plant[];

    constructor(private plantListService: PlantListService, private router: Router) {
        this.refreshInformation();
    }


    ngOnInit(): void{
        this.plantListService.getPlants().subscribe(
            currentList => this.currentList = currentList,
            err => {
                console.log(err);
            }
        );
    }

    // Get rid of this method and move `this.plantListService.getGardenLocations().subscribe(`
    // to the constructor. Also, get rid of `plants[]`
    refreshInformation() : void
    {
        this.plantListService.getFlowersByBed(this.bed).subscribe (
            plants => this.plants = plants,
            err => {
                console.log(err);
            }
        );

        this.plantListService.getGardenLocations().subscribe(
            locations => this.locations = locations,
            err => {
                console.log(err);
            }
        );
    }
}