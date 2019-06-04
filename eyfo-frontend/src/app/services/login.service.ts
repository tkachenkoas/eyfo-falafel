import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Subject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  authSubject = new Subject<{ success: boolean; message: string }>();
  private token: string;

  constructor(private http: HttpClient) {
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
          this.authSubject.next({success: true, message: ''});
        },
            res => {
          this.authSubject.next({success: false, message: res['error']['message']});
        }
      );
  }

  checkSession() {
    this.http.get('checkSession')
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
    return this.http.post('user/logout', '');
  }

  getToken(): string {
    return localStorage.getItem('xAuthToken');
  }

  isAuthenticated(): boolean {
    this.checkSession();
    return this.getToken() != null;
  }

}
