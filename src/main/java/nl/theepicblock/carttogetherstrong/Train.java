package nl.theepicblock.carttogetherstrong;

import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class Train {
    private ArrayList<Cart> linkedCarts = new ArrayList<>();

    public Train(Cart entity) {
        linkedCarts.add(entity);
    }

    public ArrayList<Cart> getLinkedCarts() {
        return linkedCarts;
    }

    public void addCart(Cart cart) {
        linkedCarts.add(cart);
    }

    public void addCarts(ArrayList<Cart> carts) {
        linkedCarts.addAll(carts);
    }

    public void removeCart(Cart cart) {
        linkedCarts.remove(cart);
    }

    public void mergeInto(Train train) {
        if (this == train) return;
        train.addCarts(linkedCarts);
        for (Cart cart : linkedCarts) {
            cart.setTrain(train);
        }
    }

    public void setVelocityForAll(Vec3d v) {
        for (Cart cart : linkedCarts) {
            cart.setVelocityRaw(v);
        }
    }

    public void addVelocityForAll(Vec3d v) {
        for (Cart cart : linkedCarts) {
            cart.addVelocityRaw(v);
        }
    }
}
