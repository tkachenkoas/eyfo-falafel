import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Subject} from 'rxjs';
import {Router} from "@angular/router";


export interface IAuthenticatedResult {
  authenticated: boolean,
  message?: string
}

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  authSubject = new Subject<IAuthenticatedResult>();

  constructor(private http: HttpClient, private router: Router) {
  }

  sendCredentials(username: string, password: string) {
    const headers = new HttpHeaders({
      'Content-type': 'application/json',
      'Authorization': 'Basic ' + btoa(username + ':' + password)
    });

    return this.http.get('token', {headers: headers})
      .subscribe(
        res => {
          localStorage.setItem('xAuthToken', res['token']);
          this.authSubject.next({authenticated: true});
        },
        err => {
          this.authSubject.next({authenticated: false});
        }
      );
  }

  checkSession() {
    this.http.get('checkSession')
      .subscribe((response) => {
          this.authSubject.next({authenticated: true});
        },
        error => {this.performLogout()}
      );
  }

  private performLogout() {
    this.authSubject.next({authenticated: false});
    localStorage.removeItem('xAuthToken');
    this.router.navigate(['/login'])
  }

  logout() {
    this.http.post('user/logout', '').toPromise()
      .then(() => {this.performLogout()});
  }

  getToken(): string {
    return localStorage.getItem('xAuthToken');
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

}
