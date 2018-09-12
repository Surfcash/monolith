package gg.warcraft.monolith.app.world;

import gg.warcraft.monolith.api.world.Direction;
import gg.warcraft.monolith.api.world.DirectionUtils;
import org.joml.Vector3f;

public class DefaultDirectionUtils implements DirectionUtils {

    @Override
    public Direction getOpposite(Direction direction) {
        switch (direction) {
            case NORTH:
                return Direction.SOUTH;
            case EAST:
                return Direction.WEST;
            case SOUTH:
                return Direction.NORTH;
            case WEST:
                return Direction.EAST;
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            default:
                throw new IllegalArgumentException("Failed to return opposite direction for " + direction);
        }
    }

    @Override
    public int getRotation(Direction from, Direction to) {
        if (from == Direction.UP || from == Direction.DOWN ||
                to == Direction.UP || to == Direction.DOWN) {
            throw new IllegalArgumentException("Failed to return rotation from " + from + " to " + to);
        }
        int fromOrdinal = from.ordinal();
        int toOrdinal = to.ordinal();
        int ordinalDiff = toOrdinal - fromOrdinal;
        while (ordinalDiff < 0) {
            ordinalDiff += 4;
        }
        return ordinalDiff * 90;
    }

    @Override
    public Direction toDirection(float yaw) {
        if (yaw >= -45) {
            if (yaw <= 45) {
                return Direction.SOUTH;
            } else if (yaw <= 135) {
                return Direction.WEST;
            } else {
                return Direction.NORTH;
            }
        } else {
            if (yaw >= -135) {
                return Direction.EAST;
            } else {
                return Direction.NORTH;
            }
        }
    }

    @Override
    public Vector3f toVector(Direction direction) {
        switch (direction) {
            case NORTH:
                return new Vector3f(0, 0, -1);
            case EAST:
                return new Vector3f(1, 0, 0);
            case SOUTH:
                return new Vector3f(0, 0, 1);
            case WEST:
                return new Vector3f(-1, 0, 0);
            case UP:
                return new Vector3f(0, 1, 0);
            case DOWN:
                return new Vector3f(0, -1, 0);
            default:
                throw new IllegalArgumentException("Failed to return vector for " + direction);
        }
    }
}
