package sdp;

import java.util.List;

class GradeAdder {
  public static final List<Grade> NO_GPA_GRADES = List.of(Grade.W, Grade.IP, Grade.F, Grade.S, Grade.U);
  public static final List<Grade> NO_CREDIT_GRADES = List.of(Grade.W, Grade.IP, Grade.F, Grade.U);
  private double totalGradePoints = 0;
  private int countOfGpaAffectingCourses = 0;
  private int inProgress = 0;
  private int countOfGraduationCourses;

  public GradeAdder() { }

  public GradeAdder(List<Course> courses) {
    addCourses(courses);
  }

  public GradeAdder(Iterable<Grade> grades) {
    addAll(grades);
  }

  private void addCourses(List<Course> courses) {
    for (Course course : courses)
      add(course.grade);
  }

  void addAll(Iterable<Grade> grades) {
    for (Grade grade : grades)
      add(grade);
  }

  void add(Grade grade) {
    countForGpaIfEligible(grade);
    countForGraduationIfEligible(grade);
    countForInProgressIfEligible(grade);
  }

  private void countForGpaIfEligible(Grade grade) {
    if (countsForGpa(grade)) {
      totalGradePoints += grade.toPoints();
      countOfGpaAffectingCourses += 1;
    }
  }

  private void countForGraduationIfEligible(Grade grade) {
    if (countsForGraduation(grade))
      countOfGraduationCourses += 1;
  }

  private void countForInProgressIfEligible(Grade grade) {
    if (isInProgress(grade))
      inProgress += 1;
  }

  private boolean countsForGraduation(Grade grade) {
    return !NO_CREDIT_GRADES.contains(grade);
  }

  private boolean countsForGpa(Grade grade) {
    return !NO_GPA_GRADES.contains(grade);
  }

  private boolean isInProgress(Grade grade) {
    return grade == Grade.IP;
  }

  private double computeGradePointAverage() {
    return countOfGpaAffectingCourses == 0 ? 0 : totalGradePoints / countOfGpaAffectingCourses;
  }

  GradeSummary getSummary() {
    return new GradeSummary(countOfGraduationCourses, totalGradePoints, computeGradePointAverage(), inProgress);
  }
}