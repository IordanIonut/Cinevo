import {
  Component,
  computed,
  input,
  InputSignal,
  OnInit,
  output,
  OutputEmitterRef,
} from '@angular/core';
import { MediaView } from '../../models/views/media-views';
import { ColumnHeaderComponent } from './column-header/column-header.component';
import { Content, Possibility } from './content';
import { PosterComponent } from './poster/poster.component';

@Component({
  selector: 'app-content',
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.css'],
  imports: [ColumnHeaderComponent, PosterComponent],
})
export class ContentComponent implements OnInit {
  public readonly content$: InputSignal<Content> = input.required<Content>();
  public readonly data$: InputSignal<MediaView[]> =
    input.required<MediaView[]>();

  public readonly toggleChange$: OutputEmitterRef<{
    toggle: Possibility[];
    value: string;
  }> = output();

  public readonly columnHeader$ = computed(() => this.content$().column_header);

  constructor() {}

  ngOnInit() {}

  protected onToggleChange(event: {
    toggle: Possibility[];
    value: string;
  }): void {
    // console.log(event);
    this.toggleChange$.emit(event);
  }

  protected trackByCinevoId(index: number, item: MediaView): string | number {
    return item.cinevoId || index;
  }
}
