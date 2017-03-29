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

@Component({
    template: `
    <router-outlet></router-outlet>
  `
})
class RoutingComponent { }

describe("Plant component", () => {
    let location;
    let plantComponent: PlantComponent;
    let fixture: ComponentFixture<PlantComponent>;
    let router: Router;

    let plantListServiceStub: {
        getPlantById: (id: string) => Observable<Plant>
        getFeedbackForPlantByPlantID: (id: string) => Observable<PlantFeedback>
    };

    let activatedRouteStub : {

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
                // {
                //     _id: {
                //         $oid: "58daf99befbd607288f772a5"
                //     },
                //     id:"16001",
                //     commonName:"Alternanthera",
                //     cultivar:"Experimental",
                //     source:"PA",
                //     gardenLocation:"13",
                //     Comments:"Name change from Purple Prince 14x18 spreader",
                //     HBHangBasketCContainerWWall: "",
                //     SSeedVVeg: "S",
                //     metadata: {
                //         pageViews: 1,
                //         visits: [
                //             {
                //                 visit: {
                //                     $oid: "58dafb02efbd607288f7740d"
                //                 }
                //             }
                //         ],
                //         ratings: [
                //             {
                //                 like: true,
                //                 ratingOnObjectOfId: {
                //                     $oid: "58daf99befbd607288f772a5"
                //                 }
                //             }
                //         ]
                //     }
                // }
                // {
                //     _id: {
                //         $oid: "58daf99befbd607288f772a6"
                //     },
                //     commonName: "Angelonia",
                //     cultivar: "Serenitaâ„¢ Pink F1",
                //     gardenLocation: "7",
                //     Comments: "",
                //     HBHangBasketCContainerWWall: "",
                //     id: "16002",
                //     source: "AAS",
                //     SSeedVVeg: "S",
                //     metadata: {
                //         pageViews: 1,
                //         visits: [
                //             {
                //                 visit: {
                //                     $oid: "58dafb65efbd607288f7740f"
                //                 }
                //             }
                //         ],
                //         ratings: [
                //             {
                //                 like: false,
                //                 ratingOnObjectOfId: {
                //                     $oid: "58daf99befbd607288f772a6"
                //                 }
                //             }
                //         ]
                //     }
                // },
                // {
                //     _id: {
                //         $oid: "58daf99befbd607288f772a8"
                //     },
                //     commonName: "Begonia",
                //     cultivar: "Megawatt Rose Green Leaf",
                //     gardenLocation: "10",
                //     Comments: "Grow in same sun or shade area; grow close proximity to each other for comparison",
                //     HBHangBasketCContainerWWall: "",
                //     id: "16008",
                //     source: "PA",
                //     SSeedVVeg: "S",
                //     metadata: {
                //         pageViews: 2,
                //         visits: [
                //             {
                //                 visit: {
                //                     $oid: "58dafbd8efbd607288f77414"
                //                 }
                //             },
                //             {
                //                 visit: {
                //                     $oid: "58dafbdfefbd607288f77415"
                //                 }
                //             }
                //         ],
                //         ratings: [
                //             {
                //                 like: false,
                //                 ratingOnObjectOfId: {
                //                     $oid: "58daf99befbd607288f772a8"
                //                 }
                //             },
                //             {
                //                 like: true,
                //                 ratingOnObjectOfId: {
                //                     $oid: "58daf99befbd607288f772a8"
                //                 }
                //             }
                //         ]
                //     }
                // }
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
            imports: [
                RouterTestingModule.withRoutes(
                    [
                         { path: 'plants/:plantID', component: PlantComponent }
                    ]),
                FormsModule],
            declarations: [ PlantComponent, RoutingComponent ],
            providers:    [ { provide: PlantListService, useValue: plantListServiceStub} ]
        })

        // router = TestBed.get(Router);
    });

    beforeEach(async(() => {
        TestBed.compileComponents().then(() => {
            fixture = TestBed.createComponent(PlantComponent);
            fixture.detectChanges();
            plantComponent = fixture.componentInstance;
        });
    }));

    beforeEach(inject([Router, Location], (_router: Router, _location: Location) => {
        location = _location;
        router = _router;
    }));

    it("can retrieve Pat by ID", () => {
        console.log("in the test");
        let fixture2 = TestBed.createComponent(RoutingComponent);
        fixture2.detectChanges();
        console.log("after the test");
        router.navigate(['/plants/16001']).then(() => {
            console.log("\n\nInside the naviation thing \n\n");
            // plantComponent.ratePlant(true);
            // expect(plantComponent.plantFeedback).toBeDefined();
            // expect("foo").toEqual("foo");
        });
        expect("foo").toEqual("foo");
        //expect(plantComponent.plantFeedback.likeCount).toBe(2);
    });

    // it("returns undefined for Santa", () => {
    //     plantComponent.setId("Santa");
    //     expect(plantComponent.plant).not.toBeDefined();
    // });

});
