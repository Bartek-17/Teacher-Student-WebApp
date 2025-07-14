import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Subject } from '../models/subject';

@Injectable({
  providedIn: 'root'
})
export class SubjectService {
  private API_URL = 'http://localhost:8080/api/subjects';

  constructor(private http: HttpClient) { }

  getAllSubjects(): Observable<Subject[]> {
    return this.http.get<Subject[]>(this.API_URL);
  }

  getMySubjects(): Observable<Subject[]> {
    return this.http.get<Subject[]>(`${this.API_URL}/my-subjects`);
  }

  getMyTeachingSubjects(): Observable<Subject[]> {
    return this.http.get<Subject[]>(`${this.API_URL}/my-teaching-subjects`);
  }

  createSubject(subject: Subject): Observable<Subject> {
    return this.http.post<Subject>(this.API_URL, subject);
  }

  enrollStudentToSubject(subjectId: number, studentId: number): Observable<any> {
    return this.http.post(`${this.API_URL}/${subjectId}/students/${studentId}`, {});
  }

  removeStudentFromSubject(subjectId: number, studentId: number): Observable<any> {
    return this.http.delete(`${this.API_URL}/${subjectId}/students/${studentId}`);
  }
}
