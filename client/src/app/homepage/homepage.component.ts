import { Component, OnInit } from '@angular/core';
import { PlantListService } from "../plants/plant-list.service";
import { Plant } from "../plants/plant";
import {Params, ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'homepage-component',
    templateUrl: 'homepage.component.html',
})

export class HomepageComponent implements OnInit {
    public bed : string;
    public plants: Plant[];
    public locations: Plant[];

    constructor(private plantListService: PlantListService, private router: Router) {
        this.refreshInformation();
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

        this.plantListService.getGardenLocations().subscribe(
            locations => this.locations = locations,
            err => {
                console.log(err);
            }
        );
    }


}
