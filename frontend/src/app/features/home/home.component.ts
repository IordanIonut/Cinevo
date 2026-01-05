import {
  Component,
  effect,
  OnInit,
  signal,
  WritableSignal,
} from '@angular/core';
import { MediaService } from '../../core/services/model/media.service';
import { Content, Possibility } from '../../shared/components/content/content';
import { ContentComponent } from '../../shared/components/content/content.component';
import { MediaType } from '../../shared/models/enums/media-type';
import { TimeWindow } from '../../shared/models/enums/time-window';
import { MediaView } from '../../shared/models/views/media-views';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  imports: [ContentComponent],
  providers: [MediaService],
})
export class HomeComponent implements OnInit {
  protected readonly trending$: WritableSignal<Content> = signal<Content>({
    column_header: {
      name: 'Trending',
      toggle_group: [
        {
          aria_label: 'Type',
          name: 'Type',
          selected_value: MediaType.MOVIE.toString(),
          possibility: [
            {
              value: MediaType.MOVIE.toString(),
              name: MediaType.MOVIE.toString(),
            },
            {
              value: MediaType.TV.toString(),
              name: MediaType.TV.toString(),
            },
          ],
        },
        {
          aria_label: 'Time',
          name: 'Time',
          selected_value: TimeWindow.DAY.toString(),
          possibility: [
            {
              value: TimeWindow.DAY.toString(),
              name: TimeWindow.DAY.toString(),
            },
            {
              value: TimeWindow.WEEK.toString(),
              name: TimeWindow.WEEK.toString(),
            },
          ],
        },
      ],
    },
  });

  protected readonly media_trending$: WritableSignal<MediaView[]> = signal<
    MediaView[]
  >([]);

  constructor(
    private _mediaService: MediaService,
  ) {
    effect(() => {
      const trending = this.trending$();
      // console.log('Trending changed:', trending);

      this.media_trending$.set([]);
      this._mediaService
        .getMediaUsingTrending(
          trending.column_header.toggle_group[0].selected_value.toUpperCase(),
          trending.column_header.toggle_group[1].selected_value.toUpperCase()
        )
        .subscribe({
          next: (data) => {
            // console.log(data);
            this.media_trending$.set(data);
          },
          error: (error) => {
            console.log(error);
          },
        });
    });
  }

  ngOnInit() {}

  protected onToggleChange(event: {
    toggle: Possibility[];
    value: string;
  }): void {
    // console.log(event);
    this.trending$.update((content) => {
      return {
        ...content,
        column_header: {
          ...content.column_header,
          toggle_group: content.column_header.toggle_group.map((group) =>
            group.possibility === event.toggle
              ? { ...group, selected_value: event.value }
              : group
          ),
        },
      };
    });
  }
}
