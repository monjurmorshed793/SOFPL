import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IHolidayCat } from 'app/shared/model/holiday-cat.model';
import { HolidayCatService } from './holiday-cat.service';

@Component({
    selector: 'jhi-holiday-cat-update',
    templateUrl: './holiday-cat-update.component.html'
})
export class HolidayCatUpdateComponent implements OnInit {
    holidayCat: IHolidayCat;
    isSaving: boolean;

    constructor(protected holidayCatService: HolidayCatService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ holidayCat }) => {
            this.holidayCat = holidayCat;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.holidayCat.id !== undefined) {
            this.subscribeToSaveResponse(this.holidayCatService.update(this.holidayCat));
        } else {
            this.subscribeToSaveResponse(this.holidayCatService.create(this.holidayCat));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IHolidayCat>>) {
        result.subscribe((res: HttpResponse<IHolidayCat>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
