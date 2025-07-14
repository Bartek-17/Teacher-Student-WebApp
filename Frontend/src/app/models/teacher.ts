import { User } from './user';

export interface Teacher {
  id?: number;
  firstName: string;
  lastName: string;
  user?: User;
}
