import { Component, OnInit } from '@angular/core';
import { Grade } from '../../models/grade';
import { GradeService } from '../grade.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-all-grades',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './all-grades.component.html',
  styleUrls: ['./all-grades.component.css']
})
export class AllGradesComponent implements OnInit {
  grades: Grade[] = [];
  isLoading: boolean = true;
  error: string | null = null;

  constructor(private gradeService: GradeService) { }

  ngOnInit(): void {
    this.loadAllGrades();
  }

  loadAllGrades(): void {
    this.isLoading = true;
    this.gradeService.getAllGrades().subscribe({
      next: (data) => {
        this.grades = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading grades:', err);
        this.error = 'Could not load grades. Please try again later.';
        this.isLoading = false;
      }
    });
  }
}
