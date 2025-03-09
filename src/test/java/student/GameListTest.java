package student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameListTest {

    public Set<BoardGame> games;
    public IGameList gameList;


    @BeforeEach
    void setUp() {
        games = new HashSet<>();
        games.add(new BoardGame("17 days", 6, 1, 8, 70, 70, 9.0, 600, 9.0, 2005));
        games.add(new BoardGame("Chess", 7, 2, 2, 10, 20, 10.0, 700, 10.0, 2006));
        games.add(new BoardGame("Go", 1, 2, 5, 30, 30, 8.0, 100, 7.5, 2000));
        games.add(new BoardGame("Go Fish", 2, 2, 10, 20, 120, 3.0, 200, 6.5, 2001));
        games.add(new BoardGame("golang", 4, 2, 7, 50, 55, 7.0, 400, 9.5, 2003));
        games.add(new BoardGame("GoRami", 3, 6, 6, 40, 42, 5.0, 300, 8.5, 2002));
        games.add(new BoardGame("Monopoly", 8, 6, 10, 20, 1000, 1.0, 800, 5.0, 2007));
        games.add(new BoardGame("Tucano", 5, 10, 20, 60, 90, 6.0, 500, 8.0, 2004));

        gameList = new GameList();

    }


    @Test
    void getGameNames() {
        gameList.addToList("all", games.stream());

        List<String> gameListNames = gameList.getGameNames();
        assertNotNull(gameListNames, "The list of game names is not null");

        List<String> expectedGameListNames = Arrays.asList("17 days", "Chess", "Go", "Go Fish", "golang", "GoRami", "Monopoly", "Tucano");
        assertEquals(8, expectedGameListNames.size());
        assertEquals(expectedGameListNames, gameListNames);
    }

    @Test
    void clear() {
        gameList.addToList("all", games.stream());
        assertEquals(9, gameList.count());
        gameList.clear();
        assertEquals(0, gameList.count());
    }








}
