package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.model.country.Country;
import seedu.address.ui.WidgetViewOption;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    /** The widget display should be reset to normal */
    private final boolean resetWidget;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    /** Indicates what kind of display to show on the widget view box. */
    private final WidgetViewOption widgetViewOption;

    /**
     * Constructs a {@code CommandResult} with the specified fields
     */
    public CommandResult(String feedbackToUser, boolean resetWidget, boolean showHelp, boolean exit,
            WidgetViewOption widgetViewOption) {
        requireAllNonNull(feedbackToUser, showHelp, exit, widgetViewOption);
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.resetWidget = resetWidget;
        this.showHelp = showHelp;
        this.exit = exit;
        this.widgetViewOption = widgetViewOption;
    }

    /**
     * Constructs a {@code CommandResult} with the specified fields and {@code widgetViewOption}
     * is set to its default value.
     */
    public CommandResult(String feedbackToUser, boolean resetWidget, boolean showHelp, boolean exit) {
        this(feedbackToUser, resetWidget, showHelp, exit, WidgetViewOption.generateNullWidgetOption());
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false, false);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isResetWidget() {
        return resetWidget;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    /**
     * Returns the string representation of the widget view.
     *
     * @return The string representation of the widget view.
     */
    public String getWidgetViewOptionAsString() {
        return widgetViewOption.toString();
    }

    /**
     * Returns true if the UI should display the client view.
     *
     * @return True if the UI should display the client view.
     */
    public boolean shouldDisplayClient() {
        return widgetViewOption.isClient();
    }

    /**
     * Returns true if the UI should display the country notes view.
     *
     * @return True if the UI should display the country notes view.
     */
    public boolean shouldDisplayCountryNote() {
        return widgetViewOption.isCountryNote();
    }

    /**
     * Gets the country that is associated with the widget view.
     *
     * @return The country that is associated with the widget view.
     */
    public Country getCountry() {
        return widgetViewOption.getCountry();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit);
    }

}
