import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from "rxjs";

@Injectable()
export class AdminService {
    private url: string = API_URL;
    constructor(private http:Http) { }

    getUploadIds(): Observable<string[]> {
        return this.http.request(this.url + "uploadIds", {withCredentials: true}).map(res => res.json());
    }

    getLiveUploadId(): Observable<string> {
        return this.http.request(this.url + "liveUploadId").map(res => res.json());
    }

    deleteUploadId(uploadId : string) : Observable<any> {
        return this.http.delete(this.url + "deleteData/" + uploadId).map(res => res.json());
    }
}