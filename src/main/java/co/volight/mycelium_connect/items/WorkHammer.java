package co.volight.mycelium_connect.items;

import co.volight.mycelium_connect.MCC;
import co.volight.mycelium_connect.tier.ObsidianTier;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.IVanishable;
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

public class WorkHammer extends TieredItem implements IVanishable, IForgeItem {
    public static class NormalWorkHammer extends WorkHammer {
        public static final String name = "work_hammer";

        public NormalWorkHammer() {
            this(ObsidianTier.tier, 5, -3.5f, new Item.Properties().group(MCC.MainGroup));
        }

        public NormalWorkHammer(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
            super(tier, attackDamage, attackSpeed, properties);
            setRegistryName(MCC.ID, name);
        }
    }

    public static class StoneWorkHammer extends WorkHammer {
        public static final String name = "stone_work_hammer";

        public StoneWorkHammer() {
            this(ItemTier.STONE, 5, -2.5f, new Item.Properties().group(MCC.MainGroup));
        }

        public StoneWorkHammer(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
            super(tier, attackDamage, attackSpeed, properties);
            setRegistryName(MCC.ID, name);
        }
    }

    public static class IronWorkHammer extends WorkHammer {
        public static final String name = "iron_work_hammer";

        public IronWorkHammer() {
            this(ItemTier.IRON, 5, -3.0f, new Item.Properties().group(MCC.MainGroup));
        }

        public IronWorkHammer(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
            super(tier, attackDamage, attackSpeed, properties);
            setRegistryName(MCC.ID, name);
        }
    }

    public static class GoldenWorkHammer extends WorkHammer {
        public static final String name = "golden_work_hammer";

        public GoldenWorkHammer() {
            this(ItemTier.GOLD, 4, -2.0f, new Item.Properties().group(MCC.MainGroup));
        }

        public GoldenWorkHammer(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
            super(tier, attackDamage, attackSpeed, properties);
            setRegistryName(MCC.ID, name);
        }
    }

    public static class DiamondWorkHammer extends WorkHammer {
        public static final String name = "diamond_work_hammer";

        public DiamondWorkHammer() {
            this(ItemTier.DIAMOND, 5, -3.0f, new Item.Properties().group(MCC.MainGroup));
        }

        public DiamondWorkHammer(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
            super(tier, attackDamage, attackSpeed, properties);
            setRegistryName(MCC.ID, name);
        }
    }

    public static class NetheriteWorkHammer extends WorkHammer {
        public static final String name = "netherite_work_hammer";

        public NetheriteWorkHammer() {
            this(ItemTier.NETHERITE, 5, -2.5f, new Item.Properties().group(MCC.MainGroup));
        }

        public NetheriteWorkHammer(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
            super(tier, attackDamage, attackSpeed, properties);
            setRegistryName(MCC.ID, name);
        }
    }

    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> attributeModifier;
    protected Random rand = new Random();

    public WorkHammer(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, properties);
        this.attackDamage = (float)attackDamage + tier.getAttackDamage();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.field_233823_f_, // generic.attack_damage
                new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.field_233825_h_, // generic.attack_speed
                new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)attackSpeed, AttributeModifier.Operation.ADDITION));
        this.attributeModifier = builder.build();
    }

    public float getAttackDamage() {
        return this.attackDamage;
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
        return material != Material.PLANTS && material != Material.TALL_PLANTS && material != Material.CORAL && !state.func_235714_a_(BlockTags.LEAVES) && material != Material.GOURD ? 1.0F : 2.5F;
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
}
