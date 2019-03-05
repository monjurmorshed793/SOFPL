/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { PersonalInfoService } from 'app/entities/personal-info/personal-info.service';
import { IPersonalInfo, PersonalInfo, MaritalStatus, Gender, Religion } from 'app/shared/model/personal-info.model';

describe('Service Tests', () => {
    describe('PersonalInfo Service', () => {
        let injector: TestBed;
        let service: PersonalInfoService;
        let httpMock: HttpTestingController;
        let elemDefault: IPersonalInfo;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(PersonalInfoService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new PersonalInfo(
                0,
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                currentDate,
                MaritalStatus.MARRIED,
                Gender.MALE,
                Religion.ISLAM,
                'AAAAAAA',
                'AAAAAAA'
            );
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        birthDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a PersonalInfo', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        birthDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        birthDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new PersonalInfo(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a PersonalInfo', async () => {
                const returnedFromService = Object.assign(
                    {
                        employeeId: 'BBBBBB',
                        fullName: 'BBBBBB',
                        fathersName: 'BBBBBB',
                        mothersName: 'BBBBBB',
                        birthDate: currentDate.format(DATE_FORMAT),
                        maritalStatus: 'BBBBBB',
                        gender: 'BBBBBB',
                        religion: 'BBBBBB',
                        permanentAddress: 'BBBBBB',
                        presentAddress: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        birthDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of PersonalInfo', async () => {
                const returnedFromService = Object.assign(
                    {
                        employeeId: 'BBBBBB',
                        fullName: 'BBBBBB',
                        fathersName: 'BBBBBB',
                        mothersName: 'BBBBBB',
                        birthDate: currentDate.format(DATE_FORMAT),
                        maritalStatus: 'BBBBBB',
                        gender: 'BBBBBB',
                        religion: 'BBBBBB',
                        permanentAddress: 'BBBBBB',
                        presentAddress: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        birthDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a PersonalInfo', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
