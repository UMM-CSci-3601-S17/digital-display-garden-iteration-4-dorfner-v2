import {Component, OnInit, ViewChild} from '@angular/core';
import { AdminService } from './admin.service';
import { PlantListService } from '../plants/plant-list.service';
import {Plant} from "../plants/plant";
import {Params, ActivatedRoute, Router} from "@angular/router";
import { Location } from '@angular/common';


@Component({
    selector: 'photo-component',
    templateUrl: 'photo.component.html',
})
export class PhotoComponent implements OnInit {
    public id: string;
    public plant: Plant;
    public clicked: boolean;
    public fileLocation: string;
    public textValue: string;

    private url: string = API_URL;

    constructor(private adminService: AdminService, private plantListService: PlantListService, private router: Router, private route: ActivatedRoute, private location: Location) {

    }

    @ViewChild('fu') fu;

    filename:string;
    uploadAttempted:boolean = false;

    handleUpload(){
        this.fu.uploadPhoto(this.id).subscribe(
            response => {
                this.filename = response.json();
                this.uploadAttempted = true;
                location.reload();
            },
            err => {
                this.uploadAttempted = true;
            }
        );

    }

    public getPlant(id: string): void {
        this.uploadAttempted = false;
        this.filename = undefined;
        this.clicked = true;
        this.id = id;
        this.fileLocation = ".plants/" + id + ".png";
        this.plantListService.getPlantByIdA(id).subscribe(
            plant => this.plant = plant,
            err => {
                console.log(err);
            })
    }

    private onKey(event: any) {
        if (event.key === "Enter") {
            this.getPlant(this.textValue);
        }
    }

    ngOnInit(): void {
        this.route.queryParams.subscribe((params: Params) => {this.textValue = params['query'];
        if(this.textValue !== ""){
            this.getPlant(this.textValue);
        }
        });

    }

    refresh(id: string) {
        this.router.navigate(['/admin/PhotoComponent'], { queryParams: { query: this.id} });
    }
}