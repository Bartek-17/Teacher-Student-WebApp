import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GradeService } from '../grade.service';

@Component({
  selector: 'app-my-grades',
  templateUrl: './my-grades.component.html',
  styleUrls: ['./my-grades.component.css'],
  standalone: true,
  imports: [CommonModule],
})
export class MyGradesComponent {
  grades: any[] = [];  // grade list

  constructor(private gradeService: GradeService) {}

  ngOnInit(): void {
    this.gradeService.getMyGrades().subscribe({
      next: (data) => {
        console.log('Grades received from backend:', data);
        this.grades = data;
      },
      error: (err) => {
        console.error('Error fetching grades:', err);
      },
    });
  }
}
