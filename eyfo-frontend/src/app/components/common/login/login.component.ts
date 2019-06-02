import {Component, OnDestroy, OnInit} from '@angular/core';
import {LoginService} from '../../../services/login.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {

  loginForm: FormGroup;
  loggedIn = false;
  errorMessage: string;
  constructor(private loginService: LoginService
            , private router: Router
            , private formBuilder: FormBuilder) {}
  onSubmit() {
    this.errorMessage = '';
    this.loginService.sendCredentials(this.loginForm.value.login, this.loginForm.value.password);
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      login: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });

    this.loginService.authSubject.subscribe( (login) => {
                  if (login.success) {
                    this.loggedIn = true;
                    this.router.navigate(['places']);
                  } else {
                    this.errorMessage = login.message;
                  }
                });
  }

  ngOnDestroy(): void {
    this.loginService.authSubject.unsubscribe();
  }

  get f() { return this.loginForm.controls; }

}
