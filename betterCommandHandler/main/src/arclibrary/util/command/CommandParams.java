package arclibrary.util.command;

import arc.util.Structs;

public class CommandParams {
  public final BetterCommandHandler.CommandParam[] params;
  public final int variadicIndex;
  public final int requiredAmount;

  public CommandParams(BetterCommandHandler.CommandParam[] params) {
    this.params = params;
    variadicIndex= Structs.indexOf(params, it->it.variadic);
    requiredAmount= Structs.count(params, it->!it.optional);


  }
}