package arclibrary.util.command;

import arc.util.Threads;

public class CommandOverridingNotAllowed extends RuntimeException {
    public CommandOverridingNotAllowed(BetterCommandHandler.BCommand overriddenCommand, String stackTrace) {
        super(createMessage(overriddenCommand, stackTrace));
    }
private static final ThreadLocal<StringBuilder> tmpBuilder= Threads.local(StringBuilder::new);
    public static String createMessage(BetterCommandHandler.BCommand overriddenCommand, String stackTrace) {
        StringBuilder builder = tmpBuilder.get();
        builder.setLength(0);
        builder.append("Try to override command '");
        builder.append(overriddenCommand.descriptor());
        builder.append("' ");
        builder.append("Command creating stacktrace:\n");
        builder.append(overriddenCommand.creationStackStace);
        builder.append("\n");
        builder.append("Overriding stacktrace:\n");
        builder.append(stackTrace);
        return builder.toString();
    }
}
