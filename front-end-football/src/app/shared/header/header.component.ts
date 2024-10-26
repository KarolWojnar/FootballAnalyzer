import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  url: string = '';

  constructor(private route: Router) {}

  ngOnInit(): void {
    this.setUrl();
  }

  setUrl() {
    setTimeout(() => {
      this.url = this.route.url.split('/')[1];
    }, 10);
  }
}
