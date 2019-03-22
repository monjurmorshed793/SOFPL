import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/index';
import { Injectable } from '@angular/core';

type Response = HttpResponse<any>;

@Injectable({ providedIn: 'root' })
export class JhiImageUploadService {
    constructor(private http: HttpClient) {}

    public uploadImage(image: File): Observable<Response> {
        const formData = new FormData();
        formData.append('file', image);
        return this.http.post('api/image/upload', formData, { observe: 'response' });
    }
}
