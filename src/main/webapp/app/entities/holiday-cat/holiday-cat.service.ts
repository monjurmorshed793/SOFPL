import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IHolidayCat } from 'app/shared/model/holiday-cat.model';

type EntityResponseType = HttpResponse<IHolidayCat>;
type EntityArrayResponseType = HttpResponse<IHolidayCat[]>;

@Injectable({ providedIn: 'root' })
export class HolidayCatService {
    public resourceUrl = SERVER_API_URL + 'api/holiday-cats';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/holiday-cats';

    constructor(protected http: HttpClient) {}

    create(holidayCat: IHolidayCat): Observable<EntityResponseType> {
        return this.http.post<IHolidayCat>(this.resourceUrl, holidayCat, { observe: 'response' });
    }

    update(holidayCat: IHolidayCat): Observable<EntityResponseType> {
        return this.http.put<IHolidayCat>(this.resourceUrl, holidayCat, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IHolidayCat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IHolidayCat[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IHolidayCat[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
