import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SofplSharedModule } from 'app/shared';
import {
    PersonalInfoComponent,
    PersonalInfoDetailComponent,
    PersonalInfoUpdateComponent,
    PersonalInfoDeletePopupComponent,
    PersonalInfoDeleteDialogComponent,
    personalInfoRoute,
    personalInfoPopupRoute
} from './';

const ENTITY_STATES = [...personalInfoRoute, ...personalInfoPopupRoute];

@NgModule({
    imports: [SofplSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PersonalInfoComponent,
        PersonalInfoDetailComponent,
        PersonalInfoUpdateComponent,
        PersonalInfoDeleteDialogComponent,
        PersonalInfoDeletePopupComponent
    ],
    entryComponents: [
        PersonalInfoComponent,
        PersonalInfoUpdateComponent,
        PersonalInfoDeleteDialogComponent,
        PersonalInfoDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SofplPersonalInfoModule {}
