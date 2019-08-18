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
import {LocationComponent} from './components/places/edit-place/location/location.component';
import {FooterComponent} from './components/common/footer/footer.component';
import {DropzoneConfigInterface, DropzoneModule} from 'ngx-dropzone-wrapper';
import {MatIconModule, MatTooltipModule} from '@angular/material';

export const DEFAULT_DROPZONE_CONFIG = (): DropzoneConfigInterface => {
  return {
    url: `${environment.apiUrl}files/upload-temp`,
    maxFilesize: 10,
    acceptedFiles: 'image/*',
    createImageThumbnails: false,
    previewTemplate: '<div></div>',
    headers: LoginService.getAuthHeader()
  };
};

@NgModule({
  declarations: [
    AppComponent,
    NavBarComponent,
    LoginComponent,
    PlacesListComponent,
    EditPlaceComponent,
    PlaceComponent,
    LocationComponent,
    FooterComponent
  ],
  imports: [
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
    MatTooltipModule,
    MatIconModule
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
