package me.viciscat.coolattributes;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = CoolAttributes.MOD_ID,
        name = CoolAttributes.MOD_NAME,
        version = CoolAttributes.VERSION
)
public class CoolAttributes {

    public static final String MOD_ID = "cool-attributes";
    public static final String MOD_NAME = "Cool Attributes";
    public static final String VERSION = "1.0-SNAPSHOT";
    static Logger logger = LogManager.getLogger(MOD_ID);

    public static final IAttribute lifeStealPercentage = new RangedAttribute(null, MOD_ID + ".lifeStealPercentage", 0.0D, 0.0D, 1.0D);
    public static final IAttribute outOfWorldPercentage = new RangedAttribute(null, MOD_ID + ".outOfWorldPercentage", 0.0D, 0.0D, 1.0D);

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static CoolAttributes INSTANCE;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {

    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {

    }

    /**
     * Forge will automatically look up and bind blocks to the fields in this class
     * based on their registry name.
     */
    @GameRegistry.ObjectHolder(MOD_ID)
    public static class Blocks {
      /*
          public static final MySpecialBlock mySpecialBlock = null; // placeholder for special block below
      */
    }

    /**
     * Forge will automatically look up and bind items to the fields in this class
     * based on their registry name.
     */
    @GameRegistry.ObjectHolder(MOD_ID)
    public static class Items {
      /*
          public static final ItemBlock mySpecialBlock = null; // itemblock for the block above
          public static final MySpecialItem mySpecialItem = null; // placeholder for special item below
      */
    }

    /**
     * This is a special class that listens to registry events, to allow creation of mod blocks and items at the proper time.
     */
    @Mod.EventBusSubscriber
    public static class ObjectRegistryHandler {
        /**
         * Listen for the register event for creating custom items
         */
        @SubscribeEvent
        public static void addItems(RegistryEvent.Register<Item> event) {
           /*
             event.getRegistry().register(new ItemBlock(Blocks.myBlock).setRegistryName(MOD_ID, "myBlock"));
             event.getRegistry().register(new MySpecialItem().setRegistryName(MOD_ID, "mySpecialItem"));
            */
        }

        /**
         * Listen for the register event for creating custom blocks
         */
        @SubscribeEvent
        public static void addBlocks(RegistryEvent.Register<Block> event) {
           /*
             event.getRegistry().register(new MySpecialBlock().setRegistryName(MOD_ID, "mySpecialBlock"));
            */
        }

        @SubscribeEvent
        public static void entityConstructing(EntityEvent.EntityConstructing event){
            Entity entity = event.getEntity();
            if (entity instanceof EntityPlayer) {
                ((EntityPlayer) entity).getAttributeMap().registerAttribute(lifeStealPercentage);
                ((EntityPlayer) entity).getAttributeMap().registerAttribute(outOfWorldPercentage);
                logger.warn("hi");
            }
        }

        @SubscribeEvent
        public static void entityDamaged(LivingDamageEvent event) {
            float damage = event.getAmount();
            Entity src = event.getSource().getImmediateSource();
            logger.debug(damage + " " + event.getSource().getDamageType());
            if (src instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) src;
                logger.debug(src.getName() + " dealt " + damage);
                float healPercent = (float) player.getAttributeMap().getAttributeInstance(lifeStealPercentage).getAttributeValue();
                player.heal(damage * healPercent);
            }

        }

        @SubscribeEvent
        public static void entityAttacked(LivingAttackEvent event) {
            float damage = event.getAmount();
            Entity entity = event.getEntity();
            Entity src = event.getSource().getImmediateSource();
            if (!(entity instanceof EntityLivingBase)) return;
            EntityLivingBase mob = (EntityLivingBase) entity;
            if (src instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) src;
                float damagePercent = (float) player.getAttributeMap().getAttributeInstance(outOfWorldPercentage).getAttributeValue();
                logger.debug(damage + ", " + damagePercent + ", " + damage * damagePercent);
                mob.attackEntityFrom(DamageSource.OUT_OF_WORLD, damage * damagePercent);
            }
        }
    }
    /* EXAMPLE ITEM AND BLOCK - you probably want these in separate files
    public static class MySpecialItem extends Item {

    }

    public static class MySpecialBlock extends Block {

    }
    */
}
