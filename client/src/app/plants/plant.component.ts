
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

    // The rating field can have 3 values:
    // null - means that the plant hasn't been rated
    // true - means that the plant was liked
    // false - means the the plant was disliked
    private rating: boolean = null;

    //public plant: Plant = null;
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

    private ratePlant(like: boolean): void {
        if(this.rating === null && like !== null) {
            this.plantListService.ratePlant(this.plant["_id"]["$oid"], like)
                .subscribe(succeeded => this.rating = like);
        }
    }

    ngOnInit(): void {

        //This gets the ID from the URL params and sets and subscribes this.plant
        this.route.params
            .switchMap((params: Params) => this.plantListService.getPlantById(params['plantID']))
            .subscribe((plant: Plant) => this.plant = plant);
    }
}
