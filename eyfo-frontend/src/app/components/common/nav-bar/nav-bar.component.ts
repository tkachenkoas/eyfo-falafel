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
    this.loginService.logout()
      .subscribe(res => {
        this.loggedIn = false;
        window.location.reload();
      }, error => {
        this.loggedIn = true;
      });
  }

  ngOnInit() {
    this.loginService.authSubject.subscribe(event => this.loggedIn = event.success);
    this.loggedIn = this.loginService.isAuthenticated();
  }

  ngOnDestroy(): void {
    this.loginService.authSubject.unsubscribe();
  }
}
