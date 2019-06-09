import {ILocation} from './location';

export interface Place {
  id?: number;
  description?: string;
  location?: ILocation;
  name?: string;
}
