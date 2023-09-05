package arclibrary.util.command;

import arc.func.Cons;

public interface NameOverridingLogger {
    void log(BetterCommandHandler.BCommand overriddenCommand, String stackTrace);

    final class Empty implements NameOverridingLogger {

        @Override
        public void log(BetterCommandHandler.BCommand overriddenCommand, String stackTrace) {

        }
    }

    final class Impl implements NameOverridingLogger {
        public final Cons<String> stringLogger;

        public Impl(Cons<String> stringLogger) {
            this.stringLogger = stringLogger;
        }

        @Override
        public void log(BetterCommandHandler.BCommand overriddenCommand, String stackTrace) {
            stringLogger.get(CommandOverridingNotAllowed.createMessage(overriddenCommand, stackTrace));
        }
    }
}
