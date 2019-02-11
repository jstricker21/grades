package sdp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class CourseReporterTest {
  @Test
  public void singleCourseGradeTotalsItself() {
    assertTotalsOfList_Are("CS 234        A ", "Units: 1.00\nGPA: 4.00\n");
    assertTotalsOfList_Are("MAT    234    A-", "Units: 1.00\nGPA: 3.67\n");
    assertTotalsOfList_Are("ENGR 121      B+", "Units: 1.00\nGPA: 3.33\n");
    assertTotalsOfList_Are("CS 234         B", "Units: 1.00\nGPA: 3.00\n");
    assertTotalsOfList_Are("CS 234        B-", "Units: 1.00\nGPA: 2.67\n");
    assertTotalsOfList_Are("ENGR 011     C+ ", "Units: 1.00\nGPA: 2.33\n");
    assertTotalsOfList_Are("ENGR 101      C ", "Units: 1.00\nGPA: 2.00\n");
    assertTotalsOfList_Are("ENGR 101    C-\n", "Units: 1.00\nGPA: 1.67\n");
    assertTotalsOfList_Are("ENGR 101      D+", "Units: 1.00\nGPA: 1.33\n");
    assertTotalsOfList_Are("CS 122L        D", "Units: 1.00\nGPA: 1.00\n");
    assertTotalsOfList_Are("CS 122L        F", "Units: 0.00\nGPA: 0.00\n");
    assertTotalsOfList_Are("CS 122L       IP", "Units: 0.00\nGPA: 0.00\nIn Progress: 1.00\n");
  }

  @Test
  public void withdrawnCoursesDontCountTowardsTotalCoursesTaken() {
    assertTotalsOfList_Are("CS 122L      W", "Units: 0.00\nGPA: 0.00\n");
    assertTotalsOfList_Are(
        "CS 234        A \nMAT 111      B\nCS 122     W\n",
        "Units: 2.00\nGPA: 3.50\n");
  }

  @Test
  public void multipleCoursesAreAllComputedForTotal() {
    assertTotalsOfList_Are(
        "CS 234        A \nMAT 111      B",
        "Units: 2.00\nGPA: 3.50\n");
    assertTotalsOfList_Are(
        "CS 234        A \nMAT 111      B\nCS 122     B-\n",
        "Units: 3.00\nGPA: 3.22\n");

  }

  @Test
  public void emptyGradeListTotalsZeroGpaAndNoCourses() {
    assertTotalsOfList_Are("", "Units: 0.00\nGPA: 0.00\n");
  }

  @Test
  public void reportCourseListContainsIncludedCoursesInAlphabeticalOrder() {
    assertAddedCoursesReportedAlphabetically(List.of(), "");
    assertAddedCoursesReportedAlphabetically(
            List.of(new Course("CS", "121C", Grade.BPLUS)),
            "CS 121C B+\n");
    assertAddedCoursesReportedAlphabetically(
            List.of(new Course("MA", "111L", Grade.CMINUS),
                    new Course("CS", "121C", Grade.BPLUS)),
            "CS 121C B+\nMA 111L C-\n");
    assertAddedCoursesReportedAlphabetically(
            List.of(new Course("CS", "131L", Grade.CMINUS),
                    new Course("CS", "121C", Grade.BPLUS),
                    new Course("CS", "021C", Grade.BPLUS)),
            "CS 021C B+\nCS 121C B+\nCS 131L C-\n");
  }

  @Test
  public void resultAddsSpacesToPropertyVerticallyAlignColumns() {
    assertAddedCoursesReportedAlphabetically(
            List.of(new Course("CS", "121C", Grade.BPLUS),
                    new Course("MAT", "121C", Grade.B)),
            "CS  121C B+\n" +
                    "MAT 121C B\n");
    assertAddedCoursesReportedAlphabetically(
            List.of(new Course("CS", "121L1", Grade.BPLUS),
                    new Course("MAT", "121C", Grade.B),
    new Course("HF", "101", Grade.AMINUS)),
            "CS  121L1 B+\n" +
                    "HF  101   A-\n" +
                    "MAT 121C  B\n");
  }

  private void assertAddedCoursesReportedAlphabetically(List<Course> courses, String output) {
    assertEquals(output, new CourseReporter().reportCourseList(courses));
  }

  static void assertTotalsOfList_Are(String input, String output) {
    List<Course> courses = new ArrayList<>();
    for (Course course : new CourseLineParser(new Scanner(input))) {
      courses.add(course);
    }
    GradeSummary summary = new GradeAdder(courses).getSummary();
    assertEquals(output, new CourseReporter().reportSummary(summary));
  }
}