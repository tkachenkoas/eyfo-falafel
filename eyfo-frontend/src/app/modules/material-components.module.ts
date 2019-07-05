import {NgModule} from '@angular/core';
import {
  MatAutocompleteModule,
  MatButtonModule,
  MatCardModule,
  MatFormFieldModule, MatGridListModule, MatInputModule, MatTableModule,
  MatToolbarModule
} from "@angular/material";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {DragScrollModule} from "ngx-drag-scroll";

@NgModule({
  imports: [
    MatButtonModule,
    MatToolbarModule,
    MatProgressSpinnerModule,
    MatAutocompleteModule,
    MatTableModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatGridListModule,
    DragScrollModule
  ],
  exports: [
    MatButtonModule,
    MatToolbarModule,
    MatProgressSpinnerModule,
    MatAutocompleteModule,
    MatTableModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatGridListModule,
    DragScrollModule
  ]
})
export class MaterialComponentsModule {
}
