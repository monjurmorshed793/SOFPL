import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { HolidayCat } from 'app/shared/model/holiday-cat.model';
import { HolidayCatService } from './holiday-cat.service';
import { HolidayCatComponent } from './holiday-cat.component';
import { HolidayCatDetailComponent } from './holiday-cat-detail.component';
import { HolidayCatUpdateComponent } from './holiday-cat-update.component';
import { HolidayCatDeletePopupComponent } from './holiday-cat-delete-dialog.component';
import { IHolidayCat } from 'app/shared/model/holiday-cat.model';

@Injectable({ providedIn: 'root' })
export class HolidayCatResolve implements Resolve<IHolidayCat> {
    constructor(private service: HolidayCatService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IHolidayCat> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<HolidayCat>) => response.ok),
                map((holidayCat: HttpResponse<HolidayCat>) => holidayCat.body)
            );
        }
        return of(new HolidayCat());
    }
}

export const holidayCatRoute: Routes = [
    {
        path: '',
        component: HolidayCatComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'HolidayCats'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: HolidayCatDetailComponent,
        resolve: {
            holidayCat: HolidayCatResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'HolidayCats'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: HolidayCatUpdateComponent,
        resolve: {
            holidayCat: HolidayCatResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'HolidayCats'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: HolidayCatUpdateComponent,
        resolve: {
            holidayCat: HolidayCatResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'HolidayCats'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const holidayCatPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: HolidayCatDeletePopupComponent,
        resolve: {
            holidayCat: HolidayCatResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'HolidayCats'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
