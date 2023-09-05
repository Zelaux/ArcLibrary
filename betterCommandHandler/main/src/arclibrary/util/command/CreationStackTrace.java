package arclibrary.util.command;

import arc.func.Boolf;
import arc.util.Strings;

public final class CreationStackTrace extends StackTrace {
    private CreationStackTrace() {
        super();
    }
    public static CreationStackTrace create(){
        return new CreationStackTrace();
    }
}
