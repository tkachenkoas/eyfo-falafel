import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable, Subject} from 'rxjs';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private apiUrl = environment.apiUrl;
  authSubject = new Subject<{ success: boolean; message: string }>();
  private token: string;

  constructor(private http: HttpClient) {
  }

  sendCredentials(username: string, password: string) {
    const login = this.apiUrl + 'token';
    const headers = new HttpHeaders({
      'Content-type': 'application/json',
      'Authorization': 'Basic ' + btoa(username + ':' + password)
    });
    return this.http.get(login, {headers: headers})
      .subscribe(
        res => {
          localStorage.setItem('xAuthToken', res['token']);
          this.authSubject.next({success: true, message: ''});
        },
            res => {
          this.authSubject.next({success: false, message: res['error']['message']});
        }
      );
  }

  checkSession() {
    const url = this.apiUrl + 'checkSession';
    this.http.get(url)
      .subscribe(data => {
          this.authSubject.next({success: true, message: ''});
        },
        error => {
          this.authSubject.next({success: false, message: ''});
          localStorage.removeItem('xAuthToken');
        }
      );
  }

  logout() {
    const url = this.apiUrl + 'user/logout';
    return this.http.post(url, '');
  }

  getToken(): string {
    return localStorage.getItem('xAuthToken');
  }

  isAuthenticated(): boolean {
    this.checkSession();
    return this.getToken() != null;
  }

}
