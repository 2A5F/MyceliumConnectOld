package co.volight.mycelium_connect.blocks.produce.glasskiln;

import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.IIntArray;

public class GlassKilnData implements IIntArray {
    public int burnTime;
    public int fuelTime;
    public int cookTime;
    public int cookTimeTotal;
    public boolean isCooking;

    public GlassKilnData() {
        this(0,0,0,0, false);
    }

    public GlassKilnData(int burnTime, int fuelTime, int cookTime, int cookTimeTotal, boolean isCooking) {
        this.burnTime = burnTime;
        this.fuelTime = fuelTime;
        this.cookTime = cookTime;
        this.cookTimeTotal = cookTimeTotal;
        this.isCooking = isCooking;
    }

    @Override
    public int get(int index) {
        switch(index) {
            case 0: return burnTime;
            case 1: return fuelTime;
            case 2: return cookTime;
            case 3: return cookTimeTotal;
            case 4: return isCooking ? 1 : 0;
            default: return 0;
        }
    }

    @Override
    public void set(int index, int value) {
        switch(index) {
            case 0: burnTime = value; break;
            case 1: fuelTime = value; break;
            case 2: cookTime = value; break;
            case 3: cookTimeTotal = value; break;
            case 4: isCooking = value > 0; break;
        }
    }

    @Override
    public int size() {
        return 5;
    }
}
