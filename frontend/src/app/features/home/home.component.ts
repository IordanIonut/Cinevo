import {
  Component,
  effect,
  OnInit,
  signal,
  WritableSignal,
} from '@angular/core';
import { MediaService } from '../../core/services/model/media.service';
import { PersonService } from '../../core/services/model/person.service';
import {
  Content,
  Possibility,
  ToggleGroup,
} from '../../shared/components/content/content';
import { ContentComponent } from '../../shared/components/content/content.component';
import { MediaType } from '../../shared/models/enums/media-type.enum';
import { TimeWindow } from '../../shared/models/enums/time-window.enum';
import { MediaView } from '../../shared/models/views/media-views';
import { PersonView } from '../../shared/models/views/person-view';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  imports: [ContentComponent],
  providers: [MediaService, PersonService],
})
export class HomeComponent implements OnInit {
  protected readonly trending$: WritableSignal<Content> = signal<Content>({
    column_header: {
      name: 'Trending',
      toggle_group: [
        {
          aria_label: 'Type',
          name: 'Type',
          selected_value: MediaType.PERSON.toString(),
          possibility: [
            {
              value: MediaType.MOVIE.toString(),
              name: MediaType.MOVIE.toString(),
            },
            {
              value: MediaType.TV.toString(),
              name: MediaType.TV.toString(),
            },
            {
              value: MediaType.PERSON.toString(),
              name: MediaType.PERSON.toString(),
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
  protected readonly feeToWatch$: WritableSignal<Content> = signal<Content>({
    column_header: {
      name: 'Free To Watch',
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
      ],
    },
  });

  protected readonly content_trending$: WritableSignal<
    MediaView[] | PersonView[]
  > = signal<MediaView[] | PersonView[]>([]);
  protected readonly content_freeToWatch$: WritableSignal<MediaView[]> = signal<
    MediaView[]
  >([]);

  constructor(
    private _mediaService: MediaService,
    private _personService: PersonService,
  ) {
    effect(() => {
      const trending = this.trending$();

      this.content_trending$.set([]);
      if (
        trending.column_header.toggle_group[0].selected_value.toUpperCase() ===
        MediaType.PERSON.toUpperCase()
      ) {
        this._personService
          .getPersonUsingTrending(
            trending.column_header.toggle_group[1].selected_value.toUpperCase(),
          )
          .subscribe({
            next: (data) => {
              // console.log(data);
              this.content_trending$.set(data ?? []);
            },
            error: (error) => {
              console.error(error);
            },
          });
      } else {
        this._mediaService
          .getMediaUsingTrending(
            trending.column_header.toggle_group[0].selected_value.toUpperCase(),
            trending.column_header.toggle_group[1].selected_value.toUpperCase(),
          )
          .subscribe({
            next: (data) => {
              // console.log(data);
              this.content_trending$.set(data ?? []);
            },
            error: (error) => {
              console.error(error);
            },
          });
      }
    });

    effect(() => {
      const freeToWatch = this.feeToWatch$();
      this.content_freeToWatch$.set([]);

      this._mediaService
        .getFreeToWatchByMediaType(
          freeToWatch.column_header.toggle_group[0].selected_value,
        )
        .subscribe({
          next: (data) => {
            // console.log(data);
            this.content_freeToWatch$.set(data ?? []);
          },
          error: (error) => {
            console.log(error);
            this.content_freeToWatch$.set([]);
          },
        });
    });
  }

  ngOnInit() {}

  protected onToggleChangeTrending(event: {
    toggle: Possibility[];
    value: string;
  }): void {
    this.updateToggleGroup(
      this.trending$,
      (group) => group.possibility === event.toggle,
      event.value,
    );
  }

  protected onToggleChangeFreeToWatch(event: {
    toggle: Possibility[];
    value: string;
  }): void {
    this.updateToggleGroup(
      this.feeToWatch$,
      (group) => group.possibility === event.toggle,
      event.value,
    );
  }

  private updateToggleGroup(
    signal: WritableSignal<Content>,
    match: (group: ToggleGroup) => boolean,
    newValue: string,
  ): void {
    signal.update((content) => {
      const groups = content.column_header.toggle_group.map((g) => ({ ...g }));
      for (let i = 0; i < groups.length; i++) {
        if (match(groups[i])) {
          groups[i] = { ...groups[i], selected_value: newValue };
          break;
        }
      }
      return {
        ...content,
        column_header: {
          ...content.column_header,
          toggle_group: groups,
        },
      };
    });
  }
}
