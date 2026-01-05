import { isPlatformBrowser } from '@angular/common';
import { Inject, Injectable, PLATFORM_ID } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ColorThiefService {
  private ct: any | null = null;
  private isBrowser: boolean;

  constructor(@Inject(PLATFORM_ID) platformId: Object) {
    this.isBrowser = isPlatformBrowser(platformId);
  }

  private async ensureColorThief() {
    if (!this.isBrowser) throw new Error('Browser only');
    if (this.ct) return this.ct;

    const mod = await import('colorthief');
    this.ct = (mod as any).default ?? mod;
    return this.ct;
  }

  /** Load any image (blob URL or remote URL) */
  private loadImage(url: string): Promise<HTMLImageElement> {
    if (!this.isBrowser) throw new Error('Browser only');

    return new Promise((resolve, reject) => {
      const img = new Image();
      img.crossOrigin = 'anonymous';

      img.onload = () => resolve(img);
      img.onerror = () => reject(new Error('Image failed to load'));

      img.src = url;
    });
  }

  /** Draw image to a small canvas */
  private drawToCanvas(img: HTMLImageElement, cols: number, rows: number) {
    const canvas = document.createElement('canvas');
    canvas.width = cols;
    canvas.height = rows;

    const ctx = canvas.getContext('2d');
    if (!ctx) throw new Error('Canvas 2D unavailable');

    ctx.drawImage(img, 0, 0, cols, rows);
    return canvas;
  }

  /** Convert RGB array → CSS string */
  private rgbToCss([r, g, b]: number[]) {
    return `rgb(${r}, ${g}, ${b})`;
  }

  /** Main API: get color grid from URL or blob URL */
  async getColorGridFromUrl(
    url: string,
    cols = 40,
    rows = 40,
    quantizeCount = 0
  ): Promise<string[][]> {
    if (!this.isBrowser) throw new Error('Browser only');

    const img = await this.loadImage(url);
    const canvas = this.drawToCanvas(img, cols, rows);

    let palette: number[][] | null = null;

    if (quantizeCount > 0) {
      const ct = await this.ensureColorThief();
      palette = ct.getPalette(canvas, quantizeCount);
    }

    const ctx = canvas.getContext('2d')!;
    const data = ctx.getImageData(0, 0, cols, rows).data;

    const grid: string[][] = [];
    let idx = 0;

    for (let r = 0; r < rows; r++) {
      const row: string[] = [];

      for (let c = 0; c < cols; c++) {
        const rgb = [data[idx], data[idx + 1], data[idx + 2]];
        idx += 4;

        row.push(this.rgbToCss(rgb));
      }

      grid.push(row);
    }

    return grid;
  }
}
