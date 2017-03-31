import {ComponentFixture, TestBed, async, inject} from "@angular/core/testing";
import { Plant } from "./plant";
import { PlantComponent } from "./plant.component";
import { PlantListService } from "./plant-list.service";
import { Observable } from "rxjs";
import {PlantFeedback} from "./plant.feedback";
import { ActivatedRoute} from "@angular/router";
import {FormsModule} from "@angular/forms";


describe("Plant component", () => {

    let plantComponent: PlantComponent;
    let fixture: ComponentFixture<PlantComponent>;
    let plantListServiceStub: {
        getPlantById: (id: string) => Observable<Plant>
        getFeedbackForPlantByPlantID: (id: string) => Observable<PlantFeedback>
        ratePlant: (id: string, like: boolean) => Observable<boolean>
    };

    let mockRouter = {
        route: {
            snapshot: {
                params: {
                    "srcBed": "bedFoo"
                }
            },
            params: {
                switchMap: (predicate) => {
                    predicate( {plantID: "16001"})
                }
            }
        }
    };

    let originalMockFeedBackData = [
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
    ];

    let mockFeedBackData;



    beforeEach(() => {

        // (re)set the fake database before every test
        mockFeedBackData = originalMockFeedBackData;

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
            getFeedbackForPlantByPlantID: (id: string) =>
                Observable.of(mockFeedBackData.find(plantFeedback => plantFeedback.id === id)),
            ratePlant: (id: string, like: boolean) => {
                mockFeedBackData.find(el => el.id === id).likeCount += 1;
                return Observable.of(true);
            }
        };


        TestBed.configureTestingModule({
            imports: [FormsModule],
            declarations: [ PlantComponent ],
            providers:    [
                {provide: PlantListService, useValue: plantListServiceStub} ,
                {provide: ActivatedRoute, useValue: mockRouter }]
        });


    });


    it("can be initialized", () => {

        async(() => {TestBed.compileComponents().then(() => {
            fixture = TestBed.createComponent(PlantComponent);
            fixture.detectChanges();
            plantComponent = fixture.componentInstance;

            expect(plantComponent).toBeDefined();
        })});
    });

    it("fetches plant feedback", () => {
        async(() => {
            TestBed.compileComponents().then(() => {
                fixture = TestBed.createComponent(PlantComponent);
                fixture.detectChanges();
                plantComponent = fixture.componentInstance;

                expect(plantComponent).toBeDefined();
                expect(plantComponent.plantFeedback.likeCount).toBe(2);
        })});
    });

    it("updates plant feedback", () => {
        async(() => {
            TestBed.compileComponents().then(() => {
                fixture = TestBed.createComponent(PlantComponent);
                fixture.detectChanges();
                plantComponent = fixture.componentInstance;

                expect(plantComponent.plantFeedback.likeCount).toBe(2);
                expect(plantComponent.ratePlant(true)).toBe(true);
                expect(plantComponent.plantFeedback.likeCount).toBe(3);
            });
        });
    });



    // it("returns undefined for Santa", () => {
    //     plantComponent.setId("Santa");
    //     expect(plantComponent.plant).not.toBeDefined();
    // });

});
