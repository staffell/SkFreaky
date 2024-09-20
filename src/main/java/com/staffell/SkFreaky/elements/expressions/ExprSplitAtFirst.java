package com.staffell.SkFreaky.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

/**
 * @author Staffell
 */

@Name("Split at first")
@Description("Splits a string at the first occurrence of the given delimiter")
@Examples("send split \"Hello there, world!\" at first \" \" # would send \"Hello\" and \"there, world!\"")
@Since("1.0")
public class ExprSplitAtFirst extends SimpleExpression<String> {
    static {
        Skript.registerExpression(ExprSplitAtFirst.class, String.class, ExpressionType.COMBINED,
                "split %string% at [the] first [occurrence of [delimiter]] %string%",
                "%string% split at [the] first [occurrence of [delimiter]] %string%");
    }


    @SuppressWarnings("null")
    private Expression<String> strings;
    @Nullable
    private Expression<String> delimiter;

    @Override
    @SuppressWarnings({"unchecked", "null"})
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        strings = (Expression<String>) exprs[0];
        delimiter = (Expression<String>) exprs[1];
        return true;
    }

    @Nullable
    @Override
    protected String[] get(Event event) {
        String input = this.strings.getSingle(event);
        if (input == null) return null;
        String del = delimiter.getSingle(event);
        if (del == null) return null;
        return input.split(del, 1);
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Split at first " + strings.toString(event, debug);

    }

}
