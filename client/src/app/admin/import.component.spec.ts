import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import { Observable } from "rxjs";
import {FormsModule} from "@angular/forms";
import {AdminService} from "./admin.service";
import {RouterTestingModule} from "@angular/router/testing";
import {NavbarComponent} from "../navbar/navbar.component";
import {ImportComponent} from "./import.component";
import {FileUploadComponent} from "./file-upload.component";
import { Http } from '@angular/http';

describe("Import Component", () => {

    let importComponent: ImportComponent;
    let fixture: ComponentFixture<ImportComponent>;
    let mockHttp: {post: (string, any) => Observable<any>};

    beforeEach(() => {
        mockHttp = {
            post: (str: string, a: any) => {
                return Observable.of({json:() => "mockFileName"});
            }
        };

        TestBed.configureTestingModule({
            imports: [FormsModule, RouterTestingModule],
            declarations: [ImportComponent, NavbarComponent, FileUploadComponent],
            providers: [{provide: Http, useValue: mockHttp}]
        });

    });

    beforeEach(
        async(() => {
            TestBed.compileComponents().then(() => {
                fixture = TestBed.createComponent(ImportComponent);
                importComponent = fixture.componentInstance;
                fixture.detectChanges();
            });
    }));

    it("can be initialized", () => {
        expect(importComponent).toBeDefined();
    });

    it("can import a file", () => {
        importComponent.fu.inputEl = {nativeElement: {files: {length: 1, item: (x) => "eh"}}};
        importComponent.handleUpload();
        expect(importComponent.filename).toEqual("mockFileName");
        expect(importComponent.uploadAttempted).toEqual(true);
    });
});
