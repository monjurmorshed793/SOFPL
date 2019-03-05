import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SofplSharedModule } from 'app/shared';
import {
    DesignationComponent,
    DesignationDetailComponent,
    DesignationUpdateComponent,
    DesignationDeletePopupComponent,
    DesignationDeleteDialogComponent,
    designationRoute,
    designationPopupRoute
} from './';

const ENTITY_STATES = [...designationRoute, ...designationPopupRoute];

@NgModule({
    imports: [SofplSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DesignationComponent,
        DesignationDetailComponent,
        DesignationUpdateComponent,
        DesignationDeleteDialogComponent,
        DesignationDeletePopupComponent
    ],
    entryComponents: [DesignationComponent, DesignationUpdateComponent, DesignationDeleteDialogComponent, DesignationDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SofplDesignationModule {}
