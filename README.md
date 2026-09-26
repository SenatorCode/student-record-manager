# Student Record Manager

A Java application that demonstrates core Object-Oriented Programming (OOP) concepts by managing student profiles, course registrations, and CGPA calculations.

---

## Student Information

| Field | Details |
| :--- | :--- |
| **Name** | Ibikunle Fawas Olamide |
| **Matric Number** | 250368 |
| **Department** | Computer Science |
| **Level** | 200 |

---

##  Design Principles Demonstrated

### Inheritance
* **Implementation:** `Student` extends `Person`.
* **Details:** The `Person` class encapsulates attributes common to any individual (such as `name`), while `Student` introduces specialized fields (`studentId`, `department`, `courses`). The `Student` constructor explicitly calls `super(name)` to initialize inherited fields, maintaining strict access boundaries since `Person` attributes are private.

### Encapsulation
* **Implementation:** Strict data hiding across all entities.
* **Details:** Every field in `Person`, `Student`, and `Course` is marked `private`. Data access and modifications are strictly brokered through public getters and setters. This architecture allows future validation rules to be injected directly into setters without mutating external code dependencies, preventing the application state from becoming corrupted.

### Dynamic Arrays (ArrayList)
* **Implementation:** Flexible course management via `ArrayList<Course>`.
* **Details:** Because the number of courses a student will register for is dynamic and unknown at compile time, an `ArrayList` is used instead of a fixed-size array. The `registerCourse()` method acts as a controlled wrapper around `ArrayList.add()`, ensuring all additions route through a single validation point.

### Exception Handling
The application avoids generic `catch (Exception e)` blocks, opting instead to catch explicit, anticipated exceptions to ensure runtime robustness without masking unrelated bugs:
* **`NumberFormatException`:** Caught specifically during credit unit parsing. This prevents system crashes if a user inputs non-numeric characters, serving a clean validation message instead.
* **`IllegalStateException`:** Thrown by `Student.calculateCGPA()` if a student attempts to compute their grade point average with fewer than 5 registered courses. This exception is caught independently by UI controllers (the CGPA button and the profile display) to handle the lifecycle event gracefully according to context.

---

## How to Run the Program

Follow these steps to compile and run the application locally from your terminal:

1. Navigate into the project root directory:
   ```bash
   cd student-record-manager
   ```

2. Move into the source files directory:
   ```bash
   cd src
   ```

3. Compile all Java source files and execute the main class:
   ```bash
   javac *.java && java StudentRecordManager
   ```

