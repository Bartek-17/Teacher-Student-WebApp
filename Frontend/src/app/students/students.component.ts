// src/app/students/students.component.ts
import {Component, inject, Input} from '@angular/core';
import { Student } from './student';
import {StudentService} from './student.service';

// <-- import these three from @angular/common -->
import { NgForOf, NgIf, NgClass, NgStyle } from '@angular/common';

@Component({
  selector: 'app-students',
  templateUrl: './students.component.html',
  standalone: true,
  styleUrl: './students.component.css',
  imports: [
    NgForOf,
    NgIf,       // for *ngIf
    NgClass,    // for [ngClass]
    // NgStyle     // for [ngStyle]
  ]
})

export class StudentsComponent {

  private studentService = inject (StudentService);

  studentList?: Student[];

  ngOnInit() { this.getStudents();   }

  getStudents(): void {
    this.studentService.getStudents()
      .subscribe(studentList => this.studentList = studentList);
  }

  add(firstname: string, lastname: string, email: string, telephone: string): void {
    firstname = firstname.trim();
    lastname = lastname.trim();
    email = email.trim();
    telephone = telephone.trim();
    this.studentService.addStudent({ firstname, lastname, email, telephone } as Student)
      .subscribe({
        next: (student: Student) => { this.studentList?.push(student); },
        error: () => {},
        complete: () => {
          if (this.studentList != undefined) {
            this.studentService.totalItems.next(this.studentList.length);
            console.log(this.studentList.length);
          }
        }
      });
  }

  delete(student: Student): void {
    this.studentList = this.studentList?.filter(c => c !== student);
    this.studentService.deleteStudent(student).subscribe(() => {
        // for automatic update of number of students in parent component
        if(this.studentList != undefined) {
          this.studentService.totalItems.next(this.studentList.length);
          console.log(this.studentList.length);
        }
      }
    );
  }

  deleteAll(): void {
    this.studentService.deleteStudents().subscribe(() => {
        if(this.studentList != undefined) {
          this.studentList.length = 0;
        }
      }
    );
  }

  update(firstname: string, lastname: string, email: string, telephone: string, chosenToUpdateStudent:Student):void {
    let id = chosenToUpdateStudent.id;
    firstname = firstname.trim();
    lastname = lastname.trim();
    email = email.trim();
    telephone = telephone.trim();
    console.log(id);
    if (id != undefined) {
      this.studentService.updateStudent({firstname, lastname, email, telephone} as Student, id)
        .subscribe({
          next: (student: Student) => {
            if (this.studentList != undefined) {
              let index = this.studentList?.indexOf(chosenToUpdateStudent);
              this.studentList[index] = student;
            }
          },
          error: () => {
          },
          complete: () => {
            if (this.studentList != undefined) {
              this.studentService.totalItems.next(this.studentList.length);
              console.log(this.studentList.length);
            }
          }
        })
    }
  }

}





