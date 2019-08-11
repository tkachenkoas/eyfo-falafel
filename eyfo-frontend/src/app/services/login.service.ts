import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Subject} from 'rxjs';
import {Router} from '@angular/router';

export interface IAuthenticatedResult {
  authenticated: boolean;
  message?: string;
}

interface AuthenticationResponse {
  token: string;
}

export const TOKEN_KEY = 'x-auth-token';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  authSubject = new Subject<IAuthenticatedResult>();

  constructor(private http: HttpClient, private router: Router) {
  }

  static getAuthHeader(): {[name: string]: string} {
    const authHeaders = {};
    authHeaders[TOKEN_KEY] = LoginService.getToken();
    return authHeaders;
  }

  static getToken(): string {
    return localStorage.getItem(TOKEN_KEY);
  }

  static isAuthenticated(): boolean {
    return !!LoginService.getToken();
  }

  sendCredentials(username: string, password: string) {
    const headers = new HttpHeaders({
      'Content-type': 'application/json',
      'Authorization': 'Basic ' + btoa(username + ':' + password)
    });

    return this.http.get('token', {headers: headers})
      .subscribe(
        (res: AuthenticationResponse) => {
          localStorage.setItem(TOKEN_KEY, res.token);
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
          this.authSubject.next({authenticated: true});
        },
        () => {
          this.performLogout();
        }
      );
  }

  private performLogout() {
    this.authSubject.next({authenticated: false});
    localStorage.removeItem(TOKEN_KEY);
    this.router.navigate(['/login']);
  }

  logout() {
    this.http.post('user/logout', '')
      .toPromise()
      .then(() => {
        this.performLogout();
      });
  }

}
