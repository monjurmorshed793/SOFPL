/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SofplTestModule } from '../../../test.module';
import { DesignationDeleteDialogComponent } from 'app/entities/designation/designation-delete-dialog.component';
import { DesignationService } from 'app/entities/designation/designation.service';

describe('Component Tests', () => {
    describe('Designation Management Delete Component', () => {
        let comp: DesignationDeleteDialogComponent;
        let fixture: ComponentFixture<DesignationDeleteDialogComponent>;
        let service: DesignationService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SofplTestModule],
                declarations: [DesignationDeleteDialogComponent]
            })
                .overrideTemplate(DesignationDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DesignationDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DesignationService);
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
