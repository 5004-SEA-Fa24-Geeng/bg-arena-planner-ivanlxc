package student;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Planner implements the IPlanner interface.
 * This class keeps track of an initial full set of board games, as well as
 * a "current" filtered subset, allowing cumulative filters to be applied
 * until reset is called.
 */
public class Planner implements IPlanner {
    /**
     * The original board games.
     */
    private final Set<BoardGame> originalGames;

    /**
     * The current filtered board games.
     */
    private Set<BoardGame> currentGames;


    /**
     * Constructs a Planner with the specified set of BoardGame objects.
     * @param games the full set of board games to manage and filter.
     */
    public Planner(Set<BoardGame> games) {
        // We'll store all games in originalGames
        this.originalGames = new LinkedHashSet<>(games);
    }


    /**
     * Filters the current list of BoardGame objects using the given filter string,
     * then sorts by GameData#NAME in ascending order.
     * The result becomes the new current list for subsequent filters.
     * @param filter The filter to apply to the board games.
     * @return a stream of the newly filtered and sorted board games.
     */
    @Override
    public Stream<BoardGame> filter(String filter) {
        // Default sort by name (ascending) if no sort criteria provided
        return filter(filter, GameData.NAME, true);
    }


    /**
     * Filters the current list of BoardGame objects using the given filter string,
     * then sorts by the specified column in ascending order.
     * The result becomes the new current list for subsequent filters.
     * @param filter The filter to apply to the board games.
     * @param sortOn The column to sort the results on.
     * @return a stream of the newly filtered and sorted board games.
     */
    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn) {
        // Default to ascending order if sort order not provided
        return filter(filter, sortOn, true);
    }




    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending) {
        if (filter == null || filter.trim().isEmpty()) {
            // no filter -> just sort entire originalGames
            return sortAndReturn(originalGames.stream(), sortOn, ascending);
        }

        // 1) Start from ALL games, ignoring previous filters
        Stream<BoardGame> filteredStream = Filter.applyFilter(originalGames.stream(), filter);

        // 2) Sort the filtered result
        return sortAndReturn(filteredStream, sortOn, ascending);
    }

    /**
     * Resets the list of BoardGame objects back to the full unfiltered set.
     */
    @Override
    public void reset() { }



    private Stream<BoardGame> sortAndReturn(Stream<BoardGame> input, GameData col, boolean ascending) {
        // Use your Sorting class
        Stream<BoardGame> sorted = Sorting.sort(input, col, ascending);
        // Just return sorted (no need to store anywhere if non-progressive)
        return sorted;
    }
}
