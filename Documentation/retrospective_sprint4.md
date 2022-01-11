# RETROSPECTIVE (Team P12)
=====================================
- [process measures](#process-measures)
- [quality measures](#quality-measures)
- [general assessment](#assessment)
- [technical debts](#technical-debt)

## PROCESS MEASURES

### Macro statistics

- 4 stories committed vs 4 stories done
- 23 points committed vs 23 points done
- 6d 2h 30m (= 50h 30m) Nr of hours planned vs spent (as a team) 12d 1h 02m (= 97h 02m including planning )

Definition of Done:
- Unit Tests passing
- Code review completed
- Code present on VCS
- End-to-End tests performed at least manually

### Detailed statistics
| Story      | #Task  | #Subtask | Points | Estimated time | Actual time    |
| ---------- | ------ | ------- | ------ | --------- | --------- |
| _SPG-40_   | 8      | \-      | \-     | \-        | \-        |
| \-         | SPG-122| 9      | \-      |2d 1h 50m  | 1d 2h 55m |
| \-         | \-     | SPG-123 | \-     |1h 30m     | 0m        |
| \-         | \-     | SPG-124 | \-     |20m        | 0m        |
| \-         | \-     | SPG-130 | \-     |30m        | 0m        |
| \-         | \-     | SPG-125 | \-     |2h         | 2h 45m    |
| \-         | \-     | SPG-128 | \-     |2h         | 1h        |
| \-         | \-     | SPG-129 | \-     |1h         | 1h        |
| \-         | \-     | SPG-141 | \-     |1h 30m     | 1h 50m    |
| \-         | \-     | SPG-145 | \-     |1d         | 2h 50m    |
| \-         | \-     | SPG-127 | \-     |1h         | 1h 30m    |
| \-         | SPG-113 | \-      | \-     | 30m       | 2h 20m    |
| \-         | SPG-115 | \-      | \-     | 1d        | 2d 1h 30m |
| \-         | SPG-133 | \-      | \-     | 2h        | 50m       |
| \-         | SPG-136 | \-      | \-     | 4h        | 0m        |
| \-         | SPG-148 | \-      | \-     | 0m        |  3d 1h    |
| \-         | SPG-149 | \-      | \-     |  2h       |15m        |
| \-         | SPG-150 | \-     | \-     |  4h       |  5h 05m    |
| _SPG-117_  |5        | \-      | \-     | 7h        | 7h 50m     |
| \-         |SPG-118  |         | \-     | 1h        | 1h 30m    |
| \-         |SPG-119  |         | \-     | 2h        | 1h 45m    |
| \-         |SPG-120  |         | \-     | 2h 30m    | 3h        |
| \-         |SPG-126  |         | \-     | 30m       | 50m       |
| \-         |SPG-143  |         | \-     | 1h        | 45m       |
| _SPG-100_  | SPG-121 | \-     | \-     | 30m       | 35m       |
| \-         | \       |        | \      |           |           |
| SPG-10     | 3       | \-     | 5      |           |           |
|            | SPG-131 | \-     | \-     |  3h       |  2h 30m   |
|            | SPG-132 | \-     | \-     |  4h       |  1h 30m   |
|            | SPG-137 | \-     | \-     |  3h  30m  |  1h 45m   |
| SPG-11     |     3    | \-     | 2      |           |           |
|            | SPG-138 | \-     | \-     |  1h      |  4h 30m      |
|            | SPG-151 | \-     | \-     |  30m      |  20m      |
|            | SPG-152 | \-     | \-     |  30m      |  1h 15m   |
| SPG-12     |     2    | \-     | 3      |           |           |
|            | SPG-134 | \-     | \-     |  1h       |  3h 15m   |
|            | SPG-139 | \-     | \-     |  2h 30m   |  1h 05m   |
| SPG-13     |     2    | \-     | 2      |           |           |
|            | SPG-140 | \-     | \-     |  1h 30m   |  2h 30m   |
|            | SPG-153 | \-     | \-     |  1h       |  1h       |
| SPG-17     |      2  | \      | 8      |           |           |
|            | SPG-135 | -      | \-     |  1h       |  1h 15m   |
|            | SPG-142 |\-      | \-     |  2h       |  3h 10m   |



**Note:**
- SPG-40: trasversal
- SPG-74: technical debt
- SPG-117: issues from stakeholders
- SPG-100: bugfix

**Total est:** 6d 2h 30m (= 50h 30m)

**Total actual:** 12d 1h 02m (= 97h 02m including planning )

- Hours per task (average, standard deviation)
    - Average: 3h 36m
    - Standard deviation: 5h 9m
- Total task estimation error ratio:
  - sum of total hours estimation / sum of total hours spent from previous table: 67.33/90.33 = 0.74

## QUALITY MEASURES

- Unit Testing:
    - Total hours estimated:
        - 20% of total task time ( ~10h 30m)
    - Total hours spent:
        - 10h 42m
    - Nr of automate unit test cases
        -  BE: 156 unit test cases
        -  FE: 160 unit test cases
    - Coverage (if available):
        - Line coverage
          - BE: 81.8%
          - FE: 87.8%
        - Condition coverage
          - BE: 43.4%
          - FE: 80.1%
- E2E testing:
    - Total hours estimated:
        - 30% of total task time excluding learning ( ~ 17h 30m)
    - Total hours spent
        - 19h 50m
- Code review
    - Total hours estimated:
        - 5% of total task time ( ~ 2h 50m)
    - Total hours spent:
        - 7h 45m

- Technical Debt management:
    - Total hours estimated: 3d 10m
    - Total hours spent: 4d 3h 50m
    - Frontend:
      - Hours estimated for remediation by SonarQube: 1d 2h (71 code smells)
      - Hours estimated for remediation by SonarQube only for the selected and planned issues: 1d 6h
      - Hours spent on remediation (by SonarQube only for the selected and planned issues): 3h 30m
      - debt ratio (as reported by SonarQube under "Measures-Maintainability"): 0.4%
      - rating for each quality characteristic reported in SonarQube under "Measures":
        - Reliability: A
        - Security: A
        - Maintainability: A
    - Backend:
      - Hours estimated for remediation by SonarQube: 4h 45m (109 code smells)
      - Hours estimated for remediation by SonarQube only for the selected and planned issues: 1d
      - Hours spent on remediation (by SonarQube only for the selected and planned issues): 1h
      - debt ratio (as reported by SonarQube under "Measures-Maintainability"): 0.9%
      - rating for each quality characteristic reported in SonarQube under "Measures":
        - Reliability: A
        - Security: A
        - Maintainability: A

**Note:**
- The testing time estimation is based on a percentage of total task time.
- The E2E tests carried out so far covered stories from past sprints.

## ASSESSMENT

- What caused your errors in estimation (if any)?
    1. Cypress:
      - Cypress best practices in react was not well explained
      - Cypress bug: detach elements in CICD
    2. Telegram Bot and Spring:
      - The use of telegram libraries in spring was not well documented
      - No user guide to implement system was available


- What lessons did you learn (both positive and negative) in this sprint?
    1. Docker was helpful to solve portability issues
    2. Working on multiple technologies could be time expensive


- Which improvement goals set in the previous retrospective were you able to achieve?
  1. Resolve responsiveness issues using local images storing
  2. Even if there was some bugs to resolve soon before the deadline the release phase was smooth.
  3. Resolve some code smells in order to maintain the ratio on average

- Which ones you were not able to achieve? Why?
   1.  Cypress: may not work for some use cases due to some behavior in the browser

- Improvement goals for the next sprint and how to achieve them (technical tasks, team coordination, etc.)
    1. Fix Cypress in CICD
    - **solution:** investigation of bugs   
    2. Write better documentation related to order and product cycles in the SPG system
    - **solution:** write documentation based on code

- One thing you are proud of as a Team!!
    - Great team management even if pandemic situation and vacation
    - Many different technologies are working together in good way, regardless of the difficulties encountered with Cypress
