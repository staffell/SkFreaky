package com.staffell.SkFreaky.elements.expressions;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

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

/**
 * @author Staffell
 */
@Name("Apostrophize")
@Description("Joins a string with \"'\"")
@Examples("send apostrophized \"Hello World!\" # would output \"H'e'l'l'o' 'W'o'r'l'd'!'\"")
@Since("2.1, 2.5.2 (regex support), 2.7 (case sensitivity)")
public class ExprApostrophized extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprApostrophized.class, String.class, ExpressionType.COMBINED,
                "apostrophized %string%");
    }


    @SuppressWarnings("null")
    private Expression<String> string;
    @Nullable
    private Expression<String> delimiter;

    @Override
    @SuppressWarnings({"unchecked", "null"})
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        string = (Expression<String>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    protected String[] get(Event event) {
        String input = this.string.getSingle(event);
        if (input == null) return null;
        String[] strings = input.split("");
        String formatted = String.join("'", strings);
        return new String[] {formatted};

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
        return "apostrophized " + string.toString(event, debug);

    }

}