import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, shareReplay, take, throwError } from 'rxjs';
import { Environment } from '../../../../environments/environment.local';
import { MediaType } from '../../../shared/models/enums/media-type.enum';
import { GenreView } from '../../../shared/models/views/genre-view';

@Injectable({
  providedIn: 'root',
})
export class GenreService {
  private readonly BACKEND_API_URL: string =
    Environment.BACKEND_ENDPOINT + 'genre';

  constructor(private _httpClient: HttpClient) {}

  public getGenreByMediaType(media_type: MediaType): Observable<GenreView[]> {
    return this._httpClient
      .get<
        GenreView[]
      >(`${this.BACKEND_API_URL}/get/genre-view/by?media_type=${media_type.toUpperCase()}`)
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
