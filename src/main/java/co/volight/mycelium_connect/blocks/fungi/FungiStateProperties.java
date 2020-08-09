package co.volight.mycelium_connect.blocks.fungi;

import net.minecraft.state.EnumProperty;
import net.minecraft.state.properties.RedstoneSide;

public class FungiStateProperties {
    public static final EnumProperty<FungiSide> EAST = EnumProperty.create("east", FungiSide.class);
    public static final EnumProperty<FungiSide> NORTH = EnumProperty.create("north", FungiSide.class);
    public static final EnumProperty<FungiSide> SOUTH = EnumProperty.create("south", FungiSide.class);
    public static final EnumProperty<FungiSide> WEST = EnumProperty.create("west", FungiSide.class);
}
