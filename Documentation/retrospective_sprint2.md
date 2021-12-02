# RETROSPECTIVE (Team P12)
=====================================
- [process measures](#process-measures)
- [quality measures](#quality-measures)
- [general assessment](#assessment)
- [technical debts](#technical-debt)

## PROCESS MEASURES

### Macro statistics

- 3 stories committed vs 3 stories done
- 26 points committed vs 26 points done
- 9d 5h 15m (= 77h 15m + planning ) Nr of hours planned vs spent (as a team) 12d 2h 46m (= 98h 46m including planning )

Definition of Done:
- Unit Tests passing *(with a little tollerance)*
- Code review completed
- Code present on VCS
- End-to-End tests performed at least manually

### Detailed statistics
| Story      | #Task  | #Subtask | Points | Estimated time | Actual time    |
| ---------- | ------ | ------- | ------ | --------- | --------- |
| _SPG-40_   | 7      | \-      | \-     | \-        | \-        |
| \-         |_SPG-74_| 12      | \-     | 3d10m     | 4d3h50m   |
| \-         | \-     | SPG-75  | \-     | 30m       | 1h5m      |
| \-         | \-     | SPG-76  | \-     | 2h        | 3h40m     |
| \-         | \-     | SPG-77  | \-     | 1h30m     | 2h30m     |
| \-         | \-     | SPG-78  | \-     | 20m       | 3h        |
| \-         | \-     | SPG-79  | \-     | 40m       | 2h30m     |
| \-         | \-     | SPG-80  | \-     | 10m       | 30m       |
| \-         | \-     | SPG-82  | \-     | 1h        | 1h        |
| \-         | \-     | SPG-83  | \-     | 3h        | 1h30m     |
| \-         | \-     | SPG-84  | \-     | 1h30m     | 1h        |
| \-         | \-     | SPG-85  | \-     | 1d        | 1h30m     |
| \-         | \-     | SPG-107 | \-     | 5h        | 1d2h55m   |
| \-         | \-     | SPG-110 | \-     | 30m       | 30m       |
| \-         | SPG-113| \-      | \-     | 30m       | \-        |
| \-         | SPG-115| \-      | \-     | 1d        | \-        |
| \-         | SPG-99 | \-      | \-     | 3h        | 7h20m     |
| \-         | SPG-103| \-      | \-     | 40m       | 1h        |
| \-         | SPG-112| \-      | \-     | 7h        | 6h10m     |
| \-         | SPG-114| \-      | \-     | 15m       | 1h        |
| \-         | SPG-111| \-      | \-     | 5h        | 5h30m     |
| _SPG-100_  | SPG-101| \-      | \-     | 15m       | 12m       |
|_SPG-91_    | \-     | \-      | \-     | \-        | \-        |
| \-         | SPG-87 | \-      | \-     | 2h        | 55m       |
| \-         | SPG-88 | \-      | \-     | 1h30m     | 1h        |
| \-         | SPG-89 | \-      | \-     | 20m       | 20m       | 
| \-         | SPG-90 | \-      | \-     | 1h        | 1h10m     |
| \-         | SPG-92 | \-      | \-     | 15m       | 4m        |
| \-         | SPG-93 | \-      | \-     | 30m       | 30m       |
| \-         | \-     | \-      | \-     | \-        | \-        |
| SPG-7      | SPG-94 | \-      | 13     | 2h        | 1h15m     |
| SPG-8		 | 2      | \-      | 5      | \-        | \-        |
| \-         | SPG-96 | \-      | \-     | 3h30m     | 4h        |
| \-         | SPG-116| \-      | \-     | 20m       | 40m       |
| SPG-9		 | 2      | \-      | 8      | \-        | \-        |
| \-         | SPG-104| \-      | \-     | 3h        | 3h10m     |
| \-         | SPG-106| \-      | \-     | 4h        | 7h50m     | 

**Note:**  
- SPG-40: trasversal
- SPG-74: technical debt
- SPG-91: issues from stakeholders
- SPG-100: bugfix

**Total est:** 10d 4h 15m (= 84h 15m + planning ) 

**Total actual:** 12d 2h 46m (= 98h 46m including planning )

- Hours per task (average, standard deviation)
    - Average: 3h 47m
    - Standard deviation: 8h 2m
- Total task estimation error ratio: 
  - sum of total hours estimation / sum of total hours spent from previous table: 84.25/98.75 = 0.85
    
## QUALITY MEASURES

- Unit Testing:
    - Total hours estimated:
        - 14h 45m 
    - Total hours spent:
        - 17h20m
    - Nr of automated unit test cases
        -  BE: 100 unit cases
        -  FE: x unit cases
    - Coverage (if available):
        - BE: 70.1% (lines of code)
        - FE: 24.0% (lines of code)
- E2E testing:
    - Total hours estimated:
        - 3h
    - Total hours spent
        - 4h
- Code review
    - Total hours estimated:
        -  3h 20m
    - Total hours spent:
        - 3h 25m
        
- Technical Debt management:
    - Total hours estimated: 2d 3h 10m
    - Total hours spent: 2d 7h 25m
    - Frontend:   
      - Hours estimated for remediation by SonarQube: 1d 1h (111 code smells)
      - Hours estimated for remediation by SonarQube only for the selected and planned issues: 0h
      - Hours spent on remediation (by SonarQube only for the selected and planned issues): 0h
      - debt ratio (as reported by SonarQube under "Measures-Maintainability"): 0.7%
      - rating for each quality characteristic reported in SonarQube under "Measures":
        - Reliability: A
        - Security: A
        - Maintainability: A
    - Backend:
      - Hours estimated for remediation by SonarQube: 3d 7h (380 code smells)
      - Hours estimated for remediation by SonarQube only for the selected and planned issues: 0h
      - Hours spent on remediation (by SonarQube only for the selected and planned issues): 0h
      - debt ratio (as reported by SonarQube under "Measures-Maintainability"): 2.7%
      - rating for each quality characteristic reported in SonarQube under "Measures":
        - Reliability: C
        - Security: A
        - Maintainability: A

**Note:** 
    - We introduced the SonarQube estimation on both sides during this sprint, since the frontend wasn't subject to analysis. Our technical debt came mainly from personal      considerations. 
    - The testing time estimation is based on an euristics evaluation. Since we have some test operations to pay in the TD we estimate to spent 55% of totoal time in testing routines.

## ASSESSMENT

- What caused your errors in estimation (if any)?
    1. We underestimated the complexity of frontend automatic test
    2. Introducing roles brought more difficulties than we expected
    3. Managing Docker is complex 

- What lessons did you learn (both positive and negative) in this sprint?
    1. The new organization of branches and tasks ensures better coordination among us
    2. Overspecialized people slow down the work in their absence. 

- Which improvement goals set in the previous retrospective were you able to achieve?
    - merge different branches: late merge on sprint1 branch and conflict between fe and be
    - branch management: wrong branch handling related to different tasks, handling correctly tasks, subtasks and their bind.
    - adding spring security to the project: configurations and set up, form login hosted on server not reachable from fe
    - docker macos: fixing bugs on mac devices
    - overspecialization of tasks: defining too many levels and too complex relationships among tasks and subtasks made it difficult to keep precise track of time

- Which ones you were not able to achieve? Why?
   - lack of doc: we spent very little time on documentation, with the only exception of aligning the existing documents

- Improvement goals for the next sprint and how to achieve them (technical tasks, team coordination, etc.)
    1. We have still few comments in the code, both in backend and frontend.
        **solutions:**
        - Write more comments
    2. Reduce overspecialization
        **solutions:**
        - Try to create subgroup of at least 2 people for each technology 
    3. Little frontend coverage
        **solutions:**
        - Introduce more frontend testing, both Unit and E2E

- One thing you are proud of as a Team!!
    - We keep learning and introducing new technologies.
    - We have learnt how much technical debt can impact on a project 
