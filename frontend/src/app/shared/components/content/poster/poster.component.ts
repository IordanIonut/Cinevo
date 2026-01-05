import { CommonModule, DatePipe } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import {
  ApplicationRef,
  Component,
  computed,
  effect,
  input,
  InputSignal,
  OnInit,
  signal,
  WritableSignal
} from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { filter, take } from 'rxjs';
import { ImageProxyService } from '../../../../core/services/image-proxy.service';
import { ImageService } from '../../../../core/services/model/image.service';
import { ColorThiefService } from '../../../../core/services/ui/color-thief.service';
import { DialogService } from '../../../dialogs/dialog.service';
import { MediaType } from '../../../models/enums/media-type';
import { ImageView } from '../../../models/views/image-views';
import { MediaView } from '../../../models/views/media-views';
import { PercentComponent } from '../percent/percent.component';

@Component({
  selector: 'app-poster',
  standalone: true,
  templateUrl: './poster.component.html',
  styleUrls: ['./poster.component.css'],
  imports: [
    MatButtonModule,
    PercentComponent,
    MatDialogModule,
    CommonModule,
    HttpClientModule,
  ],
  providers: [DialogService, ImageService],
})
export class PosterComponent implements OnInit {
  public readonly data$: InputSignal<MediaView> = input.required<MediaView>();

  public readonly images$: WritableSignal<ImageView[]> = signal<ImageView[]>(
    []
  );
  public readonly hovered$: WritableSignal<boolean> = signal(false);
  private fetchedThisHover = false;
  public readonly currentIndex$: WritableSignal<number> = signal(0);
  public readonly paused$: WritableSignal<boolean> = signal(false);
  public readonly currentImage = computed(() => {
    const imgs = this.images$();
    const idx = this.currentIndex$();
    return imgs && imgs.length ? imgs[idx % imgs.length] : null;
  });
  private hoverTimeout?: number;
  private rotationIntervalMs = 1000;
  private destroyed = false;

  public readonly MediaType = MediaType;
  constructor(
    private _dialogService: DialogService,
    private _colorThiefService: ColorThiefService,
    private _imageService: ImageService,
    private _imageProxyService: ImageProxyService,
    private appRef: ApplicationRef
  ) {
    effect((onCleanup) => {
      const imgs = this.images$();
      const isHovered = this.hovered$();
      this.currentIndex$.set(0);
      if (!isHovered || !imgs || imgs.length === 0) {
        return;
      }
      const id = window.setInterval(() => {
        if (this.paused$() || this.destroyed) {
          return;
        }
        const len = this.images$().length;
        if (len === 0) {
          return;
        }
        this.currentIndex$.set((this.currentIndex$() + 1) % len);
      }, this.rotationIntervalMs);
      onCleanup(() => clearInterval(id));
    });
  }

  ngOnInit() {
    this.appRef.isStable
      .pipe(
        filter((stable) => stable),
        take(1)
      )
      .subscribe(() => {
        this.initGrid();
      });
  }

  cols = 20;
  rows = 20;
  grid: string[][] = [];
  private async initGrid() {
    const remoteUrl = this.data$().poster_path;

    this._imageProxyService.proxy(remoteUrl).subscribe({
      next: async (buffer: ArrayBuffer) => {
        const blob = new Blob([buffer], { type: 'image/jpeg' });
        const objectUrl = URL.createObjectURL(blob);

        try {
          this.grid = await this._colorThiefService.getColorGridFromUrl(
            objectUrl,
            this.cols,
            this.rows,
            6
          );
        } finally {
          URL.revokeObjectURL(objectUrl);
        }
      },
      error: (err) => console.error(err),
    });
  }

  get flatGrid(): string[] {
    return this.grid.flat();
  }

  ngAfterViewInit(): void {}

  ngOnDestroy(): void {
    this.destroyed = true;
    if (this.hoverTimeout) {
      clearTimeout(this.hoverTimeout);
      this.hoverTimeout = undefined;
    }
  }

  protected onGetReleaseDateUsingPipe(): string {
    const pipe = new DatePipe('en-US');
    const date = this.data$().release_date || this.data$().first_air_date;
    return pipe.transform(new Date(date), 'MMM d, y') ?? '';
  }

  protected onOpenTrailer(): null | void {
    if (this.data$().site == null || this.data$().key == null) {
      //TODO: create logger and here if not respect the condition
      return null;
    }
    this._dialogService.openWatchTrailer(
      this.data$().type,
      this.data$().id,
      this.data$().key,
      this.data$().site
    );
  }

  onPosterHoverEnter(item: any) {
    window.clearTimeout(this.hoverTimeout);
    this.hoverTimeout = window.setTimeout(() => {
      this.hovered$.set(true);

      if (this.fetchedThisHover) {
        return;
      }
      this.fetchedThisHover = true;

      this._imageService
        .findImageViewByMediaTypeAndId(this.data$().id, this.data$().type)
        .pipe(take(1))
        .subscribe({
          next: (response) => {
            this.images$.set(response || []);
            console.log(this.images$());
          },
          error: (error) => {
            console.error(error);
            this.images$.set([]);
          },
        });
    }, 120);
  }

  onPosterHoverLeave() {
    window.clearTimeout(this.hoverTimeout);
    this.hoverTimeout = window.setTimeout(() => {
      this.hovered$.set(false);

      this.images$.set([]);
      this.fetchedThisHover = false;

      this.currentIndex$.set(0);
      this.paused$.set(false);
    }, 120);
  }
}
