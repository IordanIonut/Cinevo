import { NgxSliderModule } from '@angular-slider/ngx-slider';
import { isPlatformBrowser, LowerCasePipe } from '@angular/common';
import {
  Component,
  computed,
  effect,
  Inject,
  OnInit,
  PLATFORM_ID,
  Signal,
  signal,
} from '@angular/core';
import { FormBuilder, FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatOptionModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { LocationService } from '../../../core/services/location.service';
import { CountryService } from '../../../core/services/model/country.service';
import { GenreService } from '../../../core/services/model/genre.service';
import { SpokenLanguageService } from '../../../core/services/model/spoken-language.service';
import { MediaType } from '../../models/enums/media-type.enum';
import { CountryView } from '../../models/views/country-view';
import { GenreView } from '../../models/views/genre-view';
import { SpokenLanguageView } from '../../models/views/spoken-language-view';
import {
  AvailabilitiesItem,
  mapAvailabilitiesEnum,
} from './enums/availabilities.enum';
import {
  mapReleaseDatesEnum,
  ReleaseDatesItem,
} from './enums/release-dates.enums';
import { mapShowMeEnum, ShowMeItem } from './enums/show-me.enum';
import { mapSortCinevoEnum, SortCinevoItem } from './enums/sort-cinevo.enum';

@Component({
  selector: 'app-filter',
  standalone: true,
  imports: [
    MatExpansionModule,
    MatButtonModule,
    MatFormFieldModule,
    MatSelectModule,
    MatOptionModule,
    MatIconModule,
    MatDividerModule,
    ReactiveFormsModule,
    LowerCasePipe,
    MatInputModule,
    MatDatepickerModule,
    NgxSliderModule,
  ],
  providers: [
    CountryService,
    LocationService,
    GenreService,
    SpokenLanguageService,
  ],
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.css'],
})
export class FilterComponent implements OnInit {
  protected readonly sort$ = signal<SortCinevoItem | null>(null);
  protected readonly services$ = signal<boolean | null>(null);
  protected readonly country$ = signal<CountryView | null>(null);
  protected readonly providers$ = signal<string | null>(null);
  protected readonly showMe$ = signal<ShowMeItem | null>(null);
  protected readonly available$ = signal<AvailabilitiesItem[] | null>(null);
  protected readonly releaseDate$ = signal<ReleaseDatesItem[] | null>(null);
  protected readonly from$ = signal<string | null>(null);
  protected readonly to$ = signal<string | null>(null);
  protected readonly genres$ = signal<GenreView[] | null>(null);
  protected readonly adultContent$ = signal<boolean>(false);
  protected readonly language$ = signal<SpokenLanguageView | null>(null);
  protected readonly keywords$ = signal<string | null>(null);

  protected userScoreMin$ = signal(0);
  protected userScoreMax$ = signal(10);
  protected userScore$ = computed(() => ({
    value: this.userScoreMin$(),
    highValue: this.userScoreMax$(),
    options: {
      showTicks: true,
      showTicksValues: true,
      step: 1,
      floor: 0,
      ceil: 10,
      stepsArray: Array.from({ length: 11 }, (_, i) => ({ value: i })),
    },
  }));

  protected userVoteMin$ = signal(0);
  protected userVoteMax$ = signal(500);
  protected userVote$ = computed(() => ({
    value: this.userVoteMin$(),
    highValue: this.userVoteMax$(),
    options: {
      showTicks: true,
      showTicksValues: true,
      step: 1,
      floor: 0,
      ceil: 10,
      stepsArray: Array.from({ length: 6 }, (_, i) => ({ value: i * 100 })),
    },
  }));
  protected runtimeMin$ = signal(0);
  protected runtimeMax$ = signal(360);
  protected runtime$ = computed(() => ({
    value: this.runtimeMin$(),
    highValue: this.runtimeMax$(),
    options: {
      showTicks: true,
      showTicksValues: true,
      step: 1,
      floor: 0,
      ceil: 10,
      stepsArray: Array.from({ length: 13 }, (_, i) => ({ value: i * 30 })),
    },
  }));

  protected readonly sortForm;
  protected readonly whereToWatchForm;
  protected readonly filtersForm;

  protected readonly sortOptions$: Signal<SortCinevoItem[]> =
    signal<SortCinevoItem[]>(mapSortCinevoEnum());
  protected readonly showMeOptions$: Signal<ShowMeItem[]> =
    signal<ShowMeItem[]>(mapShowMeEnum());
  protected readonly availabilities$: Signal<AvailabilitiesItem[]> = signal<
    AvailabilitiesItem[]
  >(mapAvailabilitiesEnum());
  protected readonly releaseDates$: Signal<ReleaseDatesItem[]> = signal<
    ReleaseDatesItem[]
  >(mapReleaseDatesEnum());

  protected readonly countryView$ = signal<CountryView[]>([]);
  protected readonly genreView$ = signal<GenreView[]>([]);
  protected readonly spokenLanguageView$ = signal<SpokenLanguageView[]>([]);

  protected isBrowser = false;
  constructor(
    private _fb: FormBuilder,
    private _locationService: LocationService,
    private _countryService: CountryService,
    private _genreService: GenreService,
    private _spokenLanguageService: SpokenLanguageService,
    @Inject(PLATFORM_ID) platformId: Object,
  ) {
    this.isBrowser = isPlatformBrowser(platformId);

    this.sortForm = this._fb.group({
      sort: new FormControl<SortCinevoItem | null>(this.sortOptions$()[0]),
    });

    this.whereToWatchForm = this._fb.group({
      services: new FormControl<boolean | null>(null),
      country: new FormControl<CountryView | null>(null),
      providers: new FormControl<string | null>(null),
    });

    this.filtersForm = this._fb.group({
      showMe: new FormControl<ShowMeItem | null>(this.showMeOptions$()[0]),
      available: new FormControl<AvailabilitiesItem[] | null>([
        this.availabilities$()[0],
      ]),
      releaseDate: new FormControl<ReleaseDatesItem[] | null>([
        this.releaseDates$()[0],
      ]),
      from: new FormControl(null),
      to: new FormControl(null),
      genres: new FormControl<GenreView[] | null>(null),
      adultContent: new FormControl<boolean>(false),
      language: new FormControl<SpokenLanguageView | null>(null),
      userScore: new FormControl<number[] | null>(null),
      userVote: new FormControl<number[] | null>(null),
      runtime: new FormControl<number[] | null>(null),
      keywords: new FormControl(null),
    });

    this._countryService.findCountryViewByMediaType(MediaType.MOVIE).subscribe({
      next: (data) => {
        this.countryView$.set(data);
      },
      error: (err) => {
        console.error(err);
      },
    });

    this._genreService.getGenreByMediaType(MediaType.MOVIE).subscribe({
      next: (data) => {
        this.genreView$.set(data);
      },
      error: (err) => {
        console.error(err);
      },
    });

    this._spokenLanguageService
      .getSpokenLanguageViewByMediaType(MediaType.MOVIE)
      .subscribe({
        next: (data) => {
          this.spokenLanguageView$.set(data);
        },
        error: (err) => {
          console.error(err);
        },
      });

    this._locationService.getCountryFromIP().subscribe({
      next: (data) => {
        // console.log(data);
        const match: CountryView = this.countryView$().find(
          (c) => c.code === data,
        )!;
        this.whereToWatchForm.controls.country.setValue(match);
      },
      error: (error) => {
        console.log(error);
      },
    });

    // effect(() => {
    //   console.log('Selected sort:', this.sort$());
    // });

    // effect(() => {
    //   console.log('Selected service:', this.services$());
    // });
    // effect(() => {
    //   console.log('Selected country:', this.country$());
    // });
    // effect(() => {
    //   console.log('Selected providers:', this.providers$());
    // });

    // effect(() => {
    //   console.log('Select showMe:', this.showMe$());
    // });
    // effect(() => {
    //   console.log('Select available:', this.available$());
    // });
    // effect(() => {
    //   console.log('Select releaseDate:', this.releaseDate$());
    // });
    // effect(() => {
    //   console.log('Select from:', this.from$());
    // });
    // effect(() => {
    //   console.log('Select to:', this.to$());
    // });
    // effect(() => {
    //   console.log('Select genres:', this.genres$());
    // });
    // effect(() => {
    //   console.log('Select adult content', this.adultContent$());
    // });
    // effect(() => {
    //   console.log('Select language:', this.language$());
    // });
    effect(() => {
      const ref = this.userScore$();
      // console.log('Select userScore:', ref);
      this.filtersForm.controls.userScore.setValue([ref.value, ref.highValue]);

      // console.log(this.filtersForm.controls.userScore.value);
    });
    effect(() => {
      const ref = this.userVote$();
      // console.log('Select userVote:', ref);
      this.filtersForm.controls.userVote.setValue([ref.value, ref.highValue]);

      // console.log(this.filtersForm.controls.userVote.value);
    });

    effect(() => {
      const ref = this.runtime$();
      this.filtersForm.controls.runtime.setValue([ref.value, ref.highValue]);
      // console.log(this.filtersForm.controls.runtime.value);
    });
    // effect(() => {
    //   console.log('Select runtime:', this.runtime$());
    // });
    // effect(() => {
    //   console.log('Select keywords:', this.keywords$());
    // });
  }

  ngOnInit() {
    this.sortForm.controls.sort?.valueChanges.subscribe((v) =>
      this.sort$.set(v),
    );

    this.whereToWatchForm.controls.services?.valueChanges.subscribe((v) =>
      this.services$.set(v),
    );
    this.whereToWatchForm.controls.country?.valueChanges.subscribe((v) =>
      this.country$.set(v),
    );
    this.whereToWatchForm.controls.providers?.valueChanges.subscribe((v) =>
      this.providers$.set(v),
    );

    this.filtersForm.controls.showMe?.valueChanges.subscribe((v) =>
      this.showMe$.set(v),
    );
    this.filtersForm.controls.available?.valueChanges.subscribe((v) =>
      this.available$.set(v),
    );
    this.filtersForm.controls.releaseDate?.valueChanges.subscribe((v) =>
      this.releaseDate$.set(v),
    );
    this.filtersForm.controls.from?.valueChanges.subscribe((v) =>
      this.from$.set(v),
    );
    this.filtersForm.controls.to?.valueChanges.subscribe((v) =>
      this.to$.set(v),
    );
    this.filtersForm.controls.genres?.valueChanges.subscribe((v) =>
      this.genres$.set(v),
    );
    this.filtersForm.controls.adultContent?.valueChanges.subscribe((v) =>
      this.adultContent$.set(v!),
    );
    this.filtersForm.controls.language?.valueChanges.subscribe((v) =>
      this.language$.set(v),
    );
    this.filtersForm.controls.keywords?.valueChanges.subscribe((v) =>
      this.keywords$.set(v),
    );
  }

  protected compareSort = (
    a: SortCinevoItem | null,
    b: SortCinevoItem | null,
  ) => a?.key === b?.key;

  protected compareCountryView = (
    a: CountryView | null,
    b: CountryView | null,
  ) => a?.cinevo_id === b?.cinevo_id;

  protected compareLanguage = (
    a: SpokenLanguageView | null,
    b: SpokenLanguageView | null,
  ) => a?.cinevo_id === b?.cinevo_id || (a === null && b === null);

  onCheckboxChangeEnum(
    event: any,
    controlName: 'showMe' | 'available' | 'releaseDate',
    item: any,
  ): void {
    const control = this.filtersForm.get(controlName);
    // console.log(controlName);
    if (['showMe'].includes(controlName)) {
      if (event.target.checked) {
        control?.setValue(item);
      } else {
        control?.setValue(null);
      }
      return;
    } else if (['available', 'releaseDate'].includes(controlName)) {
      if (item.key === 'ALL') {
        let all: AvailabilitiesItem[] | ReleaseDatesItem[] = [];
        if (event.target.checked) {
          if (controlName === 'available') {
            all = [this.availabilities$()[0]];
          } else if (controlName === 'releaseDate') {
            all = [this.releaseDates$()[0]];
          }
          control?.setValue(all);
        } else {
          if (controlName === 'available') {
            all = this.availabilities$().filter((a) => a.key !== 'ALL');
          }
          if (controlName === 'releaseDate') {
            all = this.releaseDates$().filter((a) => a.key !== 'ALL');
          }
          control?.setValue(all);
        }
      } else {
        const current = Array.isArray(control?.value) ? control.value : [];
        const withoutAll = current.filter(
          (a) => a.key !== 'ALL' && a.key !== item.key,
        );
        control?.setValue([...withoutAll, item]);
      }
      return;
    } else {
      console.error('will not find case!!!!');
    }
  }

  protected isAvailabilityCheckedEnum(
    controlName: 'available' | 'releaseDate',
    item: AvailabilitiesItem | ReleaseDatesItem,
  ): boolean {
    const control = this.filtersForm.get(controlName);
    const raw = control?.value;
    const arr = Array.isArray(raw) ? raw : [];

    return arr.some((a) => a.key === item.key);
  }

  protected isAvailabilityCheckedView(item: GenreView): boolean {
    const arr = this.filtersForm.value.genres;
    return (
      Array.isArray(arr) && arr.some((g) => g.cinevo_id === item.cinevo_id)
    );
  }

  protected onCheckboxChangeView(item: GenreView): void {
    const control = this.filtersForm.get('genres');
    const current = Array.isArray(control?.value) ? control.value : [];
    const exists = current.some((g) => g.cinevo_id === item.cinevo_id);
    if (exists) {
      control?.setValue(current.filter((g) => g.cinevo_id !== item.cinevo_id));
    } else {
      control?.setValue([...current, item]);
    }
  }
}
