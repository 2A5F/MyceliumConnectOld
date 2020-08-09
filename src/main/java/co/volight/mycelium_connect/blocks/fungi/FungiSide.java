package co.volight.mycelium_connect.blocks.fungi;

import net.minecraft.util.IStringSerializable;
import javax.annotation.Nonnull;

public enum FungiSide implements IStringSerializable {
    UP("up"),
    SIDE("side"),
    NONE("none");

    private final String name;

    private FungiSide(String name) {
        this.name = name;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    @Override
    public String func_176610_l() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean notNone() {
        return this != NONE;
    }
}
