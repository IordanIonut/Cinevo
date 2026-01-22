import { Injectable } from '@angular/core';
import { Environment } from '../../../environments/environment.local';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, shareReplay, throwError } from 'rxjs';
import { Result } from '../../shared/models/vidfast/result';
import { Request } from '../../shared/models/vidfast/request';

@Injectable({
  providedIn: 'root',
})
export class VidFastService {
  private readonly BACKEND_API_URL: string =
    Environment.BACKEND_ENDPOINT + 'vidfast';

  constructor(private _httpClient: HttpClient) {}

  public checkAllUrl(url: string): Observable<Result> {
    const req: Request = { url: url };
    return this._httpClient
      .post<Result>(`${this.BACKEND_API_URL}/check-all`, req)
      .pipe(
        shareReplay(1),
        catchError((err: HttpErrorResponse) => {
          console.error(
            'VidFastService.checkAllUrl error:',
            err.status,
            err.message
          );
          return throwError(() => err);
        })
      );
  }
}
