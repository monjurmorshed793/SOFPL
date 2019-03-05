import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'department',
                loadChildren: './department/department.module#SofplDepartmentModule'
            },
            {
                path: 'designation',
                loadChildren: './designation/designation.module#SofplDesignationModule'
            },
            {
                path: 'holiday-cat',
                loadChildren: './holiday-cat/holiday-cat.module#SofplHolidayCatModule'
            },
            {
                path: 'holiday',
                loadChildren: './holiday/holiday.module#SofplHolidayModule'
            },
            {
                path: 'personal-info',
                loadChildren: './personal-info/personal-info.module#SofplPersonalInfoModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SofplEntityModule {}
