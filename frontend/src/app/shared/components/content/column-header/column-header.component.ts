import { CommonModule } from '@angular/common';
import {
  Component,
  input,
  InputSignal,
  OnInit,
  output,
  OutputEmitterRef,
} from '@angular/core';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatIconModule } from '@angular/material/icon';
import { ColumnHeader, Possibility, ToggleGroup } from '../content';

@Component({
  selector: 'app-column-header',
  imports: [MatButtonToggleModule, CommonModule, MatIconModule],
  templateUrl: './column-header.component.html',
  styleUrls: ['./column-header.component.css'],
})
export class ColumnHeaderComponent implements OnInit {
  public readonly columnHeader$: InputSignal<ColumnHeader> =
    input.required<ColumnHeader>();

  public readonly toggleChange$: OutputEmitterRef<{
    toggle: Possibility[];
    value: string;
  }> = output();

  constructor() {}

  ngOnInit() {}

  protected onToggleChange(event: string, group: ToggleGroup): void {
    if (!event || group.selected_value === event) {
      return;
    }
    // console.log('Selected value:', event);
    // console.log('Group changed:', group);
    this.toggleChange$.emit({ toggle: group.possibility, value: event });
  }
}
