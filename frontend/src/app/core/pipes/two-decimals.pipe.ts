import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'twoDecimals',
})
export class TwoDecimalsPipe implements PipeTransform {
  transform(value: number | string | null | undefined): string | null {
    if (value === null || value === undefined || value === '') {
      return null;
    }

    const n = Number(value);
    if (!isFinite(n)) {
      return null;
    }

    return n.toFixed(2);
  }
}
