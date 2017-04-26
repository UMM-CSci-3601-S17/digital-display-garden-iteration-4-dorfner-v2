import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Plant } from './plant';
import { Observable } from "rxjs";
import {PlantFeedback} from "./plant-feedback";
import { ObjectID } from "./object-id";

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

    getPlantByIDforAdmin(id: string): Observable<Plant> {
        return this.http.request(this.plantUrl + "/a/" + id).map(res => res.json());
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
    getFlowersByBed(bed: string): Observable<Plant[]> {
        return this.http.request(this.plantUrl + "?gardenLocation=" + bed).map(res => res.json());
    }


    commentPlant(id: string, comment: string): Observable<Boolean> {
        let returnObject = {
            plantId: id,
            comment: comment
        };
        return this.http.post(this.plantUrl + "/" + "leaveComment", JSON.stringify(returnObject)).map(res => res.json());
    }
    ratePlant(id: string, like: boolean): Observable<ObjectID> {
        let returnObject = {
            id: id,
            like: like
        };
        return this.http.post(this.plantUrl + "/" + "rate", JSON.stringify(returnObject)).map(res => res.json());
    }
        // might not need if we do post request
    // deleteRate(id: string): Observable<any> {
    //     return this.http.delete(this.plantUrl + "/" + "rate/" + id).map(res => res.json());
    // }

    changeRate(id: string, ratingID: string, like: boolean): Observable<any> {
        let returnObject = {
            id: id,
            ratingID: ratingID,
            like: like
        };
        return this.http.post(this.plantUrl + "/" + "changeRate", JSON.stringify(returnObject)).map(res => res.json());
    }

    deleteRate(id: string, ratingID: string): Observable<any> {
        let returnObject = {
            id: id,
            ratingID: ratingID,
        };
        return this.http.post(this.plantUrl + "/" + "deleteRate", JSON.stringify(returnObject)).map(res => res.json());
    }


}
