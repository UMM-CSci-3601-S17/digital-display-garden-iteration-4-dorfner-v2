import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from "rxjs";

@Injectable()
export class AdminService {
    private url: string = API_URL;
    constructor(private http:Http) { }

    getUploadIDs(): Observable<string[]> {
        return this.http.request(this.url + "uploadIDs", {withCredentials: true}).map(res => res.json());
    }

    getLiveUploadID(): Observable<string> {
        return this.http.request(this.url + "liveUploadID", {withCredentials: true}).map(res => res.json());
    }

    deleteUploadID(uploadId : string) : Observable<any> {
        return this.http.delete(this.url + "deleteData/" + uploadId, {withCredentials: true}).map(res => res.json());
    }

    authorized() : Observable<boolean> {
        return this.http.get(this.url + "check-authorization", {withCredentials: true}).map(res => res.json().authorized);
    }
}