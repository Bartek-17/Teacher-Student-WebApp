import { User } from './user';
import { Subject } from './subject';

export interface Student {
  id?: number;
  firstname: string;
  lastname: string;
  user?: User;
  subjects?: Subject[];
}
