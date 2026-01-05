import {
  HttpClient,
  HttpErrorResponse,
  HttpParams,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, shareReplay } from 'rxjs';
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

  public findImageViewByMediaTypeAndId(
    id: number,
    media_type: MediaType
  ): Observable<ImageView[]> {
    return this._httpClient
      .get<ImageView[]>(
        `${this.BACKEND_API_URL}/get/image-view/${id}/${media_type}`
      )
      .pipe(
        shareReplay(1),
        catchError((error: Error) => {
          if (error instanceof HttpErrorResponse)
            console.error(`${error.status} ${error.message}`);
          return [];
        })
      );
  }
}
