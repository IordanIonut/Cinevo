import { Component, computed, input, InputSignal, OnInit } from '@angular/core';
import { TwoDecimalsPipe } from '../../../../core/pipes/two-decimals.pipe';

@Component({
  selector: 'app-percent',
  templateUrl: './percent.component.html',
  styleUrls: ['./percent.component.css'],
  imports: [TwoDecimalsPipe],
})
export class PercentComponent implements OnInit {
  public readonly percent$: InputSignal<number | null> = input<number | null>(
    null,
  );
  public readonly isSkeleton = computed(() => this.percent$() == null);

  constructor() {}

  ngOnInit() {}

  protected onGetColor(): string {
    if (this.isSkeleton()) {
      return '';
    }
    const value = this.percent$();

    const ranges = [
      { max: 0.0, color: '#ffffff' },
      { max: 2.5, color: '#e74c3c' },
      { max: 5.0, color: '#e67e22' },
      { max: 7.5, color: '#f1c40f' },
      { max: 10.0, color: '#4cc790' },
    ];

    return ranges.find((r) => value! < r.max)?.color ?? '#ffffff';
  }
}
