package nl.theepicblock.carttogetherstrong;

import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class RotationUtil {
    public static Vec3d rotate(Vec3d v, Direction from, Direction to) {
        //rotate FROM -> NORTH
        double northX;
        double northZ;
        switch (from) {
            case SOUTH:
                northX = -v.x;
                northZ = -v.z;
                break;
            case WEST:
                northX = -v.z;
                northZ = v.x;
                break;
            case EAST:
                northX = v.z;
                northZ = -v.x;
                break;
            default:
                northX = v.x;
                northZ = v.z;
        }

        //rotate NORTH -> TO
        double newX;
        double newZ;
        switch (to) {
            case SOUTH:
                newX = -northX;
                newZ = -northZ;
                break;
            case WEST:
                newX = northZ;
                newZ = -northX;
                break;
            case EAST:
                newX = -northZ;
                newZ = northX;
                break;
            default:
                newX = northX;
                newZ = northZ;
        }

        return new Vec3d(newX, v.y, newZ);
    }
}
