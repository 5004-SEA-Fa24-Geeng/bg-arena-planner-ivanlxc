package student;

import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Utility class for sorting a stream of BoardGame objects based on a specific column.
 * Provides a method to sort in ascending or descending order according to the specified GameData.
 */
public final class Sorting {
    /** Private constructor to prevent instantiation. */
    private Sorting() { }

    /**
     * Sorts the given stream of BoardGame objects based on the specified sortOn column,
     * in either ascending or descending order. Ties are broken by name to ensure stable ordering.
     *
     * @param games     the stream of BoardGame objects to sort.
     * @param sortOn    the GameData column to sort by.
     * @param ascending if true, sort in ascending order; if false, sort in descending order.
     * @return a Stream<BoardGame> sorted by the specified column (and name for ties).
     */
    public static Stream<BoardGame> sort(Stream<BoardGame> games, GameData sortOn, boolean ascending) {
        // Base comparator for the primary sort key
        Comparator<BoardGame> primaryComparator = getComparator(sortOn);
        // Secondary comparator for tie-breaking by name (case-insensitive)
        Comparator<BoardGame> nameComparator = Comparator.comparing(bg -> bg.getName().toLowerCase());

        // Combine comparators: apply primary, then name as secondary for stable ordering
        Comparator<BoardGame> finalComparator = ascending
                ? primaryComparator.thenComparing(nameComparator)
                : primaryComparator.reversed().thenComparing(nameComparator);

        return games.sorted(finalComparator);
    }

    /**
     * Returns a Comparator for the specified GameData column.
     * If the column is not recognized, it defaults to comparing by name (case-insensitive).
     *
     * @param sortOn the GameData indicating which field to compare.
     * @return a Comparator<BoardGame> for the specified field.
     */
    private static Comparator<BoardGame> getComparator(GameData sortOn) {
        switch (sortOn) {
            case NAME:
                // Compare by name, ignoring case
                return Comparator.comparing(bg -> bg.getName().toLowerCase());
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
                // Fallback to name-based comparison if sortOn is unrecognized
                return Comparator.comparing(bg -> bg.getName().toLowerCase());
        }
    }
}
