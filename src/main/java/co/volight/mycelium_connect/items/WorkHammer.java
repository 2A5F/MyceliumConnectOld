package co.volight.mycelium_connect.items;

import co.volight.mycelium_connect.MCC;
import co.volight.mycelium_connect.tier.ObsidianTier;
import co.volight.mycelium_connect.utils.RegistrySetup;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeItem;
import javax.annotation.Nonnull;
import java.util.Random;

public class WorkHammer extends TieredItem implements IVanishable, IForgeItem, RegistrySetup {
    public static class NormalWorkHammer extends WorkHammer {
        public static final String name = "work_hammer";

        public static NormalWorkHammer setup() {
            return new NormalWorkHammer(
                    ObsidianTier.tier, 5, -3.5f, new Item.Properties().group(MCC.MainGroup)
            ).regName(name);
        }

        public NormalWorkHammer(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
            super(tier, attackDamage, attackSpeed, properties);
        }
    }

    public static class StoneWorkHammer extends WorkHammer {
        public static final String name = "stone_work_hammer";

        public static StoneWorkHammer setup() {
            return new StoneWorkHammer(
                    ItemTier.STONE, 5, -2.5f, new Item.Properties().group(MCC.MainGroup)
            ).regName(name);
        }

        public StoneWorkHammer(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
            super(tier, attackDamage, attackSpeed, properties);
        }
    }

    public static class IronWorkHammer extends WorkHammer {
        public static final String name = "iron_work_hammer";

        public static IronWorkHammer setup() {
            return new IronWorkHammer(
                    ItemTier.IRON, 5, -3.0f, new Item.Properties().group(MCC.MainGroup)
            ).regName(name);
        }

        public IronWorkHammer(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
            super(tier, attackDamage, attackSpeed, properties);
        }
    }

    public static class GoldenWorkHammer extends WorkHammer {
        public static final String name = "golden_work_hammer";

        public static GoldenWorkHammer setup() {
            return new GoldenWorkHammer(
                    ItemTier.GOLD, 5, -2.0f, new Item.Properties().group(MCC.MainGroup)
            ).regName(name);
        }

        public GoldenWorkHammer(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
            super(tier, attackDamage, attackSpeed, properties);
        }
    }

    public static class DiamondWorkHammer extends WorkHammer {
        public static final String name = "diamond_work_hammer";

        public static DiamondWorkHammer setup() {
            return new DiamondWorkHammer(
                    ItemTier.DIAMOND, 5, -3.0f, new Item.Properties().group(MCC.MainGroup)
            ).regName(name);
        }

        public DiamondWorkHammer(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
            super(tier, attackDamage, attackSpeed, properties);
        }
    }

    public static class NetheriteWorkHammer extends WorkHammer {
        public static final String name = "netherite_work_hammer";

        public static NetheriteWorkHammer setup() {
            return new NetheriteWorkHammer(
                    ItemTier.NETHERITE, 5, -2.5f, new Item.Properties().group(MCC.MainGroup)
            ).regName(name);
        }

        public NetheriteWorkHammer(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
            super(tier, attackDamage, attackSpeed, properties);
        }
    }

    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> attributeModifier;
    protected Random rand = new Random();

    public WorkHammer(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, properties.setNoRepair());
        this.attackDamage = (float)attackDamage + tier.getAttackDamage();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, // generic.attack_damage
                new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, // generic.attack_speed
                new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)attackSpeed, AttributeModifier.Operation.ADDITION));
        this.attributeModifier = builder.build();
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, @Nonnull ItemStack repair) {
        return false;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack item = itemStack.copy();
        item.setDamage(itemStack.getDamage());
        if (item.attemptDamageItem(1, this.rand, null)) {
            return itemStack;
        }
        return item;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return !player.isCreative();
    }

    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Material material = state.getMaterial();
        return material != Material.PLANTS && material != Material.TALL_PLANTS && material != Material.CORAL && !state.isIn(BlockTags.LEAVES) && material != Material.GOURD ? 1.0F : 2.5F;
    }

    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damageItem(1, attacker, (entity) -> {
            entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
        });
        return true;
    }

    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (state.getBlockHardness(worldIn, pos) != 0.0F) {
            stack.damageItem(2, entityLiving, (entity) -> {
                entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
            });
        }
        return true;
    }

    @Nonnull
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return equipmentSlot == EquipmentSlotType.MAINHAND ? this.attributeModifier : super.getAttributeModifiers(equipmentSlot);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (super.canApplyAtEnchantingTable(stack, enchantment)) return true;
        if (enchantment instanceof DamageEnchantment) return true;
        if (enchantment instanceof LootBonusEnchantment) return true;
        if (enchantment instanceof FireAspectEnchantment) return true;
        if (enchantment instanceof KnockbackEnchantment) return true;
        if (enchantment instanceof SweepingEnchantment) return true;
        if (enchantment instanceof MendingEnchantment) return true;
        if (enchantment instanceof UnbreakingEnchantment) return true;
        return false;
    }
}
