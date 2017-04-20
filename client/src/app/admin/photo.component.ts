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
    plant: Plant = new Plant();


    constructor(private adminService: AdminService, private plantListService: PlantListService) {

    }

    public getPlant(id: string): void {
        this.plantListService.getPlantById(id).subscribe(
            plant => this.plant = plant,
            err => {
                console.log(err);
            })
    }

    ngOnInit(): void {

    }
}