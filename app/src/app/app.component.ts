import {Component, OnInit} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {User} from "./model/user";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
    title = 'Football';
    loading: boolean = true;
    users: User[] = [];

    constructor(private http: HttpClient) {
    }

    ngOnInit() {
      this.loading = true;
        this.http.get<User[]>('/users').subscribe(data => {
            this.users = data;
            this.loading = false;
        });
    }
}
