------ JAR interface ------

--- Playing modes ---

A simple interface for the executable Sprouts application JAR file was
implemented with 3 different modes:

-n <gameType>
  Tells the application to start a new game of the given type.
  For example, '–n 4+F' starts a new normal game with 4 starting spots where
  the  application goes First (letter S is used for the Second player).

-j <gameId>
  Tells the application to join the game with a give ID.
  The example of this can be '-j 5Pr0u75r0K'.

-t <username>
  Tells the application to track the given username.
  Tracking of the user means that the application periodically checks if the
  user has not made any moves in any of the games that the application is a part
  of, or created any new games that the application could join.
  E.g. '-t BestSproutsPlayerEver' starts tracking the user whose username is
  BestSproutsPlayerEver.

--- Parameters ---

Several different parameters can be set with any of those modes:

-p <playerType>
  Chooses which player the application should play as.
  E.g. '–p smart' plays as the Smart Player, while '-p random' makes random
  moves instead.

-u <username>
  Sets the username of the application, which can be used as a –t parameter for
  another instance of the application.

-k
  Tells the application to keep playing after the game is over. Only works with
  –n (creates a new game of the same type after the end of the previous game)
  and –t (keeps tracking the username and waiting for a new games) playing
  modes.

-d
  Enables the verbose mode, i.e. more information about the strategy is
  outputted (e.g. which strategy is being used, how many moves were checked).

-r <interval>
  Sets the refresh interval (in seconds), i.e. how often the application checks
  if there are any new moves for the game that it has been playing or if the
  user that it has been tracking has not created any new games.
  
--- Usage Example ---

To play a game of Sprouts against the computer the user has to do the following:

  - Get the Sprouts application: e.g. download it
      from http://sprouts.laisva.lt/sprouts.jar

  - Run the application: for example, to play a 4-spots game where the opponent
      (i.e. the application) starts first, the user has to run it like this:
      java –jar sprouts.jar –n 4+F

  - Take a note of the ID of the created game, which will be displayed in the
      standard output of the application as:
      -- (GameInfo) Game started. Game id: 5Pr0u75r0K

  - Open the graphical interface client (http://sprouts.laisva.lt/), enter the
      game ID, and click the "Join Game" button.

  - Play the game! :)

By default, a random player will be used to generate the moves. To play against
the Smart Player, the –p smart parameter has to be added.
