package com.staffell.SkFreaky.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemData;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.bukkitutil.ItemUtils;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

/**
 * @author Staffell
 */

@Name("Is Freaky")
@Description("Checks if an entity is freaky (in love mode)")
@Examples("if target entity is freaky:")
@Since("1.0")
public class CondCrossbowIsLoaded extends Condition {

    static {
        Skript.registerCondition(CondCrossbowIsLoaded.class,
                "%itemstack/slot/itemtype% is (loaded|charged) [with %-itemtype%]",
                "%itemstack/slot/itemtype% is[(n't| not)] (loaded|charged) [with %-itemtype%]");
    }

    private Expression<?> bow;
    private Expression<ItemType> type;
    private boolean negate;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        bow = exprs[0];
        type = (Expression<ItemType>) exprs[1];
        negate = matchedPattern == 1;
        return true;
    }

    @Override
    public boolean check(Event e) {
        boolean contains = negate;
        if (bow == null) return false;
        ItemStack itemStack = ItemUtils.asItemStack(bow.getSingle(e));
        if (itemStack == null) return false;
        if (itemStack.getItemMeta() instanceof CrossbowMeta crossbow) {
            if (type == null) {
                return negate != crossbow.hasChargedProjectiles();
            } else {
                ItemType item = type.getSingle(e);
                for (ItemStack stack : crossbow.getChargedProjectiles()) {
                    if (stack.getType() == item.getMaterial()) return !negate;
                    }
                }
            }
        return contains;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return bow.toString(e, debug) + " is " + (negate ? "n't " : " ") + "loaded";
    }
}