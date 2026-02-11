import { HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { FilterComponent } from './shared/components/filter/filter.component';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [HttpClientModule, FilterComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  constructor() {}
}
