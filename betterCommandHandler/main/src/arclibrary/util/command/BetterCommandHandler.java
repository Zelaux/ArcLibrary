package arclibrary.util.command;


import arc.func.Boolf;
import arc.func.Cons;
import arc.struct.ObjectMap;
import arc.util.*;
import arclibrary.util.command.ParamHandler.Result;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


/**
 * Parses command syntax.
 * Recommend Arc Support plugin for Intellij idea (with version after 0.8.2) to highlight param language
 */
@SuppressWarnings("UnknownLanguage")
public class BetterCommandHandler extends CommandHandler {

    protected static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static NameOverridingLogger defaultNameOverridingLogger = new NameOverridingLogger.Impl(Log::err);
    public static boolean defaultAllowNameOverriding = true;
    private final ObjectMap<String, ParamHandler<?>> handlerObjectMap = new ObjectMap<>();
    private final ObjectMap<String, BCommand> commands;
    public NameOverridingLogger nameOverridingLogger = defaultNameOverridingLogger;
    public boolean allowNameOverriding = defaultAllowNameOverriding;

    /**
     * Creates a command handler with a specific command prefix.
     */
    public BetterCommandHandler(String prefix) {
        super(prefix);
        commands = Reflect.get(CommandHandler.class, this, "commands");
    }

    /**
     * Creates a command handler with an empty prefix
     */
    public BetterCommandHandler() {
        this("");
    }

    public void registerHandler(String name, ParamHandler<?> handler) {
        handlerObjectMap.put(name, handler);
    }

    /**
     * Handles a message with no additional parameters.
     */
    public BCommandResponse handleMessage(String message) {
        return handleMessage(message, null);
    }

    /**
     * Handles a message with optional extra parameters. Runs the command if successful.
     *
     * @return a response detailing whether or not the command was handled, and what went wrong, if applicable.
     */
    public BCommandResponse handleMessage(String message, Object params) {
        if (message == null || (!message.startsWith(prefix)))
            return new BCommandResponse(BResponseType.noCommand, null, null);

        int spaceIndex = message.indexOf(" ");
        String commandstr = spaceIndex != -1 ? message.substring(prefix.length(), spaceIndex) : message.substring(prefix.length());
        BCommand command = commands.get(commandstr);

        if (command != null) {
            CommandParamSplitter.SplitResponse splitResponse;
            if (spaceIndex == -1) {
                splitResponse = CommandParamSplitter.split("", 0, 0, command.myParams);
            } else {
                splitResponse = CommandParamSplitter.split(message, spaceIndex + 1, message.length(), command.myParams);
            }
            if (splitResponse.many) {
                return new BCommandResponse(BResponseType.manyArguments, command, commandstr);
            } else if (splitResponse.few) {
                return new BCommandResponse(BResponseType.fewArguments, command, commandstr);
            }
            CommandArguments args = splitResponse.args;
            for (int i = 0; i < command.myParams.params.length; i++) {
                BCommandParam param = command.myParams.params[i];
                if (args.has(i)) {
                    ParamHandler<?> handler = handlerObjectMap.getNull(param.handlerName);
                    if (handler == null) handler = ParamHandler.stringHandler;
                    Result<?> handle = handler.handle(command, args, i);
                    if (handle.isError()) {
                        return new BCommandResponse(BResponseType.nonValidParam, command, commandstr, handle.error);
                    }
                    args.setHandledObject(i, handle.value);
                }
            }
            command.myRunner.accept(args, params);
            return new BCommandResponse(BResponseType.valid, command, commandstr);
        } else {
            return new BCommandResponse(BResponseType.unknownCommand, null, commandstr);
        }
    }

    public void removeCommand(String text) {
        BCommand c = commands.get(text);
        if (c == null) return;
        commands.remove(text);
        getCommandList().remove(c);
    }


    /**
     * use ${@link BetterCommandHandler#bregister(String, String, BCommandRunner)}
     */
    @Override
    @Deprecated
    public <T> Command register(String text, String description, CommandRunner<T> runner) {
        return bregister(text, description, BCommandRunner.wrap(runner));
    }



    /**
     * use ${@link BetterCommandHandler#bregister(String, String, String, BCommandRunner)}
     */
    @Override
    @Deprecated
    public <T> Command register(String text, @Language("ExtendedArcCommandParams") String params, String description, CommandRunner<T> runner) {
        return bregister(text, params, description, BCommandRunner.wrap(runner));
    }

    /**
     * Register a command which handles a zero-sized list of arguments and one parameter.
     */
    public <T> BCommand bregister(String text, String description, BCommandRunner<T> runner) {
        return bregister(text, "", description, runner);
    }

    /**
     * Register a command which handles a list of arguments and one handler-specific parameter. <br>
     * argeter syntax is as follows: <br>
     * &lt;mandatory-arg-1&gt; &lt;mandatory-arg-2&gt; ... &lt;mandatory-arg-n&gt; [optional-arg-1] [optional-arg-2] <br>
     * Angle brackets indicate mandatory arguments. Square brackets to indicate optional arguments. <br>
     * All mandatory arguments must come before optional arguments. Arg names must not have spaces in them. <br>
     * You may also use the ... syntax after the arg name to designate that everything after it will not be split into extra arguments.
     * There may only be one such argument, and it must be at the end. For example, the syntax
     * &lt;arg1&gt [arg2...] will require a first argument, and then take any text after that and put it in the second argument, optionally.
     */
    public <T> BCommand bregister(String text, @Language("ExtendedArcCommandParams") String params, String description, BCommandRunner<T> runner) {
        //noinspection ThrowableNotThrown
        String stackTrace = StackTrace.create().getStringStackTrace(stackElementSkipper());
        //remove previously registered commands
        getCommandList().<BCommand>as().remove(c -> {
            if (!c.text.equals(text)) {
                return false;
            }
            if (!allowNameOverriding) throw new CommandOverridingNotAllowed(c, stackTrace);
            if (nameOverridingLogger != null) {
                nameOverridingLogger.log(c,stackTrace);
            }
            return true;
        });

        BCommand cmd = new BCommand(text, params, description, runner);
        commands.put(text.toLowerCase(), cmd);
        getCommandList().add(cmd);
        return cmd;
    }

    @NotNull
    private static Boolf<StackTraceElement> stackElementSkipper() {
        return it -> {
            String className = it.getClassName();
            return className.startsWith(BetterCommandHandler.class.getCanonicalName());
        };
    }


    /**
     * use ${@link BetterCommandHandler#bregister(String, String, Runnable)}
     */
    @Override
    @Deprecated
    public Command register(String text, String description, Cons<String[]> runner) {
        return bregister(text, description, () -> runner.get(EMPTY_STRING_ARRAY));
    }

    /**
     * use ${@link BetterCommandHandler#bregister(String, String, String, BCommandRunner.ShortACommandRunner)}
     */
    @Override
    @Deprecated
    public Command register(String text, @Language("ExtendedArcCommandParams") String params, String description, Cons<String[]> runner) {
        return bregister(text, params, description, (args) -> runner.get(args.getCopyRawStrings()));
    }

    /**
     * full name betterRegister
     */
    public BCommand bregister(String text, String description, Runnable runner) {
        return bregister(text, description, (BCommandRunner<Object>) (a, b) -> runner.run());
    }

    /**
     * full name betterRegister
     */
    public BCommand bregister(String text, @Language("ExtendedArcCommandParams") String params, String description, BCommandRunner.ShortACommandRunner runner) {
        return bregister(text, params, description, runner.full());
    }


    public enum BResponseType {
        noCommand, unknownCommand, fewArguments, manyArguments, valid, nonValidParam;
        public final ResponseType mirror = Structs.find(ResponseType.values(), it -> it.name().equals(name()));
    }

    public interface BCommandRunner<T> {
        static <T> BCommandRunner<T> wrap(CommandRunner<T> runner) {
            return new WrappedRunner<>(runner);
        }

        void accept(CommandArguments args, T parameter);

        interface ShortACommandRunner {
            void accept(CommandArguments args);

            default BCommandRunner<Object> full() {
                return new TransformedRunner(this);
            }
        }

        final class TransformedRunner implements BCommandRunner<Object> {
            public ShortACommandRunner runner;

            public TransformedRunner(ShortACommandRunner runner) {
                this.runner = runner;
            }

            @Override
            public void accept(CommandArguments args, Object parameter) {
                runner.accept(args);
            }
        }

        class WrappedRunner<T> implements BCommandRunner<T> {
            public CommandRunner<T> runner;

            public WrappedRunner(CommandRunner<T> runner) {
                this.runner = runner;
            }

            @Override
            public void accept(CommandArguments args, T parameter) {
                runner.accept(args.getCopyRawStrings(), parameter);
            }
        }
    }

    public static class BCommand extends CommandHandler.Command {

        public final String myParamText;
        public final CommandParams myParams;
        public final String creationStackStace;
        @SuppressWarnings("rawtypes")
        final BCommandRunner myRunner;

        public BCommand(String text, @Language("ExtendedArcCommandParams") String paramText, String description, BCommandRunner runner) {
            super(text, mockParams(paramText), description, (args, parameter) -> {
                if (runner instanceof BCommandRunner.WrappedRunner) {
                    //noinspection unchecked,rawtypes
                    ((BCommandRunner.WrappedRunner) runner).runner.accept(args, parameter);
                    return;
                }
                throw new IllegalArgumentException("You cannot use ACommandHandler#runner");
//                runner.accept(, parameter);
            });
//            myText = text;
            this.myParamText = paramText;
            this.myRunner = runner;

            creationStackStace = CreationStackTrace.create().getStringStackTrace(stackElementSkipper());
            myParams = CommandParamParser.parse(paramText);
        }

        @NotNull
        private static String mockParams(@Language("ExtendedArcCommandParams") String paramText) {
            CommandParams parsed = CommandParamParser.parse(paramText);
            StringBuilder builder = new StringBuilder();
            int lastRequired = -1;
            for (int i = parsed.params.length - 1; i >= 0; i--) {
                if (!parsed.params[i].optional) {
                    lastRequired = i;
                    break;
                }
            }
            for (int i = 0; i < parsed.params.length; i++) {
                if (i > 0) builder.append(' ');
                BCommandParam param = parsed.params[i];
                if (lastRequired > i) {
                    builder.append('[');
                    builder.append(param.name.replace(' ', '_'));
                    builder.append(']');
                } else {
                    builder.append('<');
                    builder.append(param.name.replace(' ', '_'));
                    builder.append('>');
                }
            }
            return builder.toString();
        }

        public String descriptor() {
            return text + " " + myParamText;
        }
    }

    public static class BCommandParam {
        public final String name;
        public final boolean optional;
        public final boolean variadic;
        @Nullable
        public final String handlerName;

        public BCommandParam(String name, boolean optional, boolean variadic, String handlerName) {
            this.name = name;
            this.optional = optional;
            this.variadic = variadic;
            this.handlerName = handlerName;
        }

        @Override
        public String toString() {
            return "BCommandParam{" +
                    "name='" + name + '\'' +
                    ", optional=" + optional +
                    ", variadic=" + variadic +
                    ", handlerName='" + handlerName + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BCommandParam that = (BCommandParam) o;
            return optional == that.optional && variadic == that.variadic && Objects.equals(name, that.name) && Objects.equals(handlerName, that.handlerName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, optional, variadic, handlerName);
        }
    }

    public static class BCommandResponse extends CommandHandler.CommandResponse {
        public final BResponseType myType;
        public final String extra;

        public BCommandResponse(BResponseType type, BCommand command, String runCommand) {
            super(type.mirror, command, runCommand);
            this.myType = type;
//            this.command = command;
//            this.runCommand = runCommand;
            extra = null;
        }

        public BCommandResponse(BResponseType type, BCommand command, String runCommand, String extra) {
            super(type.mirror, command, runCommand);
            this.myType = type;
//            this.command = command;
//            this.runCommand = runCommand;
            this.extra = extra;
        }
    }
}
