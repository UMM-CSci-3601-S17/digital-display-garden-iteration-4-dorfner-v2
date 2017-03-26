import { Component, OnInit } from '@angular/core';
import { PlantListService } from "./plant-list.service";
import { Plant } from "./plant";
import {Params, ActivatedRoute} from "@angular/router";

@Component({
    selector: 'bed-component',
    templateUrl: 'bed.component.html',
})

export class BedComponent implements OnInit {
    public bed : string;
    public plants: Plant[];

    constructor(private plantListService: PlantListService, private route: ActivatedRoute) {
        // this.plants = this.plantListService.getPlants()

        //Get the bed from the params of the route
        this.bed = this.route.snapshot.params["gardenLocation"];
    }



    ngOnInit(): void {
        //Form the URL for the plant list request for this bed
        let filterUrl = "?gardenLocation=" + this.bed;

        this.plantListService.getFlowersByFilter(filterUrl).subscribe (
            plants => this.plants = plants,
            err => {
                console.log(err);
            }
        );

    }
}
