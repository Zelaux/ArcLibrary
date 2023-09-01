package arclibrary.util.command;

import arc.func.Cons;

public interface NameOverridingLogger {
    void log(BetterCommandHandler.BCommand overriddenCommand);

    final class Empty implements NameOverridingLogger {

        @Override
        public void log(BetterCommandHandler.BCommand overriddenCommand) {

        }
    }

    final class Impl implements NameOverridingLogger {
        public final Cons<String> stringLogger;

        public Impl(Cons<String> stringLogger) {
            this.stringLogger = stringLogger;
        }

        @Override
        public void log(BetterCommandHandler.BCommand overriddenCommand) {
            stringLogger.get("Overriding command '" + overriddenCommand.descriptor() + "' Creation stacktrace: \n" + overriddenCommand.creationStackStace);
        }
    }
}
