export enum ReleaseDates {
  ALL = 'Search all releases?',
//   ALL_COUNTRY = 'Search all countries?', //not implement yet
  THEATRICAL_LIMITED = 'Theatrical (limited)',
  THEATRICAL = 'Theatrical',
  PREMIERE = 'Premiere',
  DIGITAL = 'Digital',
  PHYSICAL = 'Physical',
  TV = 'TV',
}

export interface ReleaseDatesItem {
  key: keyof typeof ReleaseDates;
  value: ReleaseDates;
}

export function mapReleaseDatesEnum(): ReleaseDatesItem[] {
  return Object.entries(ReleaseDates).map(([key, value]) => ({
    key: key as keyof typeof ReleaseDates,
    value: value as ReleaseDates,
  }));
}
