# Coding challenge - Issue tracker and sprint planner  

### How to run
Under root project directory, run command :
```bash
mvn spring-boot:run
```

### How to access endpoints
Use [Postman](https://www.postman.com/) collection placed in /postman/issue-tracker.postman_collection.json

Collection contains all available endpoints with example request data.

### List available of endpoints
Base path - http://localhost:8080/issue-tracker
#### Developer

* List of developers - GET 'http://localhost:8080/issue-tracker/developer'
* Create developer - POST 'http://localhost:8080/issue-tracker/developer'  with 'Content-Type: text/plain' and plain text as developer name in request body
* Update developer - PUT 'http://localhost:8080/issue-tracker/developer/{developerId}' with 'Content-Type: text/plain' and plain text as new developer name in request body 
	
	where : `develperId` is id of developer to be updated
* Delete developer - DELETE 'http://localhost:8080/issue-tracker/developer/{developerId}'

	where : `develperId` is id of developer to be deleted

#### Issue
* List of Issues - GET 'http://localhost:8080/issue-tracker/issue'
* Delete Issue - DELETE 'http://localhost:8080/issue-tracker/issue/{issueId}' 
where `issueId` is id of issue to be deleted
* Assign developer - PUT 'http://localhost:8080/issue-tracker/issue/{issueId}/{developerId}' 
 	
 	where : 
	
	`issueId` is id of issue to be deleted
	
	`developerId` is id of developer to be deleted

	##### Story
* Create story - POST 'http://localhost:8080/issue-tracker/issue/story'  with 'Content-Type: application/json' and Json string as request body (tile and description are mandatory, default value for status : NEW; storyPoints : 0):
```json
    "title":"String",
	"description":"String",
    "status":"String one of [NEW,ESTIMATED,COMPLETED]",
    "storyPoints":"number from 1 to 10 "
```
* Get story -   GET 'http://localhost:8080/issue-tracker/issue/story/{storyId}' 
	
	where `storyId` is id of story to be received

* Update story - PUT 'http://localhost:8080/issue-tracker/issue/story/{storyId}'  with 'Content-Type: application/json' and Json string as request body (tile and description are mandatory, default value for status : NEW; storyPoints : 0):
```json
    "title":"String",
	"description":"String",
    "status":"String one of [NEW,ESTIMATED,COMPLETED]",
    "storyPoints":"number from 1 to 10 "
```

	where `storyId` is id of story to be updated

	##### Bug
* Create bug - POST 'http://localhost:8080/issue-tracker/issue/bug'  with 'Content-Type: application/json' and Json string as request body (tile and description are mandatory, default value for status : NEW; priority : MINOR):
```json
    "title":"String",
	"description":"String",
    "status":"String one of [NEW,VERIFIED,RESOLVED]",
    "priority":"String one of [CRITICAL,MAJOR,MINOR] "
```
* Get bug -   GET 'http://localhost:8080/issue-tracker/issue/bug/{bugId}' 

	where `bugId` is id of bug to be received
* Update story - PUT 'http://localhost:8080/issue-tracker/issue/story/{bugId}'  with 'Content-Type: application/json' and Json string as request body (tile and description are mandatory, default value for status : NEW; priority : MINOR):
```json
    "title":"String",
	"description":"String",
    "status":"String one of [NEW,VERIFIED,RESOLVED]",
    "priority":"String one of [CRITICAL,MAJOR,MINOR] "
```
	
	where `bugId` is id of bug to be updated
	
#### Sprint Planner

Endpoint GET 'http://localhost:8080/issue-tracker/plan?numberOfDevelopers={number}' where query parameter `numberOfDevelopers` number of developers committing to planned sprints.
Response json :
```json 
[
{
	"index": number,
	"stories": [...]
	"totalStoryPoints": number
},
...
]
``` 
where :
	
	index : number representing sequential order of sprints starting from 0
	stories : list of stories planned for sprint
	totalStoryPoints: number representing sum of story point from all stories added into sprint 
	 
### Technical decisions and debts

#### Planning algorithm

Simple algorithm (and at least well known to me) of [Minimum Coin Change Problem]{https://www.enjoyalgorithms.com/blog/minimum-coin-change} and repeating it while all stores are assigned to the sprint.

Algorithm isn't prefect as in some cases it provides sprints with stories point with less than maximum story points in it, while there is possible combination of full sprint.

#### Missing parts

As was lack of (free) time for coding, I didn't manage to write JUnit Test to cover all functionality. But at the same time, there good portion of JUnit Test to evaluate my coding style.

For the same reason, there is lack of input validation, which can cause errors like : max story point for developer.

And finally it was my first application with RestEasy and I didn't analyze how to provide meaningful messages in response (after I found out that exception message is not provided in response). I need to read reference manual to solve and issue.     


