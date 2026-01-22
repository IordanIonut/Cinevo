import { MediaType } from '../enums/media-type.enum';
import { SiteType } from '../enums/site-type.enum';

export interface MediaView {
  id: number;
  type: MediaType;
  title: string;
  cinevo_id: string;
  poster_path: string;
  release_date: string;
  vote_average: number;
  runtime: number;
  key: string;
  first_air_date: string;
  site: SiteType;
  season_number: number;
}
