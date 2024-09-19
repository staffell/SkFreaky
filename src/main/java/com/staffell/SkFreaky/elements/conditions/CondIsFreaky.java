package com.staffell.SkFreaky.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Animals;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

@Name("Is Freaky")
@Description("Checks if an entity is freaky.")
@Examples("if target entity is freaky:")
@Since("1.0.0")
public class CondIsFreaky extends Condition {

    static {
        Skript.registerCondition(CondIsFreaky.class,
                "%livingentity% is [currently] [getting] freaky",
                "%livingentity% is(n't| not) [currently] [getting] freaky");
    }

    private Expression<Entity> entityExpr;
    private boolean negate;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        entityExpr = (Expression<Entity>) exprs[0];
        negate = matchedPattern == 1;
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (entityExpr == null) return false;
        Entity entity = entityExpr.getSingle(e);
        if (entity == null) return false;
        if (entity instanceof Animals animal) {
            return negate !=animal.isLoveMode();
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        //return (entityExpr == null ? "" : (" of " + (e == null ? "" : entityExpr.toString(e, debug))));
        return entityExpr.toString(e, debug) + " is " + (negate ? "n't " : " ") + "freaky";
    }
}