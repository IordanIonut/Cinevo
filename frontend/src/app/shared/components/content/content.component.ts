import {
  Component,
  input,
  InputSignal,
  OnInit,
  output,
  OutputEmitterRef,
} from '@angular/core';
import { TypeGuardsService } from '../../../core/services/guard/type-guards.service';
import { MediaView } from '../../models/views/media-views';
import { PersonView } from '../../models/views/person-view';
import { ColumnHeaderComponent } from './column-header/column-header.component';
import { Content, Possibility } from './content';
import { MediaComponent } from './media/media.component';
import { PersonComponent } from './person/person.component';

@Component({
  selector: 'app-content',
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.css'],
  imports: [ColumnHeaderComponent, MediaComponent, PersonComponent],
})
export class ContentComponent implements OnInit {
  public readonly content$: InputSignal<Content | null> = input<Content | null>(
    null,
  );
  public readonly data$: InputSignal<MediaView[] | PersonView[]> = input<
    MediaView[] | PersonView[]
  >([]);
  public readonly toggleChange$: OutputEmitterRef<{
    toggle: Possibility[];
    value: string;
  }> = output();

  public readonly skeletons = Array.from({ length: 20 });

  constructor(protected _typeGuardsService: TypeGuardsService) {}

  ngOnInit() {
    // console.log(this.content$());
    // console.log(this.data$());
  }

  ngAfterViewInit(): void {
    console.log(this.onFindSkeleton());
  }

  protected onFindSkeleton(): 'media' | 'person' {
    const content = this.content$();
    if (!content) return 'media';
    const typeValue =
      content.column_header.toggle_group[0].selected_value ?? '';
    return typeValue.toLowerCase().includes('person') ? 'person' : 'media';
  }

  protected onToggleChange(event: {
    toggle: Possibility[];
    value: string;
  }): void {
    this.toggleChange$.emit(event);
  }

  protected trackByCinevoId(
    index: number,
    item: MediaView | PersonView,
  ): string | number {
    return item.cinevo_id || index;
  }
}
