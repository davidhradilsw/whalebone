# QA Automation Case Study

## Tech stack
- Java 17
- TestNG
- RestAssured
- Playwright (Java)
- Maven

## Covered scenarios
### API
- Verify total number of teams (32)
- Verify oldest team is Montreal Canadiens
- Verify cities with multiple teams
- Verify Metropolitan division teams

### UI (UI Testing Playground)
- Scrape roster of oldest team and verify more Canadian than USA players
- Sample App – login/logout scenarios
- Load Delay – page loads in reasonable time
- Progress Bar – stop at ~75% as per scenario

## How to run tests
mvn test
