# RETROSPECTIVE (Team P12)
=====================================
- [process measures](#process-measures)
- [quality measures](#quality-measures)
- [general assessment](#assessment)
- [technical debts](#technical-debt)

## PROCESS MEASURES

### Macro statistics

- 5 stories committed vs 5 stories done
- 20 points committed vs 20 points done
- 8d 3h 20m (= 67h 20m) Nr of hours planned vs spent (as a team) 11d 6h 50m (= 94h 50m including planning )

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

**Total est:** 8d 3h 20m (= 67h 20m)

**Total actual:** 11d 6h 50m (= 94h 50m including planning )

- Hours per task (average, standard deviation)
    - Average: 3h 36m
    - Standard deviation: 5h 9m
- Total task estimation error ratio:
  - sum of total hours estimation / sum of total hours spent from previous table: 67.33/90.33 = 0.74

## QUALITY MEASURES

- Unit Testing:
    - Total hours estimated:
        - 25% of total task time ( ~16h 50m)
    - Total hours spent:
        - 13h 30m
    - Nr of automate unit test cases
        -  BE: 142 unit test cases
        -  FE: 115 unit test cases
    - Coverage (if available):
        - BE: 74.0% (lines of code)
        - FE: 60.6% (lines of code)
- E2E testing:
    - Total hours estimated:
        - 20% of total task time excluding learning ( ~ 13h 30m) 
    - Total hours spent
        - 13h 20m
- Code review
    - Total hours estimated:
        -  5% of total task time ( ~ 3h 20m)
    - Total hours spent:
        - 4h

- Technical Debt management:
    - Total hours estimated: 3d 10m
    - Total hours spent: 4d 3h 50m
    - Frontend:
      - Hours estimated for remediation by SonarQube: 1d 6h (99 code smells)
      - Hours estimated for remediation by SonarQube only for the selected and planned issues: 0h
      - Hours spent on remediation (by SonarQube only for the selected and planned issues): 0h
      - debt ratio (as reported by SonarQube under "Measures-Maintainability"): 0.8%
      - rating for each quality characteristic reported in SonarQube under "Measures":
        - Reliability: A
        - Security: A
        - Maintainability: A
    - Backend:
      - Hours estimated for remediation by SonarQube: 4h 45m (81 code smells)
      - Hours estimated for remediation by SonarQube only for the selected and planned issues: 2h 45m
      - Hours spent on remediation (by SonarQube only for the selected and planned issues): 2h
      - debt ratio (as reported by SonarQube under "Measures-Maintainability"): 0.8%
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
      - Responsiveness: delay due to images rendering
      - Race condition: session race condition

- What lessons did you learn (both positive and negative) in this sprint?
    1. Downloading images reduces performance in the FE
    2. Better understanding of code smells and how to avoid them
    3. Delaying introduction of E2E tests produces an high load in TD

- Which improvement goals set in the previous retrospective were you able to achieve?
  1. Resolved hyper-specialization: now more people are available for each technology
  2. Increase FE coverage (now is 60.6%)

- Which ones you were not able to achieve? Why?
   1.  We have still few comments in the code, both in backend and frontend: we didn't have the chance to introduce them during the technical debt repaying phase.
   
- Improvement goals for the next sprint and how to achieve them (technical tasks, team coordination, etc.)
    1. Increase E2E testing 
    - **solution:** Write more tests    
    2. Decrease the loading time for products pages
    - **solution:** Storing the correspoing images locally instead of downloading them every time
        
- One thing you are proud of as a Team!!
    - The quality of our code is increasing
    - Moving two of us to full stack and spreading knowlegde allowed the sprint to be smoother
