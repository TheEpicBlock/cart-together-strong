package nl.theepicblock.carttogetherstrong.mixin;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import nl.theepicblock.carttogetherstrong.Cart;
import nl.theepicblock.carttogetherstrong.Train;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends Entity implements Cart {
    @Shadow public abstract int getDamageWobbleTicks();

    @Shadow public abstract void setDamageWobbleTicks(int wobbleTicks);

    @Shadow public abstract float getDamageWobbleStrength();

    @Shadow public abstract void setDamageWobbleStrength(float f);

    @Shadow private int clientInterpolationSteps;
    @Shadow private double clientX;
    @Shadow private double clientY;
    @Shadow private double clientZ;
    @Shadow private double clientYaw;
    @Shadow private double clientPitch;

    @Shadow protected abstract void moveOnRail(BlockPos pos, BlockState state);

    @Shadow public abstract void onActivatorRail(int x, int y, int z, boolean powered);

    @Shadow protected abstract void moveOffRail();

    @Unique private Train train = new Train(this);

    public AbstractMinecartEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "pushAwayFrom(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    public void collisionInject(Entity entity, CallbackInfo ci) {
        if (entity instanceof Cart) {
            Cart minecart = (Cart)entity;

            if (minecart.getTrain() == this.getTrain()) {
                ci.cancel(); //no collisions inbetween carts of a train
            }
            if (minecart.getMinecartType() == AbstractMinecartEntity.Type.HOPPER) {
                //I'm not going to mess up anyone's hopper minecarts
                return;
            }

            minecart.merge(this);
        }
    }

    @Override
    public void remove() {
        super.remove();
        train.removeCart(this);
    }

    @Override
    public void setTrain(Train t) {
        train = t;
    }

    @Override
    public Train getTrain() {
        return train;
    }

    @Override
    public void setVelocity(Vec3d velocity) {
        train.setVelocityForAll(velocity);
    }

    @Override
    public void setVelocityRaw(Vec3d v) {
        super.setVelocity(v);
    }

    @Override
    public void addVelocityRaw(Vec3d v) {
        setVelocityRaw(getVelocity().add(v));
    }
}
