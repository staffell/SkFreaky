package com.staffell.SkFreaky.elements.expressions;

import ch.njol.skript.util.Utils;
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
@Name("Skript Gradient")
@Description("Joins a string with \"'\"")
@Examples("send apostrophized \"Hello World!\" # would output \"H'e'l'l'o' 'W'o'r'l'd'!'\"")
@Since("1.0")
public class ExprSkriptGradient extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprSkriptGradient.class, String.class, ExpressionType.COMBINED,
                "skript gradient of %string%");
    }


    @SuppressWarnings("null")
    private Expression<String> string;

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
        String formatted = input.replaceAll("&#([a-fA-F0-9]{6})", "<#$1>");
        return new String[] {Utils.replaceChatStyles(formatted)};

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
        return "skript gradient " + string.toString(event, debug);

    }

}
