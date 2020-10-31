package nl.theepicblock.carttogetherstrong;

import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public interface Cart {
    void setTrain(Train t);

    AbstractMinecartEntity.Type getMinecartType();

    Train getTrain();

    default void merge(Cart t) {
        this.getTrain().mergeInto(t.getTrain());
    }

    Vec3d getPos();

    void setVelocityRaw(Vec3d v);

    void addVelocityRaw(Vec3d v);

    Direction getMovingDirection();
}
