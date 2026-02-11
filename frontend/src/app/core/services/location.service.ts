import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LocationService {
  constructor(private _httpClient: HttpClient) {}

  getCountryFromIP(): Observable<string> {
    return this._httpClient
      .get<any>('https://ipapi.co/json/')
      .pipe(map((res) => res.country));
  }
}
