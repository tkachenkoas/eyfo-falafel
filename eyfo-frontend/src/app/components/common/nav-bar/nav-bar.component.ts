import {Component, OnDestroy, OnInit} from '@angular/core';
import {LoginService} from '../../../services/login.service';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit, OnDestroy {

  loggedIn = false;
  constructor(private loginService: LoginService) {}

  logout() {
    this.loginService.logout();
  }

  ngOnInit() {
    this.loginService.authSubject
        .subscribe(event => this.loggedIn = event.authenticated);
  }

  ngOnDestroy(): void {
    this.loginService.authSubject.unsubscribe();
  }
}
