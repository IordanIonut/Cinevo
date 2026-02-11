export enum Availabilities {
  ALL = 'Search all availabilities?',
  STREAM = 'Stream',
  FREE = 'Free',
  ADS = 'Ads',
  RENT = 'Rent',
  BUY = 'Buy',
}

export interface AvailabilitiesItem {
  key: keyof typeof Availabilities;
  value: Availabilities;
}

export function mapAvailabilitiesEnum(): AvailabilitiesItem[] {
  return Object.entries(Availabilities).map(([key, value]) => ({
    key: key as keyof typeof Availabilities,
    value: value as Availabilities,
  }));
}
