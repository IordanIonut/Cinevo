import { CommonModule, NgClass } from '@angular/common';
import {
  AfterViewInit,
  Component,
  ElementRef,
  HostListener,
  OnInit,
  signal,
  ViewChild,
} from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  imports: [NgClass, CommonModule],
})
export class NavbarComponent implements OnInit, AfterViewInit {
  protected readonly navbar$ = new BehaviorSubject<Navbar[]>([
    {
      name: 'Movie',
      link: '',
      links: [
        { link: '', title: 'Popular', describe: '123123123' },
        { link: '', title: 'Now Playing', describe: '123123' },
        { link: '', title: 'Upcoming', describe: '12312' },
        { link: '', title: 'Top Rated', describe: '123123123123' },
      ],
      image: `<svg xmlns="http://www.w3.org/2000/svg" ...></svg>`,
    },
    {
      name: 'TV Shows',
      link: '',
      links: [
        { link: '', title: 'Popular', describe: '123123123' },
        { link: '', title: 'Airing Today', describe: '123123123123' },
        { link: '', title: 'On TV', describe: '123213123' },
        { link: '', title: 'Top Rated', describe: '12312312' },
      ],
    },
    {
      name: 'Peoples',
      link: '',
      links: [{ link: '', title: 'Popular', describe: '123123123' }],
    },
  ]);

  @ViewChild('topNavbar') topNavbar!: ElementRef<HTMLElement>;
  protected readonly isNavbarVisible$ = signal(false);

  constructor(private sanitizer: DomSanitizer) {}

  ngOnInit() {}

  ngAfterViewInit() {}

  @HostListener('window:scroll') onScroll() {
    const y = window.scrollY || document.documentElement.scrollTop;
    this.isNavbarVisible$.set(y > 100);
  }

  protected sanitize(html: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(html);
  }
}

interface Navbar {
  name: string;
  link: string;
  links: Links[];
  image?: string;
}

interface Links {
  link: string;
  title: string;
  describe: string;
}
