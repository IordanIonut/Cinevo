import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import {
    Component,
    computed,
    input,
    InputSignal,
    OnInit,
    signal,
    WritableSignal,
} from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { ImageProxyService } from '../../../../core/services/image-proxy.service';
import { ImageService } from '../../../../core/services/model/image.service';
import { DialogService } from '../../../dialogs/dialog.service';
import { ImageType } from '../../../models/enums/image-type.enum';
import { MediaType } from '../../../models/enums/media-type.enum';
import { ImageView } from '../../../models/views/image-views';
import { PersonView } from '../../../models/views/person-view';
import { PercentComponent } from '../percent/percent.component';

@Component({
  selector: 'app-person',
  standalone: true,
  templateUrl: './person.component.html',
  styleUrls: ['./person.component.css'],
  imports: [
    MatButtonModule,
    PercentComponent,
    MatDialogModule,
    CommonModule,
    HttpClientModule,
  ],
  providers: [DialogService, ImageService, ImageProxyService],
})
export class PersonComponent implements OnInit {
  public readonly data$: InputSignal<PersonView | null> =
    input<PersonView | null>(null);

  public readonly images$: WritableSignal<ImageView[]> = signal([]);
  public readonly hovered$: WritableSignal<boolean> = signal(false);
  public readonly currentIndex$: WritableSignal<number> = signal(0);
  public readonly paused$: WritableSignal<boolean> = signal(false);

  private hoverTimeout?: number;
  private rotationId?: number;
  private fetchedThisHover = false;
  public readonly MediaType = MediaType;
  public readonly ImageType = ImageType;

  public readonly currentImage = computed(() => {
    const imgs = this.images$();
    const idx = this.currentIndex$();
    return imgs.length ? imgs[idx % imgs.length] : null;
  });
  public readonly isSkeleton = computed(() => this.data$() == null);
  constructor(
    private dialog: DialogService,
    private imageService: ImageService,
  ) {}

  ngOnInit(): void {}

  private startRotation() {
    if (this.isSkeleton()) return;

    if (this.rotationId) {
      return;
    }

    this.rotationId = window.setInterval(() => {
      if (!this.hovered$() || this.paused$()) return;
      const imgs = this.images$();
      if (imgs.length === 0) return;
      this.currentIndex$.update((i) => (i + 1) % imgs.length);
    }, 1000);
  }

  private stopRotation() {
    if (this.rotationId) {
      clearInterval(this.rotationId);
      this.rotationId = undefined;
    }
  }

  onPosterHoverEnter() {
    if (this.isSkeleton()) return;

    clearTimeout(this.hoverTimeout);
    this.hoverTimeout = window.setTimeout(() => {
      this.hovered$.set(true);
      this.startRotation();
      if (!this.fetchedThisHover) {
        this.fetchedThisHover = true;
        this.imageService
          .findImageViewByMediaTypeAndCinevoId(
            MediaType.PERSON,
            this.data$()!.cinevo_id,
          )
          .subscribe({
            next: (imgs) => {
              // console.log(imgs);
              this.images$.set(imgs ?? []);
            },
            error: (error) => {
              console.error(error);
              this.images$.set([]);
            },
          });
      }
    }, 120);
  }

  onPosterHoverLeave() {
    if (this.isSkeleton()) return;

    clearTimeout(this.hoverTimeout);
    this.hoverTimeout = window.setTimeout(() => {
      this.hovered$.set(false);
      this.stopRotation();
      this.images$.set([]);
      this.fetchedThisHover = false;
      this.currentIndex$.set(0);
      this.paused$.set(false);
    }, 120);
  }

  protected onOpenHomePage(): void {
    if (this.data$()?.homepage !== null) {
      window.open(this.data$()?.homepage, '_blank', 'noopener,noreferrer');
    } else {
      //TODO: add the popup to say don't existing a homepage
    }
  }
}
