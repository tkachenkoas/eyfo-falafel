import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Subject} from 'rxjs';
import {Router} from "@angular/router";


export interface IAuthenticatedResult {
  authenticated: boolean,
  message?: string
}

interface AuthenticationResponse {
  token: string;
}


@Injectable({
  providedIn: 'root'
})
export class LoginService {
  authSubject = new Subject<IAuthenticatedResult>();
  private TOKEN_KEY: string = 'xAuthToken';

  constructor(private http: HttpClient, private router: Router) {
  }

  sendCredentials(username: string, password: string) {
    const headers = new HttpHeaders({
      'Content-type': 'application/json',
      'Authorization': 'Basic ' + btoa(username + ':' + password)
    });

    return this.http.get('token', {headers: headers})
      .subscribe(
        (res: AuthenticationResponse) => {
          localStorage.setItem(this.TOKEN_KEY, res.token);
          this.authSubject.next({authenticated: true});
        },
        (err: string) => {
          this.authSubject.next({authenticated: false, message: err});
        }
      );
  }

  checkSession() {
    this.http.get('checkSession')
      .subscribe(() => {
          this.authSubject.next({authenticated: true})
        },
        () => {
          this.performLogout()
        }
      );
  }

  private performLogout() {
    this.authSubject.next({authenticated: false});
    localStorage.removeItem(this.TOKEN_KEY);
    this.router.navigate(['/login'])
  }

  logout() {
    this.http.post('user/logout', '')
      .toPromise()
      .then(() => {
        this.performLogout()
      });
  }

  getToken(): string {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

}
