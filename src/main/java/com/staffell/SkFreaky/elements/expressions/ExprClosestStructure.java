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
import org.bukkit.generator.structure.StructureType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;

import org.eclipse.jdt.annotation.Nullable;


@Name("The Closest Structure")
@Description("Sends the nearest structure of a certain type.")
@Examples("teleport player to closest \"mineshaft\" at first \" \" # Sends \"Hello\" and \"there, world!\"")
@Since("1.0")
public class ExprClosestStructure extends SimpleExpression<Location> {
    static {
        Skript.registerExpression(ExprClosestStructure.class, Location.class, ExpressionType.COMBINED,
                "[the] closest ((not |un)(explored|discovered)|) %string% (in|within) [a] radius [of] %number% (around|at|from|of) %location%");
    }

    private Expression<String> type;
    private Expression<Number> radius;
    private Expression<Location> center;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        type = (Expression<String>) exprs[0];
        radius = (Expression<Number>) exprs[1];
        center = (Expression<Location>) exprs[2];
        return true;
    }


    @Nullable
    @Override
    protected Location[] get(Event event) {
        String converted = type.getSingle(event);
        converted = converted.toLowerCase();
        if (converted.equals("mansion")) converted = "woodland mansion";
        if (converted.equals("nether fortress")) converted = "fortress";
        StructureType type = switch (converted) {
            case "buried treasure" -> StructureType.BURIED_TREASURE;
            case "desert pyramid" -> StructureType.DESERT_PYRAMID;
            case "end city" -> StructureType.END_CITY;
            case "fortress" -> StructureType.FORTRESS;
            case "igloo" -> StructureType.IGLOO;
            case "jigsaw" -> StructureType.JIGSAW;
            case "jungle temple" -> StructureType.JUNGLE_TEMPLE;
            case "mineshaft" -> StructureType.MINESHAFT;
            case "nether fossil" -> StructureType.NETHER_FOSSIL;
            case "ocean monument" -> StructureType.OCEAN_MONUMENT;
            case "ocean ruin" -> StructureType.OCEAN_RUIN;
            case "ruined portal" -> StructureType.RUINED_PORTAL;
            case "shipwreck" -> StructureType.SHIPWRECK;
            case "stronghold" -> StructureType.STRONGHOLD;
            case "swamp hut" -> StructureType.SWAMP_HUT;
            case "woodland mansion" -> StructureType.WOODLAND_MANSION;
            default -> null;
        };
        World world = center.getSingle(event).getWorld();
        Location loca = center.getSingle(event);
        int distance = radius.getSingle(event).intValue();
        return new Location[] {world.locateNearestStructure(loca, type, distance, false).getLocation()};

    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Location> getReturnType() {
        return Location.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "the closest " + type + "in radius" + radius + "of " + center;
    }

}
