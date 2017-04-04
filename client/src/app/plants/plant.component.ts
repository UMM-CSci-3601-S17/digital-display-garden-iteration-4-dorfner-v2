
import { PlantListService } from "./plant-list.service";
import { Plant } from "./plant";
import { Component, OnInit} from '@angular/core';
import { Params,  ActivatedRoute} from '@angular/router';
import {PlantFeedback} from "./plant.feedback";

@Component({
    selector: 'plant-component',
    templateUrl: 'plant.component.html'
})
export class PlantComponent implements OnInit {
    plant : Plant = new Plant();
    plantFeedback: PlantFeedback = new PlantFeedback();
    private commented: Boolean = false;

    // The rating field can have 3 values:
    // null - means that the plant hasn't been rated
    // true - means that the plant was liked
    // false - means the the plant was disliked
    private rating: boolean = null;

    //public plant: Plant = null;
    private id: string;
    private plantID: string;
    private srcBed : string;

    constructor(private plantListService: PlantListService, private route: ActivatedRoute) {
        this.srcBed = this.route.snapshot.params["srcBed"];
    }

    public comment(comment: string): void {
        if(!this.commented){
            if(comment != null) {
                this.plantListService.commentPlant(this.plant["_id"]["$oid"], comment)
                    .subscribe(succeeded => {
                        this.commented = succeeded;
                        this.refreshFeedback();
                    });
            }
        }
    }

    public ratePlant(like: boolean): void {
        if(this.rating === null && like !== null) {
            this.plantListService.ratePlant(this.plant["_id"]["$oid"], like)
                .subscribe(succeeded => {
                    this.rating = like;
                    this.refreshFeedback();
                });
        }
    }

    private refreshFeedback(): void {
        //Update flower feedback numbers
        this.route.params
            .switchMap((params: Params) => this.plantListService.getFeedbackForPlantByPlantID(params['plantID']))
            .subscribe((plantFeedback: PlantFeedback) => this.plantFeedback = plantFeedback);
    }




    ngOnInit(): void {

        //This gets the ID from the URL params and sets and asks the server for the Plant with that ID
        this.route.params
            .switchMap((params: Params) => this.plantListService.getPlantById(params['plantID']))
            .subscribe((plant: Plant) => this.plant = plant);

        //Asks the server for a PlantFeedback object corresponding
        this.route.params
            .switchMap((params: Params) => this.plantListService.getFeedbackForPlantByPlantID(params['plantID']))
            .subscribe((plantFeedback: PlantFeedback) => this.plantFeedback = plantFeedback);

    }
}
