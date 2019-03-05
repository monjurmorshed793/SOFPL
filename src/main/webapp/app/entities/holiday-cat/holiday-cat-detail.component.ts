import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHolidayCat } from 'app/shared/model/holiday-cat.model';

@Component({
    selector: 'jhi-holiday-cat-detail',
    templateUrl: './holiday-cat-detail.component.html'
})
export class HolidayCatDetailComponent implements OnInit {
    holidayCat: IHolidayCat;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ holidayCat }) => {
            this.holidayCat = holidayCat;
        });
    }

    previousState() {
        window.history.back();
    }
}
