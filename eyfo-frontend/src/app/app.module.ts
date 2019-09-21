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
import {AgmCoreModule} from '@agm/core';
import {environment} from '../environments/environment';
import {UiComponentsModule} from './modules/ui-components.module';
import {ApiUrlInterceptor} from './interceptors/api-url.interceptor';
import {PlaceLocationComponent} from './components/places/edit-place/place-location/place-location.component';
import {FooterComponent} from './components/common/footer/footer.component';
import {DropzoneModule} from 'ngx-dropzone-wrapper';
import {MatPaginatorModule} from '@angular/material';
import { ConfirmationDialogComponent } from './components/common/confirmation-dialog/confirmation-dialog.component';
import { NearbyPlacesComponent } from './components/places/nearby-places/nearby-places.component';
import { ListHeaderComponent } from './components/places/list-header/list-header.component';
import { ViewPlaceComponent } from './components/places/view-place/view-place.component';
import {NgImageSliderModule} from "ng-image-slider";
import {RatingModule} from "ng-starrating";

@NgModule({
  declarations: [
    AppComponent,
    NavBarComponent,
    LoginComponent,
    PlacesListComponent,
    EditPlaceComponent,
    PlaceComponent,
    PlaceLocationComponent,
    FooterComponent,
    ConfirmationDialogComponent,
    NearbyPlacesComponent,
    ListHeaderComponent,
    ViewPlaceComponent
  ],
  imports: [
    NgImageSliderModule,
    UiComponentsModule,
    HttpClientModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    DropzoneModule,
    ReactiveFormsModule,
    AgmCoreModule.forRoot({
      apiKey: environment.googleMapsApiKey
    }),
    routing,
    RatingModule
  ],
  exports : [
    MatPaginatorModule
  ],
  entryComponents: [
    ConfirmationDialogComponent
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
