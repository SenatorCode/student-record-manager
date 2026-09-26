## Student Information:
Name: Ibikunle Fawas Olamide
Matric Number: 250368
Department: Computer Science
Level: 200


## Design Principles Demonstrated

### Inheritance
`Student extends Person`. `Person` holds what's common to any person (a `name`);
`Student` adds what's specific to being a student (`studentId`, `department`,
`courses`). The `Student` constructor calls `super(name)` to initialize the
inherited `name` field, since `Person`'s fields are private and can only be
set through `Person`'s own constructor.

### Encapsulation
Every field in `Person`, `Student`, and `Course` is `private`, accessed only
through public getters/setters. This means validation rules can be added to a
setter later without touching any other class, and no external code can put an
object into an invalid state by reaching into its fields directly.

### ArrayList
`Student` stores its registered courses in an `ArrayList<Course>` rather than
a fixed-size array, since the number of courses a student registers is not
known in advance. `registerCourse()` wraps `ArrayList.add()` so all additions
go through one controlled entry point.

### Exception Handling
- `NumberFormatException` is caught specifically when parsing the credit unit
  field, so a non-numeric entry produces a clear message instead of a crash.
- `IllegalStateException` is thrown by `Student.calculateCGPA()` when fewer
  than 5 courses are registered, and caught separately wherever CGPA is
  calculated (the CGPA button and the profile display), each presenting the
  failure appropriately for its context.
- Both are caught by their specific type, not a generic `catch (Exception e)`,
  so only the failures the program actually anticipates are handled, any
  other bug still surfaces normally.