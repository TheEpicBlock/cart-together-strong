package nl.theepicblock.carttogetherstrong;

import com.mojang.brigadier.Command;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class CTS implements ModInitializer {
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, b) -> {
			dispatcher.register(CommandManager.literal("train")
					.then(CommandManager.literal("inspect").then(CommandManager.argument("cart", EntityArgumentType.entity()).executes((context) -> {
						Entity entity = EntityArgumentType.getEntity(context, "cart");
						if (entity instanceof Cart) {
							Train t = ((Cart)entity).getTrain();
							ServerPlayerEntity player = context.getSource().getPlayer();
							for (Cart cart : t.getLinkedCarts()) {
								player.networkHandler.sendPacket(new ParticleS2CPacket(new DustParticleEffect(0,1,0,1), true, cart.getPos().x, cart.getPos().y+1, cart.getPos().z, 0, 0, 0, 0, 0));
							}
							return Command.SINGLE_SUCCESS;
						}
						context.getSource().sendFeedback(Text.of("Entity is not of type minecart"), false);
						return 0;
					}))));
		});
	}
}
