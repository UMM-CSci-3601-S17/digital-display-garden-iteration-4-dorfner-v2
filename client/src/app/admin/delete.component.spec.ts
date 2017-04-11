import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import { Observable } from "rxjs";
import {FormsModule} from "@angular/forms";
import {DeleteComponent} from "./delete.component";
import {AdminService} from "./admin.service";
import {RouterTestingModule} from "@angular/router/testing";
import {NavbarComponent} from "../navbar/navbar.component";

describe("Delete Component", () => {

    let deleteComponent: DeleteComponent;
    let fixture: ComponentFixture<DeleteComponent>;
    let adminServiceStub: {
        getUploadIds: () => Observable<string[]>,
        getLiveUploadId: () => Observable<string>,
        deleteUploadId: (string) => Observable<any>,
    };

    beforeEach(() => {
        adminServiceStub = {
            getUploadIds: () => {
                return Observable.of(["upload id 1", "upload id 2"]);
            },
            getLiveUploadId: () => {
                return Observable.of("upload id 2");
            },
            deleteUploadId: (uploadID: string) => {

                return Observable.of({
                    success: ["upload id 1", "upload id 2"].filter(str => str !== uploadID).length === 1,
                    uploadIds: ["upload id 1", "upload id 2"].filter(str => str !== uploadID)
                });
            }
        };

        TestBed.configureTestingModule({
            imports: [FormsModule, RouterTestingModule ],
            declarations: [ DeleteComponent, NavbarComponent],
            providers:    [{provide: AdminService, useValue: adminServiceStub}]
        });

    });

    beforeEach(
        async(() => {
            TestBed.compileComponents().then(() => {
                fixture = TestBed.createComponent(DeleteComponent);
                deleteComponent = fixture.componentInstance;
                fixture.detectChanges();
            });
    }));

    it("can be initialized", () => {
        expect(deleteComponent).toBeDefined();
    });

    it("can delete an uploadID", () => {
       deleteComponent.delete("upload id 1");
       expect(deleteComponent.uploadIds).toEqual(["upload id 2"]);
    });

    it("changes nothing on failed deletions", () => {
       deleteComponent.delete("blablabla");
       expect(deleteComponent.uploadIds).toEqual(["upload id 1", "upload id 2"]);
       console.log("BORK\n\nBORKBORK\n\n\n\nBORKBORKBORKBORKBORK\n\n\n\n");
    });

});