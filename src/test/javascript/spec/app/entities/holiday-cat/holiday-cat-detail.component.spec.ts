/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SofplTestModule } from '../../../test.module';
import { HolidayCatDetailComponent } from 'app/entities/holiday-cat/holiday-cat-detail.component';
import { HolidayCat } from 'app/shared/model/holiday-cat.model';

describe('Component Tests', () => {
    describe('HolidayCat Management Detail Component', () => {
        let comp: HolidayCatDetailComponent;
        let fixture: ComponentFixture<HolidayCatDetailComponent>;
        const route = ({ data: of({ holidayCat: new HolidayCat(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SofplTestModule],
                declarations: [HolidayCatDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(HolidayCatDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(HolidayCatDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.holidayCat).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
