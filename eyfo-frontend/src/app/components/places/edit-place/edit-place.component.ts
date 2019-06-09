import {Component, OnDestroy, OnInit} from '@angular/core';
import {PlacesService} from '../../../services/places.service';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {LocationService} from '../../../services/location.service';

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
      description: ['', Validators.required],
      location: new FormControl()
    });
  }

  get frm() {
    return this.placeForm.controls;
  }
  public hasErrors  = (controlName: string) : boolean =>{
    const  control = this.frm[controlName];
    return control.touched && !control.valid;
  }

}
