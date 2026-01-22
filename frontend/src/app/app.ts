import { Component } from '@angular/core';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { HomeComponent } from './features/home/home.component';
import { NavbarComponent } from './shared/components/navbar/navbar.component';

@Component({
  selector: 'app-root',
  imports: [NavbarComponent, MatButtonToggleModule, HomeComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {}
