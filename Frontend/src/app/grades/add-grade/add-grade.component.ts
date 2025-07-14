import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Student } from '../../models/student';
import { Subject } from '../../models/subject';
import { GradeService } from '../grade.service';
import { StudentService } from '../../students/student.service';
import { SubjectService } from '../../subjects/subject.service';

@Component({
  selector: 'app-add-grade',
  templateUrl: './add-grade.component.html',
  styleUrls: ['./add-grade.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class AddGradeComponent implements OnInit {
  students: Student[] = [];
  subjects: Subject[] = [];
  selectedStudent: number | null = null;
  selectedSubject: number | null = null;
  score: number | null = null;

  successMessage: string = '';
  errorMessage: string = '';

  constructor(
    private gradeService: GradeService,
    private studentService: StudentService,
    private subjectService: SubjectService
  ) {}

  ngOnInit(): void {
    this.loadStudents();
    this.loadSubjects();
  }

  loadStudents(): void {
    this.studentService.getStudents().subscribe({
      next: (data) => {
        this.students = data;
      },
      error: (error) => {
        console.error('Error loading students:', error);
        this.errorMessage = 'Could not load the student list';
      }
    });
  }

  loadSubjects(): void {
    this.subjectService.getSubjects().subscribe({
      next: (data) => {
        this.subjects = data;
      },
      error: (error) => {
        console.error('Error loading subjects:', error);
        this.errorMessage = 'Could not load the subject list';
      }
    });
  }

  addGrade(): void {
    this.successMessage = '';
    this.errorMessage = '';

    if (!this.selectedStudent || !this.selectedSubject || !this.score) {
      this.errorMessage = 'Please fill all the fields';
      return;
    }

    if (this.score < 1 || this.score > 6) {
      this.errorMessage = 'Input a grade between 1 and 6';
      return;
    }

    this.gradeService.addGradeForStudent(
      this.selectedSubject,
      this.selectedStudent,
      this.score
    ).subscribe({
      next: (response) => {
        this.successMessage = 'Grade was added successfully';
        // Reset the form
        this.selectedStudent = null;
        this.selectedSubject = null;
        this.score = null;
      },
      error: (error) => {
        console.error('Error adding grade:', error);
        this.errorMessage = 'You can add only full or half grades from the range (1-6)';
      }
    });
  }
}
