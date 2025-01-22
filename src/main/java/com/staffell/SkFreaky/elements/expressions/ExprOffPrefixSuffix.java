package com.staffell.SkFreaky.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.OfflinePlayer;
import com.staffell.SkFreaky.util.Utils;
import org.eclipse.jdt.annotation.Nullable;


@Name("Offline Prefix/Suffix")
@Description("Gets the luckperms prefix or suffix of an offline player")
@Examples("send prefix of offlineplayer(\"Staffell\")")
@Since("1.0")
@RequiredPlugins({"LuckPerms"})
public class ExprOffPrefixSuffix extends SimplePropertyExpression<OfflinePlayer, String> {
    static {
        register(ExprOffPrefixSuffix.class, String.class, "[the] offline [luckperms] (1:prefix|2:suffix)", "offlineplayers");
    }

    private boolean suffix;


    @Override
    @SuppressWarnings("null")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        suffix = parseResult.mark == 2;
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public String convert(OfflinePlayer from) {
        return Utils.getPrefixOrSuffix(from, suffix);
    }

    @Override
    @Nullable
    public Class<?>[] acceptChange(ChangeMode mode) {
        return null;
    }

    @Override
    protected String getPropertyName() {
        return "Offline" + (suffix ? "suffix" : "prefix");
    }


    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }


}
