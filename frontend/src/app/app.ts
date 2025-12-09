import { Component } from '@angular/core';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { NavbarComponent } from "./shared/components/navbar/navbar.component";
import { ColumnHeaderComponent } from "./shared/components/column-header/column-header.component";

@Component({
  selector: 'app-root',
  imports: [NavbarComponent, MatButtonToggleModule, ColumnHeaderComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
}
