import {
  HttpClient,
  HttpErrorResponse,
  HttpParams,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, of, shareReplay, take } from 'rxjs';
import { Environment } from '../../../../environments/environment.local';
import { MediaView } from '../../../shared/models/views/media-views';

@Injectable({
  providedIn: 'root',
})
export class MediaService {
  private readonly BACKEND_API_URL: string =
    Environment.BACKEND_ENDPOINT + 'media';

  constructor(private _httpClient: HttpClient) {}

  public getMediaUsingTrending(
    type: string,
    time_window: string,
  ): Observable<MediaView[]> {
    const httpParams = new HttpParams()
      .append('type', type)
      .append('time_window', time_window);

    return this._httpClient
      .get<MediaView[]>(`${this.BACKEND_API_URL}/get/trending`, {
        params: httpParams,
      })
      .pipe(
        shareReplay(1),
        take(1),
        catchError((error: Error) => {
          if (error instanceof HttpErrorResponse) {
            console.error(`${error.status} ${error.message}`);
          }
          return of([]);
        }),
      );
  }

  public getFreeToWatchByMediaType(
    media_type: string,
  ): Observable<MediaView[]> {
    return this._httpClient
      .get<
        MediaView[]
      >(`${this.BACKEND_API_URL}/get/free_to_watch/${media_type.toUpperCase()}`)
      .pipe(
        shareReplay(1),
        take(1),
        catchError((error: Error) => {
          if (error instanceof HttpErrorResponse) {
            console.error(`${error.status} ${error.message}`);
          }
          return of([]);
        }),
      );
  }
}
