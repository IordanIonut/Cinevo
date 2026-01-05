import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UiToggleService {
  private _navHidden$ = new BehaviorSubject<boolean>(false);
  public navHidden$ = this._navHidden$.asObservable();

  constructor() {}

  hideNav() {
    this._navHidden$.next(true);
  }
  
  showNav() {
    this._navHidden$.next(false);
  }

  toggleNav() {
    this._navHidden$.next(!this._navHidden$.value);
  }
}
