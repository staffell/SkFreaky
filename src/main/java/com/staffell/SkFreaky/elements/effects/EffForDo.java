package com.staffell.SkFreaky.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.util.LiteralUtils;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;


@Name("Inline \"For Each\" loop")
@Description("Runs one line of code on every value in a list. This is mainly meant to be used for debugging, and overloading it can easily cause lag or increase parse times.")
@Examples("for {_test} in players do send {list::%uuid of {_test}%}")
@Since("1.0")
public class EffForDo extends Effect {
    static {
        Skript.registerEffect(EffForDo.class, "for %~object% in %objects% (do|->) \\(<.+>\\) [cond:\\(if <.+>\\)]");
    }

    private Expression<?> index, looping;
    private String rawEffect;
    private String rawCond;
    private Effect parsedAction;
    private Condition parsedCond;
    private boolean hasCond = false;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.index = expressions[0];
        this.looping = expressions[1];
        if (LiteralUtils.hasUnparsedLiteral(this.looping)) this.looping = LiteralUtils.defendExpression(this.looping);
        if (!(this.index instanceof Variable var && var.isLocal())) {
            Skript.error("The first expression must be a local variable");
            return false;
        }
        this.rawEffect = parseResult.regexes.get(0).group();
        this.parsedAction = Effect.parse(this.rawEffect, "Can't understand this effect: " + rawEffect);
        if (parseResult.hasTag("cond")) {
            this.rawCond = parseResult.regexes.get(1).group();
            this.parsedCond = Condition.parse(this.rawCond, "Can't understand this condition: " + rawCond);
            if (parsedCond != null) this.hasCond = true;
        }
        return parsedAction != null;
    }

    @Override
    protected void execute(Event event) {
        for (Object value : this.looping.getArray(event)) {
            if (this.hasCond) {
                if (!this.parsedCond.check(event)) continue;
            }
            this.index.change(event, new Object[] {value}, Changer.ChangeMode.SET);
            parsedAction.run(event);
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        if (this.hasCond) return "for each " + this.index.toString(event, debug) + " in " + this.looping.toString(event, debug) + " do (" + this.rawEffect +") if (" + this.rawCond + ")";
        return "for each " + this.index.toString(event, debug) + " in " + this.looping.toString(event, debug) + " do (" + this.rawEffect +")";
    }
}