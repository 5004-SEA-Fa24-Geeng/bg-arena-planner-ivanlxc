# Board Game Arena Planner Design Document


This document is meant to provide a tool for you to demonstrate the design process. You need to work on this before you code, and after have a finished product. That way you can compare the changes, and changes in design are normal as you work through a project. It is contrary to popular belief, but we are not perfect our first attempt. We need to iterate on our designs to make them better. This document is a tool to help you do that.


## (INITIAL DESIGN): Class Diagram 

Place your class diagrams below. Make sure you check the file in the browser on github.com to make sure it is rendering correctly. If it is not, you will need to fix it. As a reminder, here is a link to tools that can help you create a class diagram: [Class Resources: Class Design Tools](https://github.com/CS5004-khoury-lionelle/Resources?tab=readme-ov-file#uml-design-tools)

### Provided Code

Provide a class diagram for the provided code as you read through it.  For the classes you are adding, you will create them as a separate diagram, so for now, you can just point towards the interfaces for the provided code diagram.

```mermaid
classDiagram
class BGArenaPlanner {
    - DEFAULT_COLLECTION : String
    - BGArenaPlanner()
    + main(String[] args) : void
}

class BoardGame {
    - name : String
    - id : int
    - minPlayers : int
    - maxPlayers : int
    - maxPlayTime : int
    - minPlayTime : int
    - difficulty : double
    - rank : int
    - averageRating : double
    - yearPublished : int
    + BoardGame(String name, int id, int minPlayers, int maxPlayers, int minPlayTime, int maxPlayTime, double difficulty, int rank, double averageRating, int yearPublished)
    + getName() : String
    + getId() : int
    + getMinPlayers() : int
    + getMaxPlayers() : int
    + getMaxPlayTime() : int
    + getMinPlayTime() : int
    + getDifficulty() : double
    + getRank() : int
    + getRating() : double
    + getYearPublished() : int
    + toStringWithInfo(col : GameData) : String
    + toString() : String
    + equals(obj : Object) : boolean
    + hashCode() : int
    + main(args : String[]) : void
}

class ConsoleApp {
    - IN : Scanner
    - DEFAULT_FILENAME : String
    - RND : Random
    - current : Scanner
    - gameList : IGameList
    - planner : IPlanner
    + ConsoleApp(gameList : IGameList, planner : IPlanner)
    + start() : void
    - randomNumber() : void
    - processHelp() : void
    - processFilter() : void
    - printFilterStream(games : Stream<BoardGame>, sortOn : GameData) : void
    - processListCommands() : void
    - printCurrentList() : void
    - nextCommand() : ConsoleText
    - remainder() : String
    - getInput(format : String, args : Object...) : String
    - printOutput(format : String, output : Object...) : void
}

class ConsoleText {
    <<enumeration>>
    + WELCOME
    + HELP
    + INVALID
    + GOODBYE
    + PROMPT
    + NO_FILTER
    + NO_GAMES_LIST
    + FILTERED_CLEAR
    + LIST_HELP
    + FILTER_HELP
    + INVALID_LIST
    + EASTER_EGG
    + CMD_EASTER_EGG
    + CMD_EXIT
    + CMD_HELP
    + CMD_QUESTION
    + CMD_FILTER
    + CMD_LIST
    + CMD_SHOW
    + CMD_ADD
    + CMD_REMOVE
    + CMD_CLEAR
    + CMD_SAVE
    + CMD_OPTION_ALL
    + CMD_SORT_OPTION
    + CMD_SORT_OPTION_DIRECTION_ASC
    + CMD_SORT_OPTION_DIRECTION_DESC
    + toString() : String
    + fromString(str : String) : ConsoleText
}

class GameData {
    <<enumeration>>
    + NAME
    + ID
    + RATING
    + DIFFICULTY
    + RANK
    + MIN_PLAYERS
    + MAX_PLAYERS
    + MIN_TIME
    + MAX_TIME
    + YEAR
    - columnName : String
    - GameData(columnName : String)
    + getColumnName() : String
    + fromColumnName(columnName : String) : GameData
    + fromString(name : String) : GameData
}

class GameList {
    + GameList()
    + getGameNames() : List<String>
    + clear() : void
    + count() : int
    + saveGame(filename : String) : void
    + addToList(str : String, filtered : Stream<BoardGame>) : void
    + removeFromList(str : String) : void
}

class GamesLoader {
    - DELIMITER : String
    - GamesLoader()
    + loadGamesFile(filename : String) : Set<BoardGame>
    - toBoardGame(line : String, columnMap : Map<GameData, Integer>) : BoardGame
    - processHeader(header : String) : Map<GameData, Integer>
}

class IPlanner {
    <<Interface>>
    + filter(filter : String) : Stream<BoardGame>
    + filter(filter : String, sortOn : GameData) : Stream<BoardGame>
    + filter(filter : String, sortOn : GameData, ascending : boolean) : Stream<BoardGame>
    + reset() : void
}

class Operations {
    <<enumeration>>
    + EQUALS
    + NOT_EQUALS
    + GREATER_THAN
    + LESS_THAN
    + GREATER_THAN_EQUALS
    + LESS_THAN_EQUALS
    + CONTAINS
    - operator : String
    - Operations(operator : String)
    + getOperator() : String
    + fromOperator(operator : String) : Operations
    + getOperatorFromStr(str : String) : Operations
}

class Planner {
    + Planner(games : Set<BoardGame>)
    + filter(filter : String) : Stream<BoardGame>
    + filter(filter : String, sortOn : GameData) : Stream<BoardGame>
    + filter(filter : String, sortOn : GameData, ascending : boolean) : Stream<BoardGame>
    + reset() : void
}

class IGameList {
    <<Interface>>
    + ADD_ALL : String
    + getGameNames() : List<String>
    + clear() : void
    + count() : int
    + saveGame(filename : String) : void
    + addToList(str : String, filtered : Stream<BoardGame>) : void
    + removeFromList(str : String) : void
}

GameList ..> IGameList : implements
Planner ..> IPlanner : implements

BGArenaPlanner --> IPlanner
BGArenaPlanner --> IGameList
BGArenaPlanner --> GamesLoader

ConsoleApp --> IGameList
ConsoleApp --> IPlanner

Planner --> BoardGame
GamesLoader --> BoardGame

```


### Your Plans/Design

Create a class diagram for the classes you plan to create. This is your initial design, and it is okay if it changes. Your starting points are the interfaces. 

```mermaid
classDiagram
class IGameList {
    <<Interface>>
    + ADD_ALL : String
    + getGameNames() : List<String>
    + clear() : void
    + count() : int
    + saveGame(filename : String) : void
    + addToList(str : String, filtered : Stream<BoardGame>) : void
    + removeFromList(str : String) : void
}

class IPlanner {
    <<Interface>>
    + filter(filter : String) : Stream<BoardGame>
    + filter(filter : String, sortOn : GameData) : Stream<BoardGame>
    + filter(filter : String, sortOn : GameData, ascending : boolean) : Stream<BoardGame>
    + reset() : void
}


class GameList {
    + GameList()
    + getGameNames() : List<String>
    + clear() : void
    + count() : int
    + saveGame(filename : String) : void
    + addToList(str : String, filtered : Stream<BoardGame>) : void
    + removeFromList(str : String) : void
}

class Planner {
    + Planner(games : Set<BoardGame>)
    + filter(filter : String) : Stream<BoardGame>
    + filter(filter : String, sortOn : GameData) : Stream<BoardGame>
    + filter(filter : String, sortOn : GameData, ascending : boolean) : Stream<BoardGame>
    + reset() : void
}

class Filter {
    + Filter()
    + parseFilter(filterStr : String) : Map<GameData, String>
}


class BoardGame {
    - name : String
    - id : int
    - minPlayers : int
    - maxPlayers : int
    - maxPlayTime : int
    - minPlayTime : int
    - difficulty : double
    - rank : int
    - averageRating : double
    - yearPublished : int
    + getName() : String
    + getId() : int
    + getMinPlayers() : int
    + getMaxPlayers() : int
    + getMaxPlayTime() : int
    + getMinPlayTime() : int
    + getDifficulty() : double
    + getRank() : int
    + getRating() : double
    + getYearPublished() : int
}

class GameData {
    <<enumeration>>
    + NAME
    + ID
    + RATING
    + DIFFICULTY
    + RANK
    + MIN_PLAYERS
    + MAX_PLAYERS
    + MIN_TIME
    + MAX_TIME
    + YEAR
}

class Operations {
    <<enumeration>>
    + EQUALS
    + NOT_EQUALS
    + GREATER_THAN
    + LESS_THAN
    + GREATER_THAN_EQUALS
    + LESS_THAN_EQUALS
    + CONTAINS
}


class ConsoleApp {
    - gameList : IGameList
    - planner : IPlanner
    + ConsoleApp(gameList : IGameList, planner : IPlanner)
    + start() : void
}

class BGArenaPlanner {
    + main(args : String[]) : void
}


GameList ..> IGameList : implements
Planner ..> IPlanner : implements

BGArenaPlanner --> IPlanner
BGArenaPlanner --> IGameList
BGArenaPlanner --> GamesLoader

ConsoleApp --> IGameList
ConsoleApp --> IPlanner

Planner --> BoardGame
Planner --> Filter
GamesLoader --> BoardGame


```



## (INITIAL DESIGN): Tests to Write - Brainstorm

Write a test (in english) that you can picture for the class diagram you have created. This is the brainstorming stage in the TDD process. 

> [!TIP]
> As a reminder, this is the TDD process we are following:
> 1. Figure out a number of tests by brainstorming (this step)
> 2. Write **one** test
> 3. Write **just enough** code to make that test pass
> 4. Refactor/update  as you go along
> 5. Repeat steps 2-4 until you have all the tests passing/fully built program

You should feel free to number your brainstorm. 

1. `BoardGame` attributes should be retrievable correctly.
2. `BoardGame` objects with the same ID should be equal.
3. Adding a game to `GameList` should increase its size.
4. Removing a game from `GameList` should decrease its size.
5. `GameList` should not allow duplicate games.
6. `Planner` should correctly filter by `minPlayers`.
7. `Planner` should correctly filter by `difficulty`.
8. `GamesLoader` should load games from CSV correctly.
9. Typing `"list"` in `ConsoleApp` should display all games.




## (FINAL DESIGN): Class Diagram

Go through your completed code, and update your class diagram to reflect the final design. Make sure you check the file in the browser on github.com to make sure it is rendering correctly. It is normal that the two diagrams don't match! Rarely (though possible) is your initial design perfect. 

For the final design, you just need to do a single diagram that includes both the original classes and the classes you added. 

> [!WARNING]
> If you resubmit your assignment for manual grading, this is a section that often needs updating. You should double check with every resubmit to make sure it is up to date.

```mermaid
classDiagram
class BGArenaPlanner {
    - DEFAULT_COLLECTION : String
    - BGArenaPlanner()
    + main(String[] args) : void
}

class BoardGame {
    - name : String
    - id : int
    - minPlayers : int
    - maxPlayers : int
    - maxPlayTime : int
    - minPlayTime : int
    - difficulty : double
    - rank : int
    - averageRating : double
    - yearPublished : int
    + BoardGame(String name, int id, int minPlayers, int maxPlayers, int minPlayTime, int maxPlayTime, double difficulty, int rank, double averageRating, int yearPublished)
    + getName() : String
    + getId() : int
    + getMinPlayers() : int
    + getMaxPlayers() : int
    + getMaxPlayTime() : int
    + getMinPlayTime() : int
    + getDifficulty() : double
    + getRank() : int
    + getRating() : double
    + getYearPublished() : int
    + toStringWithInfo(col : GameData) : String
    + toString() : String
    + equals(obj : Object) : boolean
    + hashCode() : int
    + main(args : String[]) : void
}

class ConsoleApp {
    - IN : Scanner
    - DEFAULT_FILENAME : String
    - RND : Random
    - current : Scanner
    - gameList : IGameList
    - planner : IPlanner
    + ConsoleApp(gameList : IGameList, planner : IPlanner)
    + start() : void
    - randomNumber() : void
    - processHelp() : void
    - processFilter() : void
    - printFilterStream(games : Stream<BoardGame>, sortOn : GameData) : void
    - processListCommands() : void
    - printCurrentList() : void
    - nextCommand() : ConsoleText
    - remainder() : String
    - getInput(format : String, args : Object...) : String
    - printOutput(format : String, output : Object...) : void
}

class ConsoleText {
    <<enumeration>>
    + WELCOME
    + HELP
    + INVALID
    + GOODBYE
    + PROMPT
    + NO_FILTER
    + NO_GAMES_LIST
    + FILTERED_CLEAR
    + LIST_HELP
    + FILTER_HELP
    + INVALID_LIST
    + EASTER_EGG
    + CMD_EASTER_EGG
    + CMD_EXIT
    + CMD_HELP
    + CMD_QUESTION
    + CMD_FILTER
    + CMD_LIST
    + CMD_SHOW
    + CMD_ADD
    + CMD_REMOVE
    + CMD_CLEAR
    + CMD_SAVE
    + CMD_OPTION_ALL
    + CMD_SORT_OPTION
    + CMD_SORT_OPTION_DIRECTION_ASC
    + CMD_SORT_OPTION_DIRECTION_DESC
    + toString() : String
    + fromString(str : String) : ConsoleText
}

class GameData {
    <<enumeration>>
    + NAME
    + ID
    + RATING
    + DIFFICULTY
    + RANK
    + MIN_PLAYERS
    + MAX_PLAYERS
    + MIN_TIME
    + MAX_TIME
    + YEAR
    - columnName : String
    - GameData(columnName : String)
    + getColumnName() : String
    + fromColumnName(columnName : String) : GameData
    + fromString(name : String) : GameData
}

class GameList {
    + GameList()
    + getGameNames() : List<String>
    + clear() : void
    + count() : int
    + saveGame(filename : String) : void
    + addToList(str : String, filtered : Stream<BoardGame>) : void
    + removeFromList(str : String) : void
}

class GamesLoader {
    - DELIMITER : String
    - GamesLoader()
    + loadGamesFile(filename : String) : Set<BoardGame>
    - toBoardGame(line : String, columnMap : Map<GameData, Integer>) : BoardGame
    - processHeader(header : String) : Map<GameData, Integer>
}

class IPlanner {
    <<Interface>>
    + filter(filter : String) : Stream<BoardGame>
    + filter(filter : String, sortOn : GameData) : Stream<BoardGame>
    + filter(filter : String, sortOn : GameData, ascending : boolean) : Stream<BoardGame>
    + reset() : void
}

class Operations {
    <<enumeration>>
    + EQUALS
    + NOT_EQUALS
    + GREATER_THAN
    + LESS_THAN
    + GREATER_THAN_EQUALS
    + LESS_THAN_EQUALS
    + CONTAINS
    - operator : String
    - Operations(operator : String)
    + getOperator() : String
    + fromOperator(operator : String) : Operations
    + getOperatorFromStr(str : String) : Operations
}

class Planner {
    + Planner(games : Set<BoardGame>)
    + filter(filter : String) : Stream<BoardGame>
    + filter(filter : String, sortOn : GameData) : Stream<BoardGame>
    + filter(filter : String, sortOn : GameData, ascending : boolean) : Stream<BoardGame>
    + reset() : void
}

class IGameList {
    <<Interface>>
    + ADD_ALL : String
    + getGameNames() : List<String>
    + clear() : void
    + count() : int
    + saveGame(filename : String) : void
    + addToList(str : String, filtered : Stream<BoardGame>) : void
    + removeFromList(str : String) : void
}

class Filter {
    <<Utility>>
    + applyFilter(games : Stream<BoardGame>, filterString : String) : Stream<BoardGame>
}

class Sorting {
    <<Utility>>
    + sort(games : Stream<BoardGame>, sortOn : GameData, ascending : boolean) : Stream<BoardGame>
}

GameList ..> IGameList : implements
Planner ..> IPlanner : implements
Filter --> BoardGame
Sorting --> BoardGame

BGArenaPlanner --> IPlanner
BGArenaPlanner --> IGameList
BGArenaPlanner --> GamesLoader

ConsoleApp --> IGameList
ConsoleApp --> IPlanner

Planner --> BoardGame
GamesLoader --> BoardGame
Planner --> Filter
Planner --> Sorting
Filter --> Operations
Sorting --> GameData

```


## (FINAL DESIGN): Reflection/Retrospective

> [!IMPORTANT]
> The value of reflective writing has been highly researched and documented within computer science, from learning to information to showing higher salaries in the workplace. For this next part, we encourage you to take time, and truly focus on your retrospective.

Take time to reflect on how your design has changed. Write in *prose* (i.e. do not bullet point your answers - it matters in how our brain processes the information). Make sure to include what were some major changes, and why you made them. What did you learn from this process? What would you do differently next time? What was the most challenging part of this process? For most students, it will be a paragraph or two. 

Comparing to my previous design, one of the major changes was introducing `Sorting` as a dedicated class rather than having sorting logic embedded in `Planner`. 
This change allows sorting logic to be modified or extended without affecting the filtering and planning mechanisms. 
Another significant adjustment was enhancing `Filter` to handle filtering comprehensively rather than just parsing conditions. 
The original design relied on `Planner` to apply filters, but by extracting `Filter` into a standalone class that directly processes BoardGame objects, 
the filtering logic became more reusable and easier to test.

This process taught me the importance of defining clear responsibilities early on. Initially, Planner was overloaded, making debugging harder. By distributing tasks properly, 
the new design follows Single Responsibility Principle (SRP) and ensures stable sorting and consistent filtering.  The most challenging part was ensuring sorting and filtering 
worked together correctly, particularly handling case-insensitive name comparisons while maintaining stable order. 

