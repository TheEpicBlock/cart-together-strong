package nl.theepicblock.carttogetherstrong.mixin;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import nl.theepicblock.carttogetherstrong.Cart;
import nl.theepicblock.carttogetherstrong.Train;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends Entity implements Cart {

    @Shadow public abstract Direction getMovementDirection();

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
        train.setVelocityRelative(velocity, this.getMovingDirection());
    }

    @Override
    public Direction getMovingDirection() {
        Direction d = this.getMovementDirection();
        BlockState blockState = this.world.getBlockState(getBlockPos());
        if (AbstractRailBlock.isRail(blockState)) {
            AbstractRailBlock abstractRailBlock = (AbstractRailBlock)blockState.getBlock();
            RailShape railShape = (RailShape)blockState.get(abstractRailBlock.getShapeProperty());
            if (railShape == RailShape.NORTH_SOUTH) d = Direction.NORTH;
            if (railShape == RailShape.EAST_WEST) d = Direction.EAST;
        }
        Vec3d velocity = this.getVelocity();
        if (d == Direction.SOUTH || d == Direction.NORTH) {
            return velocity.z < 0 ? Direction.NORTH : Direction.SOUTH;
        } else {
            return velocity.x < 0 ? Direction.WEST : Direction.EAST;
        }
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
