/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SofplTestModule } from '../../../test.module';
import { HolidayCatUpdateComponent } from 'app/entities/holiday-cat/holiday-cat-update.component';
import { HolidayCatService } from 'app/entities/holiday-cat/holiday-cat.service';
import { HolidayCat } from 'app/shared/model/holiday-cat.model';

describe('Component Tests', () => {
    describe('HolidayCat Management Update Component', () => {
        let comp: HolidayCatUpdateComponent;
        let fixture: ComponentFixture<HolidayCatUpdateComponent>;
        let service: HolidayCatService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SofplTestModule],
                declarations: [HolidayCatUpdateComponent]
            })
                .overrideTemplate(HolidayCatUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(HolidayCatUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HolidayCatService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new HolidayCat(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.holidayCat = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new HolidayCat();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.holidayCat = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
