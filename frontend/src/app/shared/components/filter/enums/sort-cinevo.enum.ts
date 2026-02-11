export enum SortCinevo {
  POPULARITY_DESCENDING = 'Popularity Descending',
  POPULARITY_ASCENDING = 'Popularity Ascending',
  RATING_DESCENDING = 'Rating Descending',
  RATING_ASCENDING = 'Rating Ascending',
  RELEASE_DATE_DESCENDING = 'Release Date Descending',
  RELEASE_DATE_ASCENDING = 'Release Date Ascending',
}
export interface SortCinevoItem {
  key: keyof typeof SortCinevo;
  value: SortCinevo;
}

export function mapSortCinevoEnum(): SortCinevoItem[] {
  return Object.entries(SortCinevo).map(([key, value]) => ({
    key: key as keyof typeof SortCinevo,
    value: value as SortCinevo,
  }));
}
