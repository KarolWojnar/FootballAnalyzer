import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-accout-activation',
  templateUrl: './accout-activation.component.html',
  styleUrls: ['./accout-activation.component.scss']
})
export class AccoutActivationComponent implements OnInit{
  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe({
      next: (params) => {
        const token = params.get('uuid');
        console.log(token)
      }
    })
  }

}
