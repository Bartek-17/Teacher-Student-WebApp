import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Grade } from '../models/grade';

@Injectable({
  providedIn: 'root',
})
export class GradeService {
  private API_URL = 'http://localhost:8080/api/grades';

  constructor(private http: HttpClient) {}

  // get all grades for admin
  getAllGrades(): Observable<Grade[]> {
    return this.http.get<Grade[]>(this.API_URL);
  }

  // get grades for logged in student
  getMyGrades(): Observable<Grade[]> {
    return this.http.get<Grade[]>(`${this.API_URL}/my-grades`);
  }

  getMyGradesForSubject(subjectId: number): Observable<Grade[]> {
    return this.http.get<Grade[]>(`${this.API_URL}/my-grades/subject/${subjectId}`);
  }

  // get single grade
  getGradeById(id: number): Observable<Grade> {
    return this.http.get<Grade>(`${this.API_URL}/${id}`);
  }

  addGrade(grade: Grade): Observable<Grade> {
    return this.http.post<Grade>(this.API_URL, grade);
  }

  addGradeForStudent(subjectId: number, studentId: number, score: number): Observable<Grade> {
    return this.http.post<Grade>(
      `${this.API_URL}/subjects/${subjectId}/students/${studentId}?score=${score}`,
      {}
    );
  }

  updateGrade(id: number, grade: Grade): Observable<Grade> {
    return this.http.put<Grade>(`${this.API_URL}/${id}`, grade);
  }

  deleteGrade(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
