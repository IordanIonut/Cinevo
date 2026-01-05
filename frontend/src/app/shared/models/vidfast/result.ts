export interface Result {
  url: string;
  httpStatus: number;
  finalUrl: string;
  tlsOk: boolean;
  frameAllowed: boolean;
  frameHeaders: Map<string, string>;
  usable: boolean;
  error: string;
}
