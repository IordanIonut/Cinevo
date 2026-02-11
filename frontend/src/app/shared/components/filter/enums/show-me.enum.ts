export enum ShowMe {
  EVERYTHING = 'Everything',
  NOT_SEE = "Movies I Haven't Seen",
  SEE = 'Movies I Have Seen',
}

export interface ShowMeItem {
  key: keyof typeof ShowMe;
  value: ShowMe;
}

export function mapShowMeEnum(): ShowMeItem[] {
  return Object.entries(ShowMe).map(([key, value]) => ({
    key: key as keyof typeof ShowMe,
    value: value as ShowMe,
  }));
}
