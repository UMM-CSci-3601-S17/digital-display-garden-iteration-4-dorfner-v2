
import { PlantListService } from "./plant-list.service";
import { Plant } from "./plant";
import { Component, OnInit, Input} from '@angular/core';
import { Params, Route, ActivatedRoute} from '@angular/router';
import {Observable} from "rxjs";

@Component({
    selector: 'plant-component',
    templateUrl: 'plant.component.html'
})
export class PlantComponent implements OnInit {
    plant : Plant = new Plant();
    private commented: Boolean = false;
    private id: string;
    private plantID: string;

    constructor(private plantListService: PlantListService, private route: ActivatedRoute) {
    }


    private comment(comment: string): void {
        if(!this.commented){
            if(comment != null) {
                this.plantListService.commentPlant(this.plant["_id"]["$oid"], comment)
                    .subscribe(succeeded => this.commented = succeeded);
            }
        }
    }

    ngOnInit(): void {

        //This gets the ID from the URL params and sets and subscribes this.plant
        this.route.params
            .switchMap((params: Params) => this.plantListService.getPlantById(params['plantID']))
            .subscribe((plant: Plant) => this.plant = plant);
    }
}
