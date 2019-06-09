import {ILocation} from './location';

export interface Place {
  id?: number;
  location?: ILocation;
  name?: string;
  priceFrom?: number;
  priceTo?: number;
}
