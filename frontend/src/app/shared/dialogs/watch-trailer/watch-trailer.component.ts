import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { VidFastService } from '../../../core/services/vidfast.service';
import { MediaType } from '../../models/enums/media-type.enum';
import { SiteType } from '../../models/enums/site-type.enum';

@Component({
  selector: 'app-watch-trailer',
  standalone: true,
  templateUrl: './watch-trailer.component.html',
  styleUrls: ['./watch-trailer.component.css'],
  imports: [CommonModule, MatDialogModule],
  providers: [VidFastService],
})
export class WatchTrailerComponent implements OnInit {
  public readonly safeSrc: SafeResourceUrl = '' as unknown as SafeResourceUrl;
  public readonly siteType = SiteType;

  constructor(
    private _vidFastService: VidFastService,
    @Inject(MAT_DIALOG_DATA)
    protected _data$: {
      type: MediaType;
      id: number;
      key: string;
      site: SiteType;
    },
    private sanitizer: DomSanitizer
  ) {
    // console.log(this._data$);
    if (this._data$ && this._data$.key && this._data$.site) {
      const url = this.onSrcGenerate();
      this.safeSrc = this.sanitizer.bypassSecurityTrustResourceUrl(url);
    } else {
      console.warn('WatchTrailerComponent: missing dialog data', this._data$);
    }
  }

  ngOnInit() {
    //TODO: when want to see the media call this code
    this._vidFastService
      .checkAllUrl(
        `https://vidfast.{type}/${this._data$.type.toLocaleLowerCase()}/${
          this._data$.id
        }?theme=16A085&nextButton=false&autoNext=false&hideServer=true&autoPlay=true`
      )
      .subscribe({
        next: (response) => {
          console.log(response);
          if (response.httpStatus === 200 && response.tlsOk) {
            // window.open(response.finalUrl, '_blank', 'noopener,noreferrer');
          }
        },
        error: (error) => {
          console.log(error);
        },
      });
  }

  ngAfterViewInit(): void {}

  protected onSrcGenerate(): string {
    switch (this._data$?.site.toUpperCase()) {
      case SiteType.YOUTUBE.toUpperCase():
        return (
          'https://www.youtube.com/embed/' +
          this._data$.key +
          '?autoplay=1&mute=1'
        );
      case SiteType.VIMEO.toUpperCase():
        return (
          'https://player.vimeo.com/video/' +
          this._data$.key +
          '?autoplay=1&mute=1'
        );
      default: {
        console.error('not find this ' + this._data$.site.toLocaleUpperCase());
        return '';
      }
    }
  }
}
