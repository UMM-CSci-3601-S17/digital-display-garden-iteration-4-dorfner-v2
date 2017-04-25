import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';
import { AdminService } from "./admin.service";

@Injectable()
export class AuthGuard implements CanActivate {

   url: string = API_URL;

    constructor(private adminService: AdminService) {}

    canActivate() {
        this.adminService.authorized().subscribe(authorized => {
            if (!authorized) {
                // redirect to the backend for authorization
                window.location.href = this.url + "authorize?originatingURL="+window.location.href;
            }
        });
        return true;
    }
}