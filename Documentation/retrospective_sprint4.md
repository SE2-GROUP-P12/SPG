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
- 6d 5h 30m (= 53h 30m) Nr of hours planned vs spent (as a team) 12d 3h 02m (= 99h 02m including planning )

Definition of Done:
- Unit Tests passing
- Code review completed
- Code present on VCS
- End-to-End tests performed at least manually

### Detailed statistics
| Story      | #Task   | #Subtask | Points | Estimated time | Actual time    |
| ---------- | ------  | -------  | ------ | --------- | --------- |
| _SPG-40_   | \-      | \-       | \-     | \-        | \-        |
| \-         | SPG-180 | 6        | \-     | 3d5h      | 5d7h10m   |
| \-         | \-      | SPG-183  | \-     | 2d        | 2d6h30m   |
| \-         | \-      | SPG-184  | \-     | 1h        | 2h30m     |
| \-         | \-      | SPG-186  | \-     | 2h        | 3h30m     |
| \-         | \-      | SPG-188  | \-     | 4h        | 1d2h50m   |
| \-         | \-      | SPG-194  | \-     | 5h        | 6h50m     |
| \-         | \-      | SPG-196  | \-     | 1h        | 1h        |
| \-         | SPG-149 | \-       | \-     | 2h        | 2h15m     |
| \-         | SPG-191 | \-       | \-     | 1h30m     | 0m        |
| _SPG-165_  | 2       | \-	      | \-     | \-        | \-        |
| \-         | SPG-166 | \-       | \-     | 20m       | 20m       |
| \-         | SPG-167 | \-       | \-     | 20m       | 20m       |
| _SPG-100_  | 3       | \-       | \-     | \-        | \-        |
| \-         | SPG-168 | \-       | \-     | 1h        | 1h        |
| \-         | SPG-169 | \-       | \-     | 30m       | 20m       |
| \-         | SPG-170 | \-       | \-     | 2h        | 2h        |
| \-         | SPG-192 | \-       | \-     | 5m        | 5m        |
| _SPG-187_  | \-      | \-       | \-     | \-        | 2d6h      |      
| \-         | \-      | \-       | \-     | \-        | \-        |
| SPG-154    | 2       | \-	      | 8      | \-        | \-        |
| \-         | SPG-179 | \-       | \-     | 15m       | 12m       |
| \-         | SPG-173 | \-       | \-     | 4h30m     | 6h20m     |
| SPG-155    | 3       | \-	      | 5      | \-        | \-        |
| \-         | SPG-172 | \-       | \-     | 2h30m     | 3h        |
| \-         | SPG-176 | \-       | \-     | 3h        | 2h        |
| \-         | SPG-190 | \-       | \-     | 1h30m     | 1h30m     |
| SPG-156    | 1       | \-	      | 5      | \-        | \-        |
| \-         | SPG-189 | \-       | \-     | 1h30m     | 6h        |
| \-         | SPG-178 | \-       | \-     | 2h        | 2h30m     |
| SPG-157    | 1       | \-	      | 5      | \-        | \-        |
| \-         | SPG-177 | \-       | \-     | 1h30m     | 2h        |



**Note:**
- SPG-187: Planning
- SPG-165: Github issues
- SPG-100: Bugfix
- SPG-40: Trasversal

**Total est:** 6d 5h 30m (= 53h 30m)

**Total actual:** 12d 3h 02m (= 99h 02m including planning )

- Hours per task (average, standard deviation)
    - Average: 5h 49m
    - Standard deviation: 11h 15m
- Total task estimation error ratio:
  - sum of total hours estimation / sum of total hours spent from previous table: 53.5/99.02 = 0.54

## QUALITY MEASURES

- Unit Testing:
    - Total hours estimated:
        - 20% of total task time ( ~11h)
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
        - 30% of total task time excluding learning ( ~ 18h)
    - Total hours spent
        - 19h 50m
- Code review
    - Total hours estimated:
        - 5% of total task time ( ~ 3h 30m)
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
