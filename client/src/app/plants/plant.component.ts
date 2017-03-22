
import { PlantListService } from "./plant-list.service";
import { Plant } from "./plant";
import { Component, OnInit, Input} from '@angular/core';

@Component({
    selector: 'plant-component',
    templateUrl: 'plant.component.html'
})
export class PlantComponent implements OnInit {
    @Input() plant : Plant;
    @Input() showDialog : boolean;
    private commented: Boolean = false;

    // The rating field can have 3 values:
    // null - means that the plant hasn't been rated
    // true - means that the plant was liked
    // false - means the the plant was disliked
    private rating: boolean = null;

    //public plant: Plant = null;
    private id: string;

    constructor(private plantListService: PlantListService) {
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

    private ratePlant(like: boolean): void {
        if(this.rating === null && like !== null) {
            this.plantListService.ratePlant(this.plant["_id"]["$oid"], like)
                .subscribe(succeeded => this.rating = like);
        }
    }

    ngOnInit(): void {
        //this.subscribeToServiceForId();
        // console.log(this.plant.cultivar);
    }
}
