import {Component, OnDestroy, OnInit} from '@angular/core';
import {LoginService} from '../../../services/login.service';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
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
      login: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required])
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

  public hasErrors  = (controlName: string) : boolean =>{
    const  control = this.loginForm.controls[controlName];
    return control.touched && !control.valid;
  }

}
