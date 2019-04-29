package sdp;

import org.junit.Test;

import java.util.Scanner;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class Main {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println(processGrades(scanner));
  }

  private static String processGrades(Scanner scanner) {
    int numCourses = 0;
    double sumQualityPoints = 0;

    Map<String, Double> letterGradeToPointValue = getLetterToGradePointMap();

    while(scanner.hasNext())
    {
      scanner.next();
      scanner.next();

      String letterGrade = scanner.next();

      if(letterGrade.equals("W")) continue;

      sumQualityPoints += letterGradeToPointValue.get(letterGrade);
      numCourses++;
    }

    double GPA = numCourses == 0 ? 0 : sumQualityPoints / numCourses;
    return String.format("Courses: %d\nGPA: %.2f\n", numCourses, GPA);
  }

  private static Map<String, Double> getLetterToGradePointMap()
  {
    Map<String, Double> letterGradeToPointValue = new HashMap<String, Double>();

    letterGradeToPointValue.put("A", 4.0);
    letterGradeToPointValue.put("A-", 3.67);
    letterGradeToPointValue.put("B+", 3.33);
    letterGradeToPointValue.put("B", 3.0);
    letterGradeToPointValue.put("B-", 2.67);
    letterGradeToPointValue.put("C+", 2.33);
    letterGradeToPointValue.put("C", 2.0);
    letterGradeToPointValue.put("C-", 1.67);
    letterGradeToPointValue.put("D+", 1.33);
    letterGradeToPointValue.put("D", 1.0);
    letterGradeToPointValue.put("F", 0.0);

    return letterGradeToPointValue;
  }

  @Test
  public void emptyGradeListReportsZeroGpaAndNoCourses() {
    assertTotalsOfList_Are("", "Courses: 0\nGPA: 0.00\n");
  }

  @Test
  public void singleCourseGradeReportsItself() {
    assertTotalsOfList_Are("CS 234        A ", "Courses: 1\nGPA: 4.00\n");
    assertTotalsOfList_Are("MAT    234    A-", "Courses: 1\nGPA: 3.67\n");
    assertTotalsOfList_Are("ENGR 121      B+", "Courses: 1\nGPA: 3.33\n");
    assertTotalsOfList_Are("CS 234         B", "Courses: 1\nGPA: 3.00\n");
    assertTotalsOfList_Are("CS 234        B-", "Courses: 1\nGPA: 2.67\n");
    assertTotalsOfList_Are("ENGR 011     C+ ", "Courses: 1\nGPA: 2.33\n");
    assertTotalsOfList_Are("ENGR 101      C ", "Courses: 1\nGPA: 2.00\n");
    assertTotalsOfList_Are("ENGR 101    C-\n", "Courses: 1\nGPA: 1.67\n");
    assertTotalsOfList_Are("ENGR 101      D+", "Courses: 1\nGPA: 1.33\n");
    assertTotalsOfList_Are("CS 122L        D", "Courses: 1\nGPA: 1.00\n");
    assertTotalsOfList_Are("CS 122L        F", "Courses: 1\nGPA: 0.00\n");
  }

  @Test
  public void multipleCoursesAreAllComputed() {
    assertTotalsOfList_Are(
            "CS 234        A \nMAT 111      B",
            "Courses: 2\nGPA: 3.50\n");
    assertTotalsOfList_Are(
            "CS 234        A \nMAT 111      B\nCS 122     B-\n",
            "Courses: 3\nGPA: 3.22\n");

  }

  @Test
  public void withdrawnCoursesDontCountTowardsCoursesTaken() {
    assertTotalsOfList_Are("CS 122L      W", "Courses: 0\nGPA: 0.00\n");
    assertTotalsOfList_Are(
            "CS 234        A \nMAT 111      B\nCS 122     W\n",
            "Courses: 2\nGPA: 3.50\n");
  }

  private void assertTotalsOfList_Are(String input, String output) {
    assertEquals(output, processGrades(new Scanner(input)));
  }
}
