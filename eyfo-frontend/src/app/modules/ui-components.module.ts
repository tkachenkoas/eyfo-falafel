import {NgModule} from '@angular/core';
import {
  MatAutocompleteModule,
  MatButtonModule,
  MatCardModule, MatDialogModule,
  MatFormFieldModule,
  MatGridListModule, MatIconModule,
  MatInputModule, MatPaginatorModule, MatProgressBarModule,
  MatTableModule,
  MatToolbarModule, MatTooltipModule
} from "@angular/material";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {DragScrollModule} from "ngx-drag-scroll";

@NgModule({
  imports: [
    MatDialogModule,
    MatButtonModule,
    MatToolbarModule,
    MatProgressSpinnerModule,
    MatAutocompleteModule,
    MatTableModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatGridListModule,
    DragScrollModule,
    MatTooltipModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressBarModule
  ],
  exports: [
    MatDialogModule,
    MatButtonModule,
    MatToolbarModule,
    MatProgressSpinnerModule,
    MatAutocompleteModule,
    MatTableModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatGridListModule,
    DragScrollModule,
    MatTooltipModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressBarModule
  ]
})
export class UiComponentsModule {
}
