
TEMPLATE FOR RETROSPECTIVE (Team 12)
=====================================
- [process measures](#process-measures)
- [quality measures](#quality-measures)
- [general assessment](#assessment)
- [technical debts](#technical-debt)

## PROCESS MEASURES

### Macro statistics

- 6 stories committed vs 4 stories done
- 25 points committed vs 18 points done
- 1w 2d 1h 10m (= 57h 10m) Nr of hours planned vs spent (as a team) 2w 2d 35m (= 96h 35m)

**Remember**  a story is done ONLY if it fits the Definition of Done:

- Unit Tests passing *(with a little tollerance)*
- Code review completed
- Code present on VCS
- End-to-End tests performed at least manually

### Detailed statistics

| Story       | # Tasks     | Points | Hours est. | Hours actual |
| ----------- | ----------- | ------ | ---------- | ------------ |
| _#0_        | 6           | -      | -          | 6h 5m        | 
| -           | SPG-41      | -      | 3h 10m     | 40m          |
| -           | SPG-44      | -      | 1d 4h      | 2d 5h 40m    |
| -           | SPG-48      | -      | 5h         | 2h           |
| -           | SPG-52      | -      | 2h         | 2h 20m       |
| -           | SPG-58      | -      | 2d         | 4h           |
| -           | SPG-67      | -      | 6h         | 50m          |
| -           | -           | -      | -          | -            |
| SPG-1       | 2           | 8      | -          | 4h 45m       |
| -           | SPG-53      | -      | 4h         | 2h 30m       |
| -           | SPG-41      | -      | 4h         | 2h 25m       |
| SPG-2       | 3           | 5      | -          | 50m          |
| -           | SPG-49      | -      | 1h         | 3h 30m       |
| -           | SPG-50      | -      | 2h         | 2h 35m       |
| -           | SPG-51      | -      | 4h         | 50m          |
| SPG-3       | 2           | 3      | -          | -            |
| -           | SPG-59      | -      | 3h         | 1h           |
| -           | SPG-60      | -      | 3h         | 1h           |
| SPG-4       | 4           | 2      | -          | 2h           |
| -           | SPG-55      | -      | 3h         | 1h           |
| -           | SPG-57      | -      | 4h         | 3h 45m       |
| -           | SPG-63      | -      | 2h         | 1h 45m       |
| -           | SPG-64      | -      | 1h         | 1h 20m       |
| SPG-5       | 2           | 2      | -          | 1h           |
| -           | SPG-65      | -      | 1h         | 45m          |
| -           | SPG-66      | -      | 3h         | 1h           |
| SPG-6       | SPG-50      | 5      | 2h         | 2h 55m       |


Total est: 9d 7h 10m
Total actual: 9d 0h 20m

> place technical tasks corresponding to story `#0` and leave out story points (not applicable in this case)

**Note: Hours actual in story entries (eg: _SPG-1_) means time spent in overall activities, it is not a total**

- Hours per task (average, standard deviation)
    - Average: 3 h 37m
    - Standard deviation: 4h 4m
- Total task estimation error ratio: sum of total hours estimation / sum of total hours spent from previous table
    - 79.16/72.33 = 1.09
    
## QUALITY MEASURES

- Unit Testing:
    - Total hours estimated:
        - 7h
    - Total hours spent:
        - 3h 15m
    - Nr of automated unit test cases
        -  14
    - Coverage (if available):
        - BE only: 41% (lines of code)
- E2E testing:
    - Total hours estimated:
        - 5h
    - Total hours spent
        - 7h
- Code review
    - Total hours estimated:
        -  5h
    - Total hours spent:
        - 4h 25m
        
- Technical Debt management:
    - Total hours estimated: 0h
    - Total hours spent: 0h 25m
    - Hours estimated for remediation by SonarQube: 30m
    - Hours estimated for remediation by SonarQube only for the selected and planned issues: 0h
    - Hours spent on remediation: 0h 25m
    - debt ratio (as reported by SonarQube under "Measures-Maintainability"): 1.8%
    - rating for each quality characteristic reported in SonarQube under "Measures":
      - Reliability: A
      - Security: D
      - Maintainability: A

**Note:** Technical debt has not been extensively managed in this sprint because of the high expected effort in the planning and starting phases.
The team did its best during the sprint to keep the code easily maintainable to repay quickly its debt.

## ASSESSMENT

- What caused your errors in estimation (if any)?
    1. merge different branches: late merge on sprint1 branch and conflict between fe and be
    2. branch management: wrong branch handling related to different tasks
    3. adding spring security to the project: configurations and set up, form login hosted on server not reachable from fe
    4. docker macos: fixing bugs on mac devices
    5. lack of doc: handle docs in a better way especially in the end of sprint

- What lessons did you learn (both positive and negative) in this sprint?
    1. it's better to plan and bind branches and tasks
    2. we don't have a stable fe-be linked version on sprint1 until the end of the sprint
    3. lack of documentation:
      - related to role based auth system
      - code comments are important to guarantee better readability
    4. migrate login credentials injection from be into fe, introduce some sec bugs fixes

- Which improvement goals set in the previous retrospective were you able to achieve?
  - introducing some docs, especially in the first design phase
  - better alignment between fe and be thanks to a better design communication
  - better handling of gitignore
  - faster set-up of maven and spring boot thanks to previous project learning phase

- Which ones you were not able to achieve? Why?
  - documentation must be improved for all part of the project:
    - not only for design but also for methods and new technologies imported into the project
  - testing must be extended to increase coverage and improve code quality

- Improvement goals for the next sprint and how to achieve them (technical tasks, team coordination, etc.)
    1. Write better code documentation to improve functionalities understanding and code readability.
       **solutions:**
       - Write more comments
       - introduce methods docs (input parameters, algo functionalities, output parameters)
    2. Try to have more readable code
       **solutions:**
        - Improve again external files to handle and collect all the possible api path and fe fetches
        - Spend more time on code review. The author can try to explain what he has done to 1 or 2 other members of the team
    3. Try to spend more time on task assignments
       **solutions:**
        - Create subgroups in order to have a better alignment between fe and be for a specific task
        - Define the tasks assignments before starting development
        - Have at least stable development branch

- One thing you are proud of as a Team!!
    - We did useful docs in the design phase
    - Everyone has learnt something new
    - Morale is high as always
