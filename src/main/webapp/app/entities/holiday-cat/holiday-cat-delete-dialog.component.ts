import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHolidayCat } from 'app/shared/model/holiday-cat.model';
import { HolidayCatService } from './holiday-cat.service';

@Component({
    selector: 'jhi-holiday-cat-delete-dialog',
    templateUrl: './holiday-cat-delete-dialog.component.html'
})
export class HolidayCatDeleteDialogComponent {
    holidayCat: IHolidayCat;

    constructor(
        protected holidayCatService: HolidayCatService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.holidayCatService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'holidayCatListModification',
                content: 'Deleted an holidayCat'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-holiday-cat-delete-popup',
    template: ''
})
export class HolidayCatDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ holidayCat }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(HolidayCatDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.holidayCat = holidayCat;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/holiday-cat', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/holiday-cat', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
