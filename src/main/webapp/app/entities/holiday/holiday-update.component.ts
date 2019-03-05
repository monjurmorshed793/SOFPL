import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IHoliday } from 'app/shared/model/holiday.model';
import { HolidayService } from './holiday.service';
import { IHolidayCat } from 'app/shared/model/holiday-cat.model';
import { HolidayCatService } from 'app/entities/holiday-cat';

@Component({
    selector: 'jhi-holiday-update',
    templateUrl: './holiday-update.component.html'
})
export class HolidayUpdateComponent implements OnInit {
    holiday: IHoliday;
    isSaving: boolean;

    holidaycats: IHolidayCat[];
    startDateDp: any;
    endDateDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected holidayService: HolidayService,
        protected holidayCatService: HolidayCatService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ holiday }) => {
            this.holiday = holiday;
        });
        this.holidayCatService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IHolidayCat[]>) => mayBeOk.ok),
                map((response: HttpResponse<IHolidayCat[]>) => response.body)
            )
            .subscribe((res: IHolidayCat[]) => (this.holidaycats = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.holiday.id !== undefined) {
            this.subscribeToSaveResponse(this.holidayService.update(this.holiday));
        } else {
            this.subscribeToSaveResponse(this.holidayService.create(this.holiday));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IHoliday>>) {
        result.subscribe((res: HttpResponse<IHoliday>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackHolidayCatById(index: number, item: IHolidayCat) {
        return item.id;
    }
}
