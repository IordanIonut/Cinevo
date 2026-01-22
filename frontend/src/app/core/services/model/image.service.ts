import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, shareReplay, take } from 'rxjs';
import { Environment } from '../../../../environments/environment.local';
import { MediaType } from '../../../shared/models/enums/media-type';
import { ImageView } from '../../../shared/models/views/image-views';

@Injectable({
  providedIn: 'root',
})
export class ImageService {
  private readonly BACKEND_API_URL: string =
    Environment.BACKEND_ENDPOINT + 'image';

  constructor(private _httpClient: HttpClient) {}

  public findImageViewByMediaTypeAndCinevoId(
    media_type: MediaType,
    cinevo_id: string,
  ): Observable<ImageView[]> {
    return this._httpClient
      .get<
        ImageView[]
      >(`${this.BACKEND_API_URL}/get/by/image-view/${media_type.toLocaleUpperCase()}/${cinevo_id}`)
      .pipe(
        shareReplay(1),
        take(1),
        catchError((error: Error) => {
          if (error instanceof HttpErrorResponse)
            console.error(`${error.status} ${error.message}`);
          return [];
        }),
      );
  }
}
