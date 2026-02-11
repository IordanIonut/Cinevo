import { Injectable } from '@angular/core';
import { Environment } from '../../../../environments/environment.local';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { MediaType } from '../../../shared/models/enums/media-type.enum';
import { catchError, Observable, shareReplay, take, throwError } from 'rxjs';
import { SpokenLanguageView } from '../../../shared/models/views/spoken-language-view';

@Injectable({
  providedIn: 'root',
})
export class SpokenLanguageService {
  private readonly BACKEND_API_URL: string =
    Environment.BACKEND_ENDPOINT + 'spoken-language';

  constructor(private _httpClient: HttpClient) {}

  public getSpokenLanguageViewByMediaType(
    media_type: MediaType,
  ): Observable<SpokenLanguageView[]> {
    return this._httpClient
      .get<
        SpokenLanguageView[]
      >(`${this.BACKEND_API_URL}/get/spoken-language-view/${media_type.toUpperCase()}`)
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
