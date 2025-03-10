package student;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Planner implements the IPlanner interface.
 * This class keeps track of an initial full set of board games, as well as
 * a "current" filtered subset, allowing cumulative filters to be applied
 * until reset is called.
 */
public class Planner implements IPlanner {
    /**
     * The complete set of all available board games.
     */
    private final Set<BoardGame> allGames;

    /**
     * A list that holds the currently filtered set of board games.
     */
    private List<BoardGame> current;


    /**
     * Constructs a Planner with the specified set of BoardGame objects.
     * @param games the full set of board games to manage and filter.
     */
    public Planner(Set<BoardGame> games) {
        this.allGames = games;
        // Initially, no filters are applied, so current includes all games
        this.current = new ArrayList<>(allGames);
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
        return filter(filter, sortOn, true);
    }


    /**
     * Filters the current list of BoardGame objects using the given filter string,
     * then sorts by the specified column in either ascending or descending order,
     * depending on ascending.
     * @param filter The filter to apply to the board games.
     * @param sortOn The column to sort the results on.
     * @param ascending Whether to sort the results in ascending order or descending order.
     * @return a stream of the newly filtered and sorted board games.
     */
    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending) {
        // Start from current filtered list
        Stream<BoardGame> tempStream = current.stream();
        // Apply the text-based filter
        tempStream = Filter.applyFilter(tempStream, filter);
        // Then sort the resulting stream by the specified column and order
        tempStream = Sorting.sort(tempStream, sortOn, ascending);
//        // Convert the stream back to a list to store in 'current'
//        current = tempStream.toList();
        // Return a new stream based on the updated list
        return tempStream;
    }

    /**
     * Resets the list of BoardGame objects back to the full unfiltered set.
     */
    @Override
    public void reset() {
        current = new ArrayList<>(allGames);
    }
}
