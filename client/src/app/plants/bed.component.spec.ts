import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Plant } from './plant';
import { PlantListService } from './plant-list.service';
import { Observable } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { BedComponent } from './bed.component';
import { GardenNavbarComponent } from '../navbar/garden-navbar.component';
import { FilterBy } from './filter.pipe';

describe("Bed component", () => {

    let bedComponent: BedComponent;
    let fixture: ComponentFixture<BedComponent>;
    let mockRouter: {
        events: Observable<any>
    };
    let mockRoute: {
        snapshot: {
            params: {
                gardenLocation: string
            }
        }
    };
    let plantListServiceStub: {
        getFlowersByBed: (string) => Observable<Plant[]>,
    };

    beforeEach(() => {

        mockRouter = {
            events: Observable.of(null)
        };
        mockRoute = {
            snapshot: {
                params: {
                    "gardenLocation": "bestBed"
                }
            }
        };
        plantListServiceStub = {
            getFlowersByBed: (bed: string) => Observable.of([
                {
                    _id: {$oid: "58daf99befbd607288f772a1"},
                    id: "16001",
                    plantID: "16001",
                    plantType: "",
                    commonName: "commonName1",
                    cultivar: "cultivar1",
                    source: "",
                    gardenLocation: "1N",
                    year: 0,
                    pageURL: "",
                    plantImageURLs: [""],
                    recognitions: [""]
                },
                {
                    _id: {$oid: "58daf99befbd607288f772a2"},
                    id: "16002",
                    plantID: "16002",
                    plantType: "",
                    commonName: "commonName2",
                    cultivar: "cultivar2",
                    source: "",
                    gardenLocation: "2N",
                    year: 0,
                    pageURL: "",
                    plantImageURLs: [""],
                    recognitions: [""]
                }
            ]),
        };

        TestBed.configureTestingModule({
            imports: [FormsModule, RouterTestingModule],
            declarations: [ BedComponent, GardenNavbarComponent, FilterBy ],
            providers:    [
                {provide: PlantListService, useValue: plantListServiceStub} ,
                {provide: ActivatedRoute, useValue: mockRoute },
                {provide: Router, useValue: mockRouter}]
        });
    });

    beforeEach(
        async(() => {
            TestBed.compileComponents().then(() => {

                fixture = TestBed.createComponent(BedComponent);
                bedComponent = fixture.componentInstance;
                // fixture.detectChanges(); // doesn't work if this is uncommented
            });
    }));

    it("can be initialized", () => {
        expect(bedComponent).toBeDefined();
        expect("foo").toEqual("foo");
    });
});