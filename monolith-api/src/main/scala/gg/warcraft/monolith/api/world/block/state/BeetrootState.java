package gg.warcraft.monolith.api.world.block.state;

import gg.warcraft.monolith.api.world.block.BlockState;

public enum BeetrootState implements BlockState {
    AGE_0,
    AGE_1,
    AGE_2,
    AGE_3;

    private static final BeetrootState[] finalValues = values();

    public static BeetrootState valueOf(int data) {
        return finalValues[data];
    }

    @Override
    public int toInt() {
        return ordinal();
    }
}