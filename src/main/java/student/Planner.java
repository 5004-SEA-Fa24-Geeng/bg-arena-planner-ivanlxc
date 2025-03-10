package student;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Planner implements the IPlanner interface.
 * Manages a full collection of board games and a "current" subset that can be filtered cumulatively.
 */
public class Planner implements IPlanner {
    /** The complete set of all available board games. */
    private final Set<BoardGame> allGames;
    /** The currently filtered subset of board games (maintained between filters). */
    private Set<BoardGame> current;

    /**
     * Constructs a Planner with the specified set of BoardGame objects.
     * @param games the full set of board games to manage and filter.
     */
    public Planner(Set<BoardGame> games) {
        this.allGames = games;
        this.current = new LinkedHashSet<>();  // start with an empty current set (no filters applied yet)
    }

    @Override
    public Stream<BoardGame> filter(String filter) {
        return filter(filter, GameData.NAME, true);
    }

    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn) {
        return filter(filter, sortOn, true);
    }

    /**
     * Filters the current list of BoardGame objects using the given filter string,
     * then sorts by the specified column in either ascending or descending order.
     * The result becomes the new current list for subsequent filters (cumulative filtering).
     *
     * @param filter    the filter criteria to apply (e.g., "minPlayers>3,rating>=8").
     * @param sortOn    the column to sort the results on.
     * @param ascending whether to sort in ascending order (true) or descending order (false).
     * @return a stream of the newly filtered and sorted board games.
     */
    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending) {
        // Determine the base set for filtering: use current subset if available, otherwise all games
        Stream<BoardGame> baseStream = current.isEmpty() ? allGames.stream() : current.stream();

        // If no filter is provided, simply sort the base set and update current
        if (filter == null || filter.trim().isEmpty()) {
            Stream<BoardGame> sortedStream = Sorting.sort(baseStream, sortOn, ascending);
            current = sortedStream.collect(Collectors.toCollection(LinkedHashSet::new));
            return current.stream();
        }

        // Apply the filter conditions to the base set
        Stream<BoardGame> filteredStream = Filter.applyFilter(baseStream, filter);
        // Sort the filtered results and update the current set
        Stream<BoardGame> sortedStream = Sorting.sort(filteredStream, sortOn, ascending);
        current = sortedStream.collect(Collectors.toCollection(LinkedHashSet::new));
        return current.stream();
    }

    /**
     * Resets the current filtered set, reverting back to the full collection for the next filter operation.
     */
    @Override
    public void reset() {
        current = new LinkedHashSet<>();
    }
}
