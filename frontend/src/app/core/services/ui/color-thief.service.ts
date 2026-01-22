import { isPlatformBrowser } from '@angular/common';
import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import ColorThief from 'colorthief';

@Injectable({ providedIn: 'root' })
export class ColorThiefService {
  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  private loadImage(src: string): Promise<HTMLImageElement> {
    return new Promise((resolve, reject) => {
      if (!isPlatformBrowser(this.platformId)) {
        return reject('ColorThief can only run in the browser');
      }
      const img = new Image();
      img.crossOrigin = 'Anonymous';
      img.src = src;
      img.onload = () => resolve(img);
      img.onerror = (err) => reject(err);
    });
  }
  async getPalette(src: string, count = 5): Promise<number[][]> {
    if (!isPlatformBrowser(this.platformId)) return [];
    const img = await this.loadImage(src);
    const thief = new ColorThief();
    return thief.getPalette(img, count);
  }
  async getGradient(src: string, count = 3): Promise<string> {
    if (!isPlatformBrowser(this.platformId)) return '';
    const palette = await this.getPalette(src, count);
    if (!palette.length) return '';
    const stops = palette
      .slice(0, count)
      .map((c) => `rgb(${c.join(',')})`)
      .join(', ');
    return `linear-gradient(to bottom, ${stops})`;
  }
}
