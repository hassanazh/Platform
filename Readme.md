# A spring docker maven demo.

### Problem
A pawn can move on 10x10 chequerboard horizontally, vertically and diagonally by these rules:
1) 3 tiles moving North (N), West (W), South (S) and East (E)
2) 2 tiles moving NE, SE, SW and NW
3) Moves are only allowed if the ending tile exists on the board
4) Starting from initial position, the pawn can visit each cell only once

Write a program that finds at least one path for the pawn to visit all tiles on the board following the
above rules, starting from any tile.

### APIs created
1) To check valid moves [GET]: `http://localhost:8080/platform/valid-moves`
2) To Start a new game [POST]: `http://localhost:8080/platform/new-game/{rowId}/{columnId}`
   This request will return game id.
3) To apply next move [POST]: `http://localhost:8080/platform/{gameId}/next-move/{Direction}`
4) To generate unique path [POST]: `http://localhost:8080/platform/{gameId}/unique-path` 
5) To read path [GET]: `http://localhost:8080/platform/{gameId}/path`
6) To get current position [GET]: `http://localhost:8080/platform/{gameId}/current-position`
7) To finish game [POST]: `http://localhost:8080/platform/{gameId}/end-game`

### Solution description
This application can be run with docker and integrated with any running application within
same network. API calls will return the ids and path which can be further validated.

### Build application with Maven

 `mvn clean install` 
 
### To run from terminal
 
* Go to project directory and run `mvn spring-boot:run`

### Creating docker image and running the application

* To create docker image `docker build -t demo .`
* To run docker image `docker run -p 8080:8080 demo`

There is also a docker-compose file in the project which can be used to run the image with the following command
`docker-compose up`. 

To run the application in one command, use following.

`mvn clean install && docker build -t platform . && docker run -p 8080:8080 platform`