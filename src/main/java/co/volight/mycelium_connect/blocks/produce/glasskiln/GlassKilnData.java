package co.volight.mycelium_connect.blocks.produce.glasskiln;

import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.IIntArray;

public class GlassKilnData implements IIntArray {
    public int burnTime;
    public int fuelTime;
    public int cookTime;
    public int cookTimeTotal;

    public GlassKilnData() {
        this(0,0,0,0);
    }

    public GlassKilnData(int burnTime, int fuelTime, int cookTime, int cookTimeTotal) {
        this.burnTime = burnTime;
        this.fuelTime = fuelTime;
        this.cookTime = cookTime;
        this.cookTimeTotal = cookTimeTotal;
    }

    @Override
    public int get(int index) {
        switch(index) {
            case 0: return burnTime;
            case 1: return fuelTime;
            case 2: return cookTime;
            case 3: return cookTimeTotal;
            default: return 0;
        }
    }

    @Override
    public void set(int index, int value) {
        switch(index) {
            case 0: burnTime = value; break;
            case 1: fuelTime = value; break;
            case 2: cookTime = value; break;
            case 3: cookTimeTotal = value;
        }
    }

    @Override
    public int size() {
        return 4;
    }
}
