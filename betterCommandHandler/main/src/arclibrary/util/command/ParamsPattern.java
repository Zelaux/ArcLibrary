package arclibrary.util.command;

import arc.struct.Bits;
import arc.util.Structs;

public class ParamsPattern {
    public final BetterCommandHandler.BCommandParam[] params;
    public final int variadicIndex;
    public final int requiredAmount;
    public Bits separators = new Bits();

    public ParamsPattern(BetterCommandHandler.BCommandParam[] params) {
        this.params = params;
        variadicIndex = Structs.indexOf(params, it -> it.variadic);
        requiredAmount = Structs.count(params, it -> !it.optional);
        separators(' ');


    }

    public ParamsPattern separators(char... separators) {
        this.separators.clear();
        for (char separator : separators) {
            this.separators.set(separator);
        }
        return this;
    }

    public char[] separators() {
        int size = 0;
        for (int i = 0; i < separators.length(); i++) {
            if (separators.get(i)) size++;
        }
        char[] chars = new char[size];
        int charIdx = 0;
        for (int i = 0; i < separators.length(); i++) {
            if (separators.get(i)) {
                chars[charIdx] = (char) i;
                charIdx++;
            }
        }
        return chars;
    }

    public boolean isSeparator(char c) {
        return separators.get(c);
    }
}
