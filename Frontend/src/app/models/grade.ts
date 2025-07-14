import { Student } from './student';
import { Subject } from './subject';

export interface Grade {
  id?: number;
  score: number;
  student?: Student;
  subject: Subject;
  studentFirstName?: string;
  studentLastName?: string;
}
