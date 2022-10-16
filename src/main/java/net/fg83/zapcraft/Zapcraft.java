package net.fg83.zapcraft;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.mcreator.minecraft.link.CurrentDevice;

import org.slf4j.Logger;

import static net.fg83.zapcraft.Zapcraft.MODID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MODID)
public class Zapcraft {
    public static final String MODID = "zapcraft";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static float oldHealth;
    public static boolean isDead = false;
    public Zapcraft() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    }
    @Mod.EventBusSubscriber
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onJoinWorld(PlayerEvent.PlayerLoggedInEvent event){
            oldHealth = event.getEntity().getHealth();
        }

        @SubscribeEvent
        public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event){
            oldHealth = event.getEntity().getHealth();
            isDead = false;
        }

        @SubscribeEvent
        public static void onPlayerDeath(LivingDeathEvent event){
            if (event.getEntity() instanceof Player){
                CurrentDevice.sendMessage( "30");
                LOGGER.info("SENDING DAMAGE: 30");
                isDead = true;
            }
        }

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event){
            if (event.phase == TickEvent.Phase.END) {
                int damage = 0;
                if (event.player.getHealth() < oldHealth){
                    if (!isDead) {
                        damage = Math.round(oldHealth - event.player.getHealth());
                        LOGGER.info("SENDING DAMAGE: " + damage);
                        CurrentDevice.sendMessage(Integer.toString(damage));
                        oldHealth = event.player.getHealth();
                    }
                }
                else if (event.player.getHealth() > oldHealth) {
                    oldHealth = event.player.getHealth();
                }
            }
        }
    }
}
