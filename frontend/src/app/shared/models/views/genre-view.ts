import { MediaType } from '../enums/media-type.enum';

export interface GenreView {
  name: string;
  type: MediaType;
  cinevo_id: string;
}
