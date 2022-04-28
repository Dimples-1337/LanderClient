package cn.Arctic.Module.modules.COMBAT;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.MoveUtils;
import cn.Arctic.Util.Chat.Helper;
import cn.Arctic.Util.Player.PlayerUtil;
import cn.Arctic.Util.Timer.TimerUtil;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

public class Regen extends Module {

	private Option<Boolean> hc = new Option<Boolean>("HealthCheck", false);
	private Numbers<Double> health = new Numbers<Double>("Health", 18.0, 0.0, 40.0, 1.0);
	private Numbers<Double> foodValue = new Numbers<Double>("Food", 18.0, 0.0, 20.0, 1.0);
	private Numbers<Double> speedValue = new Numbers<Double>("Speed", 100.0, 1.0, 100.0, 1.0);
	private Option<Boolean> noAirValue = new Option<Boolean>("NoAir", false);
	private Option<Boolean> potionEffectValue = new Option<Boolean>("PotionEffect", false);
	private Option<Boolean> moving = new Option<Boolean>("Moving", true);

	private boolean timerReseted;

	public Regen() {
		super("Regen", new String[] { "reg" }, ModuleType.Player);
		this.addValues(hc, health, foodValue, speedValue, noAirValue, potionEffectValue, moving);
	}

	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1.0F;
		super.onDisable();
	}

	@EventHandler
	private void onUpdate(final EventPreUpdate e) {
		TimerUtil timer = new TimerUtil();
		
//        if (timer.hasReached(300) && mc.player.fallDistance <= 2.0f && mc.player.getHealth() < mc.player.getMaxHealth() && mc.player.getFoodStats().getFoodLevel() >= 19 && mc.player.onGround) {
//            for (int i = 0; i < 13; ++i) {
//                if (Regen.mc.player.onGround) {
//                    Regen.mc.player.sendQueue.addToSendQueue(new C03PacketPlayer());
//                    timer.reset();
//                }
//            }
//        }
		
		
		if (timerReseted) {
			mc.timer.timerSpeed = 1F;
			timerReseted = false;
		}

		if ((!noAirValue.getValue() || mc.player.onGround) && !mc.player.capabilities.isCreativeMode
				&& mc.player.getFoodStats().getFoodLevel() > foodValue.getValue() && mc.player.isEntityAlive()) {

			if (potionEffectValue.getValue() && !mc.player.isPotionActive(Potion.regeneration))
				return;
			
			
//			TimerUtil timer = new TimerUtil();
			
//			for(Entity ent : mc.world.getLoadedEntityList()) {
//				if(ent instanceof EntityFireball) { 
//					EntityFireball fireball = (EntityFireball) ent;
//					if(timer.hasReached(200)) {
//						mc.getConnection().sendPacket(new C02PacketUseEntity(fireball, C02PacketUseEntity.Action.ATTACK));
//						timer.reset();
//					}
//				}
//			}
			
			
			
			

//			if (!mc.player.onGround)
//				return;

//			if(this.moving.getValue() && MoveUtils.isMoving())
//				return;

//			if(mc.player.isBurning() && !mc.player.isEating()) {
//				for (int i = 0; i <= 9; i++) {
//					mc.getConnection().sendPacket(new C03PacketPlayer(mc.player.onGround));
//				}
//			}
			if (mc.player.isUsingItem()) {
				if (mc.player.getItemInUseDuration() > 14) {
					for (int i = 0; i <= 20; i++) {
						mc.getConnection().sendPacket(new C03PacketPlayer(mc.player.onGround));
					}
					mc.playerController.onStoppedUsingItem(mc.player);
				}
			}
			
			timerReseted = true;

		}
	}

}
