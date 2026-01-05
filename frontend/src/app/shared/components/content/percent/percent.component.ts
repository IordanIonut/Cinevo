import { Component, input, InputSignal, OnInit } from '@angular/core';

@Component({
  selector: 'app-percent',
  templateUrl: './percent.component.html',
  styleUrls: ['./percent.component.css'],
})
export class PercentComponent implements OnInit {
  public readonly percent$: InputSignal<number> = input.required<number>();

  constructor() {}

  ngOnInit() {}

  protected onGetColor(): string {
    const value = this.percent$();

    const ranges = [
      { max: 0.0, color: '#ffffff' },
      { max: 2.5, color: '#e74c3c' },
      { max: 5.0, color: '#e67e22' },
      { max: 7.5, color: '#f1c40f' },
      { max: 10.0, color: '#4cc790' },
    ];

    return ranges.find((r) => value < r.max)?.color ?? '#ffffff';
  }
}
