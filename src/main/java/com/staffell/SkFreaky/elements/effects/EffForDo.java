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
        Skript.registerEffect(EffForDo.class, "for %~object% in %objects% (do|->) \\(<.+>\\)");
    }

    private Expression<?> index, looping;
    private String rawEffect;
    private Effect parsedEffect;

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
        this.parsedEffect = Effect.parse(this.rawEffect, "Can't understand this effect: " + rawEffect);
        return parsedEffect != null;
    }

    @Override
    protected void execute(Event event) {
        for (Object value : this.looping.getArray(event)) {
            this.index.change(event, new Object[] {value}, Changer.ChangeMode.SET);
            TriggerItem.walk(this.parsedEffect, event);
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "for each " + this.index.toString(event, debug) + " in " + this.looping.toString(event, debug) + " do (" + this.rawEffect +")";
    }
}