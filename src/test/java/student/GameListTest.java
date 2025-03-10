package student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

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
    public void testInitiallyEmpty() {
        assertEquals(0, gameList.count(), "New GameList should be empty initially.");
        assertTrue(gameList.getGameNames().isEmpty(), "getGameNames should return an empty list at start.");
    }


    @Test
    void getGameNames() {
        gameList.addToList("all", games.stream());

        List<String> gameListNames = gameList.getGameNames();
        assertNotNull(gameListNames, "The list of game names is not null");

        List<String> expected = Arrays.asList("17 days", "Chess", "Go", "Go Fish", "golang", "GoRami", "Monopoly", "Tucano");
        assertEquals(8, expected.size());
        assertEquals(expected, gameListNames);
    }

    @Test
    void clear() {
        gameList.addToList("all", games.stream());
        assertEquals(8, gameList.count());
        gameList.clear();
        assertEquals(0, gameList.count());
    }

    @Test
    void count() {
        gameList.addToList("all", games.stream());
        assertEquals(8, gameList.count());
    }

    @Test
    void testSaveGame() {
        gameList.addToList("all", games.stream());
        // save to a test file
        String filename = "test_savelist.txt";
        gameList.saveGame(filename);

        try {
            // read contents from file
            List<String> lines = Files.readAllLines(Paths.get(filename));
            List<String> expected = Arrays.asList(
                    "17 days", "Chess", "Go", "Go Fish", "golang", "GoRami", "Monopoly", "Tucano"
            );
            assertEquals(expected, lines, "Saved file content should match the sorted game names");
        } catch (IOException e) {
            fail("IOException occurred while reading save file: " + e.getMessage());
        }
    }



    //tests on adding

    @Test
    void testAddSingleGameToListByName() {
        gameList.addToList("Chess", games.stream());
        assertEquals(1, gameList.count());
        assertEquals("Chess", gameList.getGameNames().get(0));
    }

    @Test
    void testAddInvalidSingleGameToListByName() {
        assertThrows(IllegalArgumentException.class, () -> {
            gameList.addToList("Pokemon", games.stream());
        });
        assertEquals(0, gameList.count());
    }


    @Test
    void testAddSingleGameToListByIndex() {
        gameList.addToList("2", games.stream());
        assertEquals(1, gameList.count());
        assertEquals("Chess", gameList.getGameNames().get(0));
    }


    @Test
    void testAddInvalidSingleGameToListByIndex() {
        assertThrows(IllegalArgumentException.class, () -> {
            gameList.addToList("10", games.stream());
        });
        assertEquals(0, gameList.count());
    }


    @Test
    void testAddRangeOfGamesToList() {
        gameList.addToList("2-4", games.stream());
        assertEquals(3, gameList.count());
        List<String> current = gameList.getGameNames();
        assertTrue(current.contains("Chess"));
        assertTrue(current.contains("Go"));
        assertTrue(current.contains("Go Fish"));
    }


    @Test
    void testAddInvalidRangeOfGamesToList() {
        assertThrows(IllegalArgumentException.class, () -> {
            gameList.addToList("5-2", games.stream());
        });
        assertEquals(0, gameList.count());
    }


    @Test
    void testAddAllGamesToList() {
        gameList.addToList("all", games.stream());
        assertEquals(8, gameList.count());
        List<String> current = gameList.getGameNames();
        assertTrue(current.containsAll(Arrays.asList("17 days", "Chess", "Go", "Go Fish",
                "golang", "GoRami", "Monopoly", "Tucano")));
    }



    //tests on removing


    @Test
    void testRemoveSingleGameFromListByName() {
        gameList.addToList("all", games.stream());
        assertEquals(8, gameList.count());

        gameList.removeFromList("Chess");
        assertEquals(7, gameList.count());
        assertFalse(gameList.getGameNames().contains("Chess"));
    }

    @Test
    void testRemoveInvalidSingleGameFromListByName() {
        gameList.addToList("all", games.stream());
        assertEquals(8, gameList.count());

        assertThrows(IllegalArgumentException.class, () -> {
            gameList.removeFromList("Pokemon");
        });
        assertEquals(8, gameList.count());
    }


    @Test
    void testRemoveSingleGameFromListByIndex() {
        gameList.addToList("all", games.stream());
        assertEquals(8, gameList.count());

        gameList.removeFromList("3");
        assertEquals(7, gameList.count());
        assertFalse(gameList.getGameNames().contains("Go"));
    }

    @Test
    void testRemoveInvalidSingleGameFromListByIndex() {
        gameList.addToList("all", games.stream());
        assertEquals(8, gameList.count());

        assertThrows(IllegalArgumentException.class, () -> {
            gameList.removeFromList("10");
        });
        assertEquals(8, gameList.count());
    }


    @Test
    void testRemoveRangeFromList() {
        gameList.addToList("all", games.stream());
        assertEquals(8, gameList.count());

        gameList.removeFromList("2-4");

        assertEquals(5, gameList.count());
        List<String> current = gameList.getGameNames();
        assertFalse(current.contains("Chess"));
        assertFalse(current.contains("Go"));
        assertFalse(current.contains("Go Fish"));
    }


    @Test
    void testRemoveInvalidRangeFromList() {
        gameList.addToList("all", games.stream());
        assertEquals(8, gameList.count());

        assertThrows(IllegalArgumentException.class, () -> {
            gameList.removeFromList("8-3");
        });
        assertEquals(8, gameList.count());
    }

    @Test
    void testRemoveAllFromList() {
        gameList.addToList("all", games.stream());
        assertEquals(8, gameList.count());

        gameList.removeFromList("all");
        assertEquals(0, gameList.count());
    }


}
