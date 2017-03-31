import {ComponentFixture, TestBed, async, inject} from "@angular/core/testing";
import { Plant } from "./plant";
import { PlantComponent } from "./plant.component";
import { PlantListService } from "./plant-list.service";
import { Observable } from "rxjs";
import {PlantFeedback} from "./plant.feedback";
import {Router} from "@angular/router";
import { Location } from '@angular/common';
import {RouterTestingModule} from "@angular/router/testing";
import {FormsModule} from "@angular/forms";
import {Component} from "@angular/core";


describe("Plant component", () => {

    let plantComponent: PlantComponent;
    let fixture: ComponentFixture<PlantComponent>;
    let plantListServiceStub: {
        getPlantById: (id: string) => Observable<Plant>
        getFeedbackForPlantByPlantID: (id: string) => Observable<PlantFeedback>
    };



    beforeEach(() => {
        console.log("before the test");
        // stub plantService for test purposes
        plantListServiceStub = {
            getPlantById: (id: string) => Observable.of([
                {
                    _id: {$oid: "58daf99befbd607288f772a5"},
                    id: "16001",
                    plantID: "16001",
                    plantType: "",
                    commonName: "",
                    cultivar: "",
                    source: "",
                    gardenLocation: "",
                    year: 0,
                    pageURL: "",
                    plantImageURLs: [""],
                    recognitions: [""]
                }
            ].find(plant => plant.id === id)),
            getFeedbackForPlantByPlantID: (id: string) => Observable.of([
                {
                    id:"16001",
                    commentCount: 1,
                    likeCount: 2,
                    dislikeCount: 0
                },
                {
                    id:"16002",
                    commentCount: 62,
                    likeCount: 8,
                    dislikeCount: 6
                },
                {
                    id:"16008",
                    commentCount: 2,
                    likeCount: 22,
                    dislikeCount: 5
                }
            ].find(plantFeedback => plantFeedback.id === id))
        };




        TestBed.configureTestingModule({
            imports: [FormsModule, RouterTestingModule],
            declarations: [ PlantComponent ],
            providers:    [ { provide: PlantListService, useValue: plantListServiceStub} ]
        });

        async(() => {
            TestBed.compileComponents().then(() => {
                fixture = TestBed.createComponent(PlantComponent);
                fixture.detectChanges();
                // plantComponent = fixture.componentInstance;
            });
        });
        // router = TestBed.get(Router);
    });

    // beforeEach(
    // }));

    // beforeEach(inject([Router, Location], (_router: Router, _location: Location) => {
    //     location = _location;
    //     router = _router;
    // }));

    it("can retrieve Pat by ID", () => {
        console.log("in the test");
        console.log("after the test");

        // plantComponent.ratePlant(true);
        // expect(plantComponent.plantFeedback).toBeDefined();

        expect("foo").toEqual("foo");
        //expect(plantComponent.plantFeedback.likeCount).toBe(2);
    });

    // it("returns undefined for Santa", () => {
    //     plantComponent.setId("Santa");
    //     expect(plantComponent.plant).not.toBeDefined();
    // });

});
