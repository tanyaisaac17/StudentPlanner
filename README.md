# Student Study Planner

A desktop application built in **Java** that helps students organize their academic life — track subjects, log study sessions, manage to-do tasks, and stay on top of progress. All data is stored persistently in a **MySQL** database, with a custom-themed Swing GUI on top.

---

## Features

- **User accounts** — sign up and log in. Passwords are never stored in plain text (SHA-256 + per-user salt).
- **Subjects** — add the courses you're studying.
- **Study sessions** — log how many hours you spend on each subject; built-in live timer.
- **Tasks** — maintain a to-do list, mark items complete.
- **Persistent storage** — everything saved to MySQL; data survives app restarts.
- **Custom dark theme** — neon-accented UI built with Java Swing.
- **Multithreading** — the study timer runs in a background thread so the UI stays responsive.

---

## Tech Stack

| Layer            | Technology                                  |
| ---------------- | ------------------------------------------- |
| Language         | Java 17 / 21                                |
| GUI              | Java Swing (AWT, GridBagLayout, CardLayout) |
| Database         | MySQL 8                                     |
| DB Connectivity  | JDBC (`mysql-connector-j-9.6.0.jar`)        |
| Password Hashing | SHA-256 with per-user salt (`SecureRandom`) |
| Build            | `javac` + `compile_and_run.bat` / `.sh`     |

No external frameworks — everything uses Java's standard library.

---

## Project Structure

```
StudentPlanner/
├── src/com/studyplanner/
│   ├── main/         StudyPlannerApp.java        (entry point)
│   ├── db/           DBConnection.java           (JDBC singleton)
│   ├── model/        Record, Subject, Task,
│   │                 StudySession, User, Status,
│   │                 DatabaseEntity (annotation)
│   ├── service/      PlannerService (interface)
│   │                 PlannerServiceImpl
│   ├── util/         PasswordHasher, IconUtil
│   └── gui/          AppFrame, LoginPanelSimple,
│                     SignUpPanelSimple,
│                     DashboardHomePanel,
│                     SubjectsPanel, TasksPanel,
│                     StudySessionPanel, ...
├── schema_init.sql                (database setup)
├── compile_and_run.bat            (Windows build script)
├── compile_and_run.sh             (Linux/macOS build script)
├── mysql-connector-j-9.6.0.jar    (JDBC driver)
└── image.png                      (login background)
```

---

## Database Schema

Four tables in the `study_planner` database:

| Table      | Columns                                                |
| ---------- | ------------------------------------------------------ |
| `users`    | id, username (unique), password_hash, salt, created_at |
| `subjects` | id, name (unique), created_at                          |
| `sessions` | id, subject, hours, created_at                         |
| `tasks`    | id, task, completed, created_at                        |

Full schema: see [`schema_init.sql`](schema_init.sql).

---

## Setup & Run

### Prerequisites

- **Java JDK 17 or 21**
- **MySQL Server 8** running on `localhost:3306`

### Steps

1. **Clone the repo**

   ```bash
   git clone https://github.com/<your-username>/<repo-name>.git
   cd <repo-name>
   ```

2. **Initialize the database** (one-time)

   ```bash
   mysql -u root -p < schema_init.sql
   ```

3. **Set your MySQL password** as an environment variable

   ```bash
   # Windows (cmd)
   set STUDYPLANNER_DB_PASSWORD=your_password

   # Linux / macOS
   export STUDYPLANNER_DB_PASSWORD=your_password
   ```

4. **Compile and run**

   ```bash
   # Windows
   compile_and_run.bat

   # Linux / macOS
   ./compile_and_run.sh
   ```

   Or manually:

   ```bash
   javac -d . src/com/studyplanner/**/*.java
   java -cp "mysql-connector-j-9.6.0.jar:." com.studyplanner.main.StudyPlannerApp
   ```

5. **First time?** Click **Sign up** on the login screen, create an account, and you'll be auto-logged in to the dashboard.

---

## How Authentication Works

1. On signup, a 16-byte random salt is generated with `SecureRandom`.
2. The password is hashed with `SHA-256(salt || password)`.
3. Username, hash, and salt are stored in the `users` table — the plain password is never persisted.
4. On login, the same hash is recomputed using the stored salt and compared against the stored hash.
5. Successful signup auto-logs the user in; the dashboard shows their username in the title bar.

---

## Java Concepts Demonstrated

This project intentionally showcases a wide range of core Java topics:

- **Object-Oriented Programming** — encapsulation, inheritance, polymorphism
- **Abstract classes** — `Record` as the base for `Subject`, `Task`, `StudySession`
- **Interfaces** — `PlannerService` defining the service contract
- **Enums** — `Status` (ACTIVE, PENDING, COMPLETED, FAILED)
- **Custom annotations** — `@DatabaseEntity` with runtime retention
- **Collections** — `ArrayList`, `HashMap` for caching
- **Singleton design pattern** — `DBConnection` with double-checked locking
- **JDBC** — `PreparedStatement` for safe, parameterized SQL queries
- **Multithreading** — background thread for the study timer; `SwingWorker` in `ViewDataPanel`
- **Exception handling** — `try-catch-finally`, `try-with-resources`
- **Lambdas / functional interfaces** — for event handling and callbacks
- **Layout managers** — `BorderLayout`, `GridBagLayout`, `CardLayout`
- **Hashing & secure randomness** — `MessageDigest`, `SecureRandom`

---

## Known Limitations / Roadmap

- Subjects, sessions, and tasks are not yet scoped per user — all signed-in users currently see the same data.
- Password reset / change flows are not implemented.
- No automated tests — manual verification only.
- Hours-per-session is capped at 24 by a CHECK constraint.

---

## Author

Built as a Java course project. Contributions and feedback welcome.
