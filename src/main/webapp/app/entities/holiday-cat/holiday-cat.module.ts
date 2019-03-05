import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SofplSharedModule } from 'app/shared';
import {
    HolidayCatComponent,
    HolidayCatDetailComponent,
    HolidayCatUpdateComponent,
    HolidayCatDeletePopupComponent,
    HolidayCatDeleteDialogComponent,
    holidayCatRoute,
    holidayCatPopupRoute
} from './';

const ENTITY_STATES = [...holidayCatRoute, ...holidayCatPopupRoute];

@NgModule({
    imports: [SofplSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        HolidayCatComponent,
        HolidayCatDetailComponent,
        HolidayCatUpdateComponent,
        HolidayCatDeleteDialogComponent,
        HolidayCatDeletePopupComponent
    ],
    entryComponents: [HolidayCatComponent, HolidayCatUpdateComponent, HolidayCatDeleteDialogComponent, HolidayCatDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SofplHolidayCatModule {}
