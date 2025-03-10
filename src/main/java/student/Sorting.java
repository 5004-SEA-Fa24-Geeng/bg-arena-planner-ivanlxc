package student;

import java.util.Comparator;
import java.util.stream.Stream;


/**
 * A utility class for sorting a stream of BoardGame objects based on a specific column.
 * The class provides a static method which sorts the stream either in ascending
 *  or descending order, according to the specified GameData.
 */
public final class Sorting {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Sorting() { }

    /**
     * Sorts the given stream of BoardGame objects based on the specified
     * sortOn column in either ascending or descending order.
     *
     * @param games     the stream of BoardGame objects to sort.
     * @param sortOn    the GameData column to sort by.
     * @param ascending if true, the sorting is in ascending order; otherwise descending.
     * @return a Stream<BoardGame> sorted based on the specified column and order.
     */
    public static Stream<BoardGame> sort(Stream<BoardGame> games, GameData sortOn, boolean ascending) {
        // Get a comparator based on the desired column
        Comparator<BoardGame> comparator = getComparator(sortOn);
        // Reverse the comparator if descending order is requested
        if (!ascending) {
            comparator = comparator.reversed();
        }
        // Sort the stream using the comparator
        return games.sorted(comparator);
    }


    /**
     * Returns a Comparator for the specified GameData column.
     * If the column is not recognized, it defaults to comparing by name in a case-insensitive manner.
     * @param sortOn the GameData indicating which field to compare.
     * @return a Comparator for the specified field.
     */
    private static Comparator<BoardGame> getComparator(GameData sortOn) {
        switch (sortOn) {
            case NAME:
                // Compare board games by name ignoring case
                return Comparator.comparing(BoardGame::getName, String.CASE_INSENSITIVE_ORDER);
            case RATING:
                return Comparator.comparingDouble(BoardGame::getRating);
            case DIFFICULTY:
                return Comparator.comparingDouble(BoardGame::getDifficulty);
            case RANK:
                return Comparator.comparingInt(BoardGame::getRank);
            case MIN_PLAYERS:
                return Comparator.comparingInt(BoardGame::getMinPlayers);
            case MAX_PLAYERS:
                return Comparator.comparingInt(BoardGame::getMaxPlayers);
            case MIN_TIME:
                return Comparator.comparingInt(BoardGame::getMinPlayTime);
            case MAX_TIME:
                return Comparator.comparingInt(BoardGame::getMaxPlayTime);
            case YEAR:
                return Comparator.comparingInt(BoardGame::getYearPublished);
            default:
                // Fallback to name-based comparison
                return Comparator.comparing(BoardGame::getName, String.CASE_INSENSITIVE_ORDER);
        }
    }
}
