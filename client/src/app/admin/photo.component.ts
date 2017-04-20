import { Component, OnInit } from '@angular/core';
import { AdminService } from './admin.service';
import { PlantListService } from '../plants/plant-list.service';
import {Plant} from "../plants/plant";


@Component({
    selector: 'photo-component',
    templateUrl: 'photo.component.html',
})
export class PhotoComponent implements OnInit {
    public id: string;
    public plant: Plant;
    public clicked: boolean;


    constructor(private adminService: AdminService, private plantListService: PlantListService) {

    }

    public getPlant(id: string): void {
        this.clicked = true;
        this.plantListService.getPlantById(id).subscribe(
            plant => this.plant = plant,
            err => {
                console.log(err);
            })
    }

    ngOnInit(): void {

    }
}