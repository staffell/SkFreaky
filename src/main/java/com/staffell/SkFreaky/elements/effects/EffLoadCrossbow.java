package com.staffell.SkFreaky.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.bukkitutil.ItemUtils;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.slot.Slot;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Load Crossbows")
@Description("Loads a crossbow")
@Examples("load tool of player with 4 arrows")
@Since("1.0")
public class EffLoadCrossbow extends Effect { // To register an effect, extend the class with Effect

    static {
        Skript.registerEffect(EffLoadCrossbow.class, "load %itemstacks/slots/itemtypes% with %itemtypes%");
    }
    private Expression<?> item;
    private Expression<ItemType> material;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.item = expressions[0];
        this.material = (Expression<ItemType>) expressions[1];
        return true;
    }

    @Override
    protected void execute(Event event) {
        ItemType[] mats = material.getArray(event);
        ArrayList<ItemStack> list = new ArrayList<>();
        for (ItemType thisItem : mats) {
            for (int i = 0; i < thisItem.getAmount(); i++) {
                list.add(new ItemStack(thisItem.getMaterial()));
            }
        }
        List<ItemStack> list1 = list.subList(0, list.size());
        for (Object object : item.getArray(event)) {
            ItemStack itemStack = ItemUtils.asItemStack(object);
            if (!(itemStack.getItemMeta() instanceof CrossbowMeta meta)) {
                continue;
            }
            if (meta != null) {
                meta.setChargedProjectiles(list1);
                if (object instanceof Slot slot) {
                    itemStack.setItemMeta(meta);
                    slot.setItem(itemStack);
                } else if (object instanceof ItemType crossbow) {
                    crossbow.setItemMeta(meta);
                } else if (object instanceof ItemStack crossbow) {
                    crossbow.setItemMeta(meta);
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "load " + item.toString(event, debug) + " with " + material.toString(event, debug);
    }

}