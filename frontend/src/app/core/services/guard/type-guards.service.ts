import { Injectable } from '@angular/core';
import { MediaView } from '../../../shared/models/views/media-views';
import { PersonView } from '../../../shared/models/views/person-view';

@Injectable({
  providedIn: 'root',
})
export class TypeGuardsService {
  isMediaView(x: MediaView | PersonView | null | undefined): x is MediaView {
    return !!x && (x as MediaView).title !== undefined;
  }

  isPersonView(x: MediaView | PersonView | null | undefined): x is PersonView {
    return !!x && (x as PersonView).known_for_department !== undefined;
  }
}
