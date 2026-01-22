import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, of, shareReplay } from 'rxjs';
import { Environment } from '../../../environments/environment.local';

@Injectable({
  providedIn: 'root',
})
export class ImageProxyService {
  private readonly BACKEND_API_URL: string = Environment.BACKEND_ENDPOINT;

  constructor(private _httpClient: HttpClient) {}

  public proxy(url: string): Observable<ArrayBuffer> {
    if (!url) {
      return of(new ArrayBuffer(0));
    }

    return this._httpClient
      .get(
        `${this.BACKEND_API_URL}proxy-image?url=${encodeURIComponent(url)}`,
        {
          responseType: 'arraybuffer',
        }
      )
      .pipe(
        shareReplay(1),
        catchError(() => of(new ArrayBuffer(0)))
      );
  }
}
