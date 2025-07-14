import { Teacher } from './teacher';

export interface Subject {
  id?: number;
  name: string;
  teacher?: Teacher;
}
