import {Component, OnDestroy, OnInit} from '@angular/core';
import {PlacesService} from '../../../services/places.service';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {LocationService} from '../../../services/location.service';
import {Place} from "../../../models/place";

@Component({
  selector: 'app-edit-place',
  templateUrl: './edit-place.component.html',
  styleUrls: ['./edit-place.component.css']
})
export class EditPlaceComponent implements OnInit, OnDestroy {
  public placeForm: FormGroup;

  constructor(private placesService: PlacesService
            , private locationsService: LocationService
            , private formBuilder: FormBuilder) {
  }

  ngOnDestroy(): void {
    this.locationsService.locationSubject.unsubscribe();
  }

  ngOnInit() {
    this.placeForm = this.formBuilder.group({
      name: ['', Validators.required],
      location: new FormControl()
    });
  }

  get frm() {
    return this.placeForm.controls;
  }
  hasErrors = (controlName: string) : boolean =>{
    const  control = this.frm[controlName];
    return control.touched && !control.valid;
  }

  submitForm() {
    if (!this.placeForm.valid) return;

    const place: Place = this.placeForm.value as Place;
    console.log('create place ' + place);
    this.placesService.createPlace(place);
  }
}
