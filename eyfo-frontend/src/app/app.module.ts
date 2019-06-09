import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {routing} from './app.routing';
import {AppComponent} from './app.component';
import {NavBarComponent} from './components/common/nav-bar/nav-bar.component';
import {LoginComponent} from './components/common/login/login.component';
import {LoginService} from './services/login.service';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {PlacesListComponent} from './components/places/places-list/places-list.component';
import {EditPlaceComponent} from './components/places/edit-place/edit-place.component';
import {PlaceComponent} from './components/places/place.component';
import {AuthInterceptor} from './interceptors/auth.interceptor';
import {MatAutocompleteModule} from '@angular/material';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {AgmCoreModule} from '@agm/core';
import {environment} from '../environments/environment';
import {MaterialComponentsModule} from "./modules/material-components.module";
import {APP_BASE_HREF} from "@angular/common";
import {ApiUrlInterceptor} from "./interceptors/api-url.interceptor";
import { LocationComponent } from './components/places/edit-place/location/location.component';

@NgModule({
  declarations: [
    AppComponent,
    NavBarComponent,
    LoginComponent,
    PlacesListComponent,
    EditPlaceComponent,
    PlaceComponent,
    LocationComponent
  ],
  imports: [
    MaterialComponentsModule,
    HttpClientModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    AgmCoreModule.forRoot({
      apiKey: environment.googleMapsApiKey
    }),
    routing
  ],
  providers: [
    LoginService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ApiUrlInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
