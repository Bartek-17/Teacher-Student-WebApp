import { Component, OnInit } from '@angular/core';
import { Subject } from '../../models/subject';
import { SubjectService } from '../subject.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-subject-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './subject-list.component.html',
  styleUrls: ['./subject-list.component.css']
})
export class SubjectListComponent implements OnInit {
  subjects: Subject[] = [];
  filteredSubjects: Subject[] = [];
  searchTerm: string = '';

  constructor(private subjectService: SubjectService) { }

  ngOnInit(): void {
    this.loadSubjects();
  }

  loadSubjects(): void {
    this.subjectService.getSubjects().subscribe({
      next: (data) => {
        console.log('Received subjects:', data);
        this.subjects = data;
        this.filteredSubjects = [...this.subjects];
      },
      error: (error) => {
        console.error('Error while loading subjects:', error);
      }
    });
  }


  isAscending: boolean = true;

  sortAlphabetically(): void {
    const direction = this.isAscending ? 1 : -1;
    this.filteredSubjects.sort((a, b) => direction * a.name.localeCompare(b.name));
    this.isAscending = !this.isAscending;
  }


  search(): void {
    if (!this.searchTerm.trim()) {
      this.filteredSubjects = [...this.subjects];
    } else {
      const term = this.searchTerm.toLowerCase();
      this.filteredSubjects = this.subjects.filter(subject =>
        subject.name.toLowerCase().includes(term) ||
        (subject.teacher &&
          (subject.teacher.firstName?.toLowerCase().includes(term) ||
            subject.teacher.lastName?.toLowerCase().includes(term)))
      );
    }
  }
}
