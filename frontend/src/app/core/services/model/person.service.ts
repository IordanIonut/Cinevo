import {
  HttpClient,
  HttpErrorResponse,
  HttpParams,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  catchError,
  Observable,
  of,
  shareReplay,
  take,
  throwError,
} from 'rxjs';
import { Environment } from '../../../../environments/environment.local';
import { PersonView } from '../../../shared/models/views/person-view';

@Injectable({
  providedIn: 'root',
})
export class PersonService {
  private readonly BACKEND_API_URL: string =
    Environment.BACKEND_ENDPOINT + 'person';

  constructor(private _httpClient: HttpClient) {}

  public getPersonUsingTrending(time_window: string): Observable<PersonView[]> {
    const httpParams = new HttpParams().append('time_window', time_window);
    return this._httpClient
      .get<
        PersonView[]
      >(`${this.BACKEND_API_URL}/get/trending`, { params: httpParams })
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
