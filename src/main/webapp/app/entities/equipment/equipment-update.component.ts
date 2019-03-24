import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IEquipment } from 'app/shared/model/equipment.model';
import { EquipmentService } from './equipment.service';

@Component({
    selector: 'jhi-equipment-update',
    templateUrl: './equipment-update.component.html'
})
export class EquipmentUpdateComponent implements OnInit {
    equipment: IEquipment;
    isSaving: boolean;

    constructor(protected equipmentService: EquipmentService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ equipment }) => {
            this.equipment = equipment;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.equipment.id !== undefined) {
            this.subscribeToSaveResponse(this.equipmentService.update(this.equipment));
        } else {
            this.subscribeToSaveResponse(this.equipmentService.create(this.equipment));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IEquipment>>) {
        result.subscribe((res: HttpResponse<IEquipment>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
