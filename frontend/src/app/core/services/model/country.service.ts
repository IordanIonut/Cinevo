import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, shareReplay, take, throwError } from 'rxjs';
import { Environment } from '../../../../environments/environment.local';
import { MediaType } from '../../../shared/models/enums/media-type.enum';
import { CountryView } from '../../../shared/models/views/country-view';

@Injectable({
  providedIn: 'root',
})
export class CountryService {
  private readonly BACKEND_API_URL: string =
    Environment.BACKEND_ENDPOINT + 'country';

  constructor(private _httpClient: HttpClient) {}

  public findCountryViewByMediaType(
    media_type: MediaType,
  ): Observable<CountryView[]> {
    return this._httpClient
      .get<
        CountryView[]
      >(`${this.BACKEND_API_URL}/get/country-view/by/media-type/${media_type.toUpperCase()}`)
      .pipe(
        shareReplay(1),
        take(1),
        catchError((error: Error) => {
          if (error instanceof HttpErrorResponse) {
            console.error(`${error.status} ${error.message}`);
          }
          return throwError(() => error);
        }),
      );
  }
}
