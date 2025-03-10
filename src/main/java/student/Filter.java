package student;

import java.util.stream.Stream;

/**
 * Utility class for filtering a Stream of objects based on a textual filter.
 * If the filter is empty or null, the original stream is returned without any changes.
 */
public final class Filter {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Filter() { }

    /**
     * Splits the given filterString by commas and applies each condition
     * sequentially to the given stream.
     * @param games        the original stream of BoardGame objects.
     * @param filterString a set of conditions separated by commas.
     * @return a new Stream<BoardGame> after applying all filtering conditions.
     */
    public static Stream<BoardGame> applyFilter(Stream<BoardGame> games, String filterString) {
        if (filterString == null || filterString.trim().isEmpty()) {
            return games;
        }

        // Split by comma and trim each condition (preserve internal spaces in values)
        String[] conditions = filterString.split(",");
        Stream<BoardGame> result = games;
        for (String condition : conditions) {
            condition = condition.trim();
            result = filterSingle(result, condition);
        }
        return result;
    }

    /**
     * Parses a single condition (e.g., "minPlayers>4") and applies it to the given stream of games.
     * Identifies the operator and the column. If the condition is invalid or operator is unrecognized,
     * the original stream is returned unchanged.
     *
     * @param games     the stream of BoardGame objects to filter.
     * @param condition a single condition string (e.g., "maxPlayers<6").
     * @return a new Stream<BoardGame> with the condition applied.
     */
    private static Stream<BoardGame> filterSingle(Stream<BoardGame> games, String condition) {
        // Identify the operator in the condition string
        Operations op = Operations.getOperatorFromStr(condition);
        if (op == null) {
            // If no valid operator is found, return the stream as is
            return games;
        }

        // Split the condition into left and right parts around the operator
        String[] parts = condition.split(op.getOperator());
        if (parts.length != 2) {
            // Invalid format, return the stream unchanged
            return games;
        }
        String left = parts[0].trim();
        String right = parts[1].trim();

        // Determine the column to filter on, ignoring 'ID' (not a valid filter criterion)
        GameData column;
        try {
            column = GameData.fromString(left);
            if (column == GameData.ID) {
                return games;
            }
        } catch (Exception e) {
            // If the column name is unrecognized, skip this filter
            return games;
        }

        // Apply the appropriate filtering based on the column's data type
        switch (column) {
            case NAME:
                return stringFilter(games, column, op, right);
            case RATING:
            case DIFFICULTY:
                // Double-based filter
                try {
                    double val = Double.parseDouble(right);
                    return doubleFilter(games, column, op, val);
                } catch (NumberFormatException e) {
                    return games;
                }
            case RANK:
            case MIN_PLAYERS:
            case MAX_PLAYERS:
            case MIN_TIME:
            case MAX_TIME:
            case YEAR:
                // Integer-based filter
                try {
                    int val = Integer.parseInt(right);
                    return intFilter(games, column, op, val);
                } catch (NumberFormatException e) {
                    return games;
                }
            default:
                return games;
        }
    }

    /**
     * Applies a string-based filter to the stream (e.g., filtering by name).
     * @param games   the stream of games.
     * @param column  the GameData enum specifying the column (expected to be NAME).
     * @param op      the parsed operator from Operations class.
     * @param value   the string value to match against.
     * @return a filtered stream based on the string matching rules.
     */
    private static Stream<BoardGame> stringFilter(Stream<BoardGame> games, GameData column,
                                                  Operations op, String value) {
        final String v = value.toLowerCase();
        switch (op) {
            case CONTAINS:
                // Name contains (case-insensitive)
                return games.filter(g -> getStringVal(g, column).toLowerCase().contains(v));
            case EQUALS:
                // Name equals (case-insensitive)
                return games.filter(g -> getStringVal(g, column).equalsIgnoreCase(v));
            case NOT_EQUALS:
                // Name not equals (case-insensitive)
                return games.filter(g -> !getStringVal(g, column).equalsIgnoreCase(v));
            default:
                return games;
        }
    }

    /**
     * Applies an integer-based filter to the stream.
     * @param games   the stream of games.
     * @param column  the GameData enum specifying which int field to compare.
     * @param op      the parsed operator from Operations class.
     * @param value   the integer value used in comparison.
     * @return a filtered stream based on the integer comparison rules.
     */
    private static Stream<BoardGame> intFilter(Stream<BoardGame> games, GameData column,
                                               Operations op, int value) {
        switch (op) {
            case GREATER_THAN:
                return games.filter(g -> getIntVal(g, column) > value);
            case GREATER_THAN_EQUALS:
                return games.filter(g -> getIntVal(g, column) >= value);
            case LESS_THAN:
                return games.filter(g -> getIntVal(g, column) < value);
            case LESS_THAN_EQUALS:
                return games.filter(g -> getIntVal(g, column) <= value);
            case EQUALS:
                return games.filter(g -> getIntVal(g, column) == value);
            case NOT_EQUALS:
                return games.filter(g -> getIntVal(g, column) != value);
            default:
                return games;
        }
    }

    /**
     * Applies a double-based filter to the stream (e.g., rating or difficulty).
     * @param games   the stream of games.
     * @param column  the GameData enum specifying which double field to compare.
     * @param op      the parsed operator from Operations class.
     * @param value   the double value used in comparison.
     * @return a filtered stream based on the double comparison rules.
     */
    private static Stream<BoardGame> doubleFilter(Stream<BoardGame> games, GameData column,
                                                  Operations op, double value) {
        switch (op) {
            case GREATER_THAN:
                return games.filter(g -> getDoubleVal(g, column) > value);
            case GREATER_THAN_EQUALS:
                return games.filter(g -> getDoubleVal(g, column) >= value);
            case LESS_THAN:
                return games.filter(g -> getDoubleVal(g, column) < value);
            case LESS_THAN_EQUALS:
                return games.filter(g -> getDoubleVal(g, column) <= value);
            case EQUALS:
                // Use a small tolerance for double equality comparisons
                return games.filter(g -> Math.abs(getDoubleVal(g, column) - value) < 1e-9);
            case NOT_EQUALS:
                return games.filter(g -> Math.abs(getDoubleVal(g, column) - value) > 1e-9);
            default:
                return games;
        }
    }


    /**
     * Helper to extract a string-based column value from a BoardGame (currently only name).
     * @param game the BoardGame object from which to retrieve the value.
     * @param col the GameData column to extract (should be NAME).
     * @return the string value of the specified column, or an empty
     * string if the column is not NAME.
     */
    private static String getStringVal(BoardGame game, GameData col) {
        // Only the NAME field is string-based in this context
        return game.getName();
    }

    /**
     * Helper to extract an integer-based column value from a BoardGame.
     * Potential int fields: rank, minPlayers, maxPlayers, minTime, maxTime, year.
     * @param game the BoardGame object from which to retrieve the value.
     * @param col the GameData column to extract.
     * @return the integer value of the specified column, or 0 if the column is not an integer field.
     */
    private static int getIntVal(BoardGame game, GameData col) {
        switch (col) {
            case RANK:
                return game.getRank();
            case MIN_PLAYERS:
                return game.getMinPlayers();
            case MAX_PLAYERS:
                return game.getMaxPlayers();
            case MIN_TIME:
                return game.getMinPlayTime();
            case MAX_TIME:
                return game.getMaxPlayTime();
            case YEAR:
                return game.getYearPublished();
            default:
                return 0;
        }
    }

    /**
     * Helper to extract a double-based column value from a BoardGame.
     * Potential double fields: rating, difficulty.
     * @param game the BoardGame object from which to retrieve the value.
     * @param col the GameData column to extract.
     * @return the double value of the specified column, or 0.0 if the column is not a double field.
     */
    private static double getDoubleVal(BoardGame game, GameData col) {
        switch (col) {
            case RATING:
                return game.getRating();
            case DIFFICULTY:
                return game.getDifficulty();
            default:
                return 0.0;
        }
    }
}
