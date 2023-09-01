package arclibrary.util.command;

public interface ParamHandler<T>{
    ParamHandler<String> stringHandler = (c, args, i) -> Result.success(args.getRawString(i));

    Result<T> handle(BetterCommandHandler.BCommand command, CommandArguments arguments, int index);

    final class Result<T>{
        public final T value;
        public final String error;

        private Result(T value){
            this.value = value;
            error = null;
        }

        private Result(boolean bool, String error){
            this.error = error;
            this.value = null;
        }

        public static <T> Result<T> success(T value){
            return new Result<>(value);
        }

        public static <T> Result<T> error(String value){
            return new Result<>(false, value);
        }

        public final boolean isSuccess(){
            return error == null;
        }

        public final boolean isError(){
            return error != null;
        }

    }
}
