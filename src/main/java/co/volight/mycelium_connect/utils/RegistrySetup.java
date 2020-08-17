package co.volight.mycelium_connect.utils;

import co.volight.mycelium_connect.MCC;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface RegistrySetup {
    default <S extends V, V extends IForgeRegistryEntry<V>> S regName(String name) {
        return (S)((ForgeRegistryEntry<V>)this).setRegistryName(MCC.ID, name);
    }
}
