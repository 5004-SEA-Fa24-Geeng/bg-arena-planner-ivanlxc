package student;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;
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
        // Initialize original and current game collections
        this.originalGames = new LinkedHashSet<>(games);
        this.currentGames = new LinkedHashSet<>(games);
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
        // if filter is null or empty => no new filter, just sort
        if (filter == null || filter.trim().isEmpty()) {
            return sortAndReturn(sortOn, ascending);
        }

        // apply the filter to currentGames
        // (your Filter class uses applyFilter to handle multi-condition)
        Stream<BoardGame> filteredStream = Filter.applyFilter(currentGames.stream(), filter);

        // collect it back into currentGames for cumulative effect
        currentGames = filteredStream.collect(Collectors.toCollection(LinkedHashSet::new));

        // then sort, collect, and return
        return sortAndReturn(sortOn, ascending);
    }

    /**
     * Resets the list of BoardGame objects back to the full unfiltered set.
     */
    @Override
    public void reset() {
        // Restore the currentGames to the full original set (no filters)
        currentGames = new HashSet<>(originalGames);
    }




    private Stream<BoardGame> sortAndReturn(GameData sortOn, boolean ascending) {
        // If your Sorting class has a method sort(Stream<BoardGame>, GameData, boolean)
        // we can do:
        Stream<BoardGame> sorted = Sorting.sort(currentGames.stream(), sortOn, ascending);

        // collect back into currentGames if you want to preserve that order
        currentGames = sorted.collect(Collectors.toCollection(LinkedHashSet::new));

        // finally return them as a stream
        return currentGames.stream();
    }

}
