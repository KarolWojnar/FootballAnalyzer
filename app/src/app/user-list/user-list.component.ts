import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterLink} from "@angular/router";
import {MatButtonModule} from "@angular/material/button";
import {MatTableModule} from "@angular/material/table";
import {MatIconModule} from "@angular/material/icon";
import {User} from "../model/user";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, RouterLink, MatButtonModule, MatTableModule, MatIconModule],
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent {
  displayedColumns: string[] = ['id', 'first name', 'last name', 'email', 'login', 'role'];
  title: string = 'Users';
  loading: boolean = true;
  users: User[] = [];
  feedback: any = {}

  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.http.get<User[]>('/users').subscribe(data => {
      this.users = data;
      this.loading = false;
      this.feedback = {};
    });
  }
}
