
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
    plant : Plant = {id:"", plantID:"", plantType:"", commonName:"",cultivar:"",source:"",gardenLocation:"",year:1987,pageURL:"",plantImageURLs:[], recognitions:[]};
    private commented: Boolean = false;
    //public plant: Plant = null;
    private id: string;
    private plantID: string;

    constructor(private plantListService: PlantListService, private route: ActivatedRoute) {
        // this.plants = this.plantListService.getPlants();



    }

    /*
    private subscribeToServiceForId() {
        if (this.id) {
            this.plantListService.getPlantById(this.id).subscribe(
                plant => this.plant = plant,
                err => {
                    console.log(err);
                }
            );
        }
    }



    setId(id: string) {
        this.id = id;
        this.subscribeToServiceForId();
    }*/

    private comment(comment: string): void {
        if(!this.commented){
            if(comment != null) {
                this.plantListService.commentPlant(this.plant["_id"]["$oid"], comment)
                    .subscribe(succeeded => this.commented = succeeded);
            }
        }
    }

    ngOnInit(): void {
        //this.subscribeToServiceForId();
        // console.log(this.plant.cultivar);

        //
        this.route.params
            .switchMap((params: Params) => this.plantListService.getPlantById(params['plantID']))
            .subscribe((plant: Plant) => this.plant = plant);
    }
}
