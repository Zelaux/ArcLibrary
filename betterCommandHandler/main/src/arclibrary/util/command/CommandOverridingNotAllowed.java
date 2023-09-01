package arclibrary.util.command;

public class CommandOverridingNotAllowed extends RuntimeException {
    public CommandOverridingNotAllowed(BetterCommandHandler.BCommand overriddenCommand) {
        super("Overriding command '" + overriddenCommand.descriptor() + "' Creation stacktrace: \n" + overriddenCommand.creationStackStace);
    }
}
