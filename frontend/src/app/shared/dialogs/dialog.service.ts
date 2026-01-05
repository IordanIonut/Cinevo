import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MediaType } from '../models/enums/media-type';
import { SiteType } from '../models/enums/site-type';
import { WatchTrailerComponent } from './watch-trailer/watch-trailer.component';
import { UiToggleService } from '../../core/services/ui/ui-toggle.service';
import { take } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DialogService {
  constructor(
    private _dialog: MatDialog,
    private _uiToggleService: UiToggleService
  ) {}

  public openWatchTrailer(
    type: MediaType,
    id: number,
    key: string,
    site: SiteType
  ) {
    this._uiToggleService.hideNav();

    const dialogRef = this._dialog.open(WatchTrailerComponent, {
      width: '90vw',
      height: '90vw',
      maxWidth: '90vw',
      maxHeight: '90vh',
      panelClass: 'trailer-dialog',
      backdropClass: 'dark-backdrop',
      disableClose: false,
      autoFocus: false,
      restoreFocus: true,
      enterAnimationDuration: '200ms',
      exitAnimationDuration: '150ms',
      data: { type, id, key, site },
    });

    dialogRef
      .afterClosed()
      .pipe(take(1))
      .subscribe((result) => {
        this._uiToggleService.showNav();
      });

    return dialogRef;
  }
}
