import { MediaType } from '../enums/media-type.enum';

export interface CountryView {
  code: string;
  name: string;
  type: MediaType;
  cinevo_id: string;
}
