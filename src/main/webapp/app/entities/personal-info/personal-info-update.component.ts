import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IPersonalInfo } from 'app/shared/model/personal-info.model';
import { PersonalInfoService } from './personal-info.service';
import { IDepartment } from 'app/shared/model/department.model';
import { DepartmentService } from 'app/entities/department';
import { IDesignation } from 'app/shared/model/designation.model';
import { DesignationService } from 'app/entities/designation';

@Component({
    selector: 'jhi-personal-info-update',
    templateUrl: './personal-info-update.component.html'
})
export class PersonalInfoUpdateComponent implements OnInit {
    personalInfo: IPersonalInfo;
    isSaving: boolean;

    departments: IDepartment[];

    designations: IDesignation[];
    birthDateDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected personalInfoService: PersonalInfoService,
        protected departmentService: DepartmentService,
        protected designationService: DesignationService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ personalInfo }) => {
            this.personalInfo = personalInfo;
        });
        this.departmentService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IDepartment[]>) => mayBeOk.ok),
                map((response: HttpResponse<IDepartment[]>) => response.body)
            )
            .subscribe((res: IDepartment[]) => (this.departments = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.designationService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IDesignation[]>) => mayBeOk.ok),
                map((response: HttpResponse<IDesignation[]>) => response.body)
            )
            .subscribe((res: IDesignation[]) => (this.designations = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.personalInfo.id !== undefined) {
            this.subscribeToSaveResponse(this.personalInfoService.update(this.personalInfo));
        } else {
            this.subscribeToSaveResponse(this.personalInfoService.create(this.personalInfo));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonalInfo>>) {
        result.subscribe((res: HttpResponse<IPersonalInfo>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackDepartmentById(index: number, item: IDepartment) {
        return item.id;
    }

    trackDesignationById(index: number, item: IDesignation) {
        return item.id;
    }
}
