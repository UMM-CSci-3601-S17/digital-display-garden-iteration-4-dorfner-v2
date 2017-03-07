import { Component, OnInit } from '@angular/core';
import { PlantListService } from "./plant-list.service";
import { Plant } from "./plant";

@Component({
    selector: 'plant-component',
    templateUrl: 'plant.component.html'
})
export class PlantComponent implements OnInit {
    public plant: Plant = null;
    private id: string;

    constructor(private plantListService: PlantListService) {
        // this.plants = this.plantListService.getPlants();
    }

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
    }

    ngOnInit(): void {
        this.subscribeToServiceForId();
    }
}
