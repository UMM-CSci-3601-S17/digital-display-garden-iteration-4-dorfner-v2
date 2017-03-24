import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Plant } from './plant';
import { Observable } from "rxjs";
import {PlantFeedback} from "./plant.feedback";

@Injectable()
export class PlantListService {
    private plantUrl: string = API_URL + "plants";
    constructor(private http:Http) { }

    getPlants(): Observable<Plant[]> {
        return this.http.request(this.plantUrl).map(res => res.json());
    }

    getPlantById(id: string): Observable<Plant> {
        return this.http.request(this.plantUrl + "/" + id).map(res => res.json());
    }

    getFeedbackForPlantByPlantID(id: string): Observable<PlantFeedback> {
        return this.http.request(this.plantUrl + "/" + id + "/counts").map(res => res.json());
    }


    getGardenLocations(): Observable<Plant[]> {
        return this.http.request(API_URL + "/gardenLocations").map(res => res.json());
    }
    getFlowersByFilter(filterUrl: string): Observable<Plant[]> {
        return this.http.request(this.plantUrl + filterUrl).map(res => res.json());
    }


    commentPlant(id: string, comment: string): Observable<Boolean> {
        let returnObject = {
            plantId: id,
            comment: comment
        };
        return this.http.post(this.plantUrl + "/" + "leaveComment", JSON.stringify(returnObject)).map(res => res.json());
    }
    ratePlant(id: string, like: boolean): Observable<boolean> {
        let returnObject = {
            id: id,
            like: like
        };
        return this.http.post(this.plantUrl + "/" + "rate", JSON.stringify(returnObject)).map(res => res.json());
    }
}
