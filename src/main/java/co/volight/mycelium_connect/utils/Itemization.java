package co.volight.mycelium_connect.utils;

import co.volight.mycelium_connect.MCC;
import net.minecraft.item.Item;

public interface Itemization {
    default void itemization(Item.Properties properties) {
        properties.group(MCC.MainGroup);
    }
}
