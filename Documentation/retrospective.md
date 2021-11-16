
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
| _#0_        | 5           | -      | 4d 1h 30m  | 4d 1h 40m    |
| -           | SPG-41      | -      | 1d 3h      | 1d 4h 5m     |
| -           | S2112OQM-15 | -      | 0d 6h      | 0d 5h 15m    |
| -           | S2112OQM-14 | -      | 0d 3h      | 0d 1h 15m    |
| -           | S2112OQM-28 | -      | 0d 3h      | 0d 5h 20m    |
| -           | S2112OQM-27 | -      | 1d 1h      | 0d 7h        |
| -           | S2112OQM-26 | -      | 0d 1h 30m  | 0d 2h 45m    |
| -           | -           | -      | -          | -            |
| S2112OQM-3  | S2112OQM-17 | 5      | 0d 1h      | 0d 1h        |
| S2112OQM-4  | S2112OQM-23 | 8      | 0d 5h      | 0d 3h        |
| S2112OQM-8  | S2112OQM-22 | 2      | 0d 1h      | 0h 30m       |
| S2112OQM-10 | S2112OQM-25 | 3      | 0d 4h      | 0d 2h 15m    |
| S2112OQM-11 | S2112OQM-19 | 8      | 0d 6h      | 0d 4h 30m    |
| S2112OQM-6  | S2112OQM-29 | 2      | 0d 2h      | 0d 2h        |

Total est: 6d 4h 30m
Total actual: 5d 6h 55m

> place technical tasks corresponding to story `#0` and leave out story points (not applicable in this case)

**Note: story _#0_ contains also the transversal tasks**

- Hours per task (average, standard deviation)
    - Average: 3 h 55m
    - Standard deviation: 3h 7m
- Total task estimation error ratio: sum of total hours estimation / sum of total hours spent from previous table
    - 52.5/46.91= 1.12


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

**ignore for this spring**
- Technical Debt management:
    - Total hours estimated: 1d
    - Total hours spent:
    - Hours estimated for remediation by SonarQube
    - Hours estimated for remediation by SonarQube only for the selected and planned issues
    - Hours spent on remediation
    - debt ratio (as reported by SonarQube under "Measures-Maintainability")
    - rating for each quality characteristic reported in SonarQube under "Measures" (namely reliability, security, maintainability )



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
  - testing must be extend to increase coverage and improve code quality

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
    - We did great docs in the design phase
    - Everyone has learnt something new
    - Morale is high as always
