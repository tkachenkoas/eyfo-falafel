import {ModuleWithProviders} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './components/common/login/login.component';
import {PlacesListComponent} from './components/places/places-list/places-list.component';
import {EditPlaceComponent} from './components/places/edit-place/edit-place.component';
import {AuthGuardService as AuthGuard} from './services/auth-guard.service';
import {PlaceComponent} from './components/places/place.component';

const appRoutes: Routes = [
  { path: 'login', component: LoginComponent},
  { path : '', redirectTo : '/places', pathMatch: 'full'},
  { path : 'places', component: PlaceComponent, canActivate: [AuthGuard], children: [
      { path: '', component: PlacesListComponent },
      { path: 'new', component: EditPlaceComponent },
      { path: ':id/edit', component: EditPlaceComponent },
    ]
  }
];

export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes);
