/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SofplTestModule } from '../../../test.module';
import { HolidayCatDeleteDialogComponent } from 'app/entities/holiday-cat/holiday-cat-delete-dialog.component';
import { HolidayCatService } from 'app/entities/holiday-cat/holiday-cat.service';

describe('Component Tests', () => {
    describe('HolidayCat Management Delete Component', () => {
        let comp: HolidayCatDeleteDialogComponent;
        let fixture: ComponentFixture<HolidayCatDeleteDialogComponent>;
        let service: HolidayCatService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SofplTestModule],
                declarations: [HolidayCatDeleteDialogComponent]
            })
                .overrideTemplate(HolidayCatDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(HolidayCatDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HolidayCatService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
