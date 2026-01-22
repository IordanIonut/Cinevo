import { ImageType } from '../enums/image-type.enum';

export interface ImageView {
  file_path: string;
  cinevo_id: string;
  type: ImageType;
  vote_average: number;
}
