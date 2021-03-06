import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {LoginService} from '../services/login.service';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {Router} from '@angular/router';
import {HttpStatus} from '../util/http';
import {RestResponse} from '../models/rest-response';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private router: Router) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<RestResponse<any>>> {
    if (request.url.indexOf('token') >= 0) {
      return next.handle(request);
    }
    request = this.addAuthToken(request);
    return next.handle(request).pipe(catchError(err => {
      if (err.status === HttpStatus.UNAUTHORIZED) {
        this.router.navigate(['/login']);
      }
      return throwError(err);
    }));
  }
  addAuthToken(request: HttpRequest<any>): HttpRequest<any> {
    return request.clone({
      setHeaders: LoginService.getAuthHeader()
    });
  }
}
