import { Injectable, Renderer2, RendererFactory2 } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  private themeFromLocalStorage = localStorage.getItem('darkMode');
  private darkMode = new BehaviorSubject<boolean>(
    this.themeFromLocalStorage === 'true',
  );
  private renderer: Renderer2;
  darkMode$ = this.darkMode.asObservable();

  constructor(rendererFactory: RendererFactory2) {
    this.renderer = rendererFactory.createRenderer(null, null);
    this.initTheme(this.themeFromLocalStorage === 'true');
  }

  initTheme(isDark: boolean) {
    this.darkMode.next(isDark);
    if (isDark) {
      this.renderer.addClass(document.body, 'dark-mode');
      this.renderer.removeClass(document.body, 'light-mode');
    } else {
      this.renderer.removeClass(document.body, 'dark-mode');
      this.renderer.addClass(document.body, 'light-mode');
    }
  }

  toggleDarkMode() {
    const isDark = !this.darkMode.value;
    localStorage.setItem('darkMode', isDark.toString());
    this.initTheme(isDark);
  }
}
