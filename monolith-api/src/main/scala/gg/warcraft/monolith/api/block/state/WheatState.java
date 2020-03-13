package gg.warcraft.monolith.api.block.state;

import gg.warcraft.monolith.api.block.BlockState;

public enum WheatState implements BlockState {
    AGE_0,
    AGE_1,
    AGE_2,
    AGE_3,
    AGE_4,
    AGE_5,
    AGE_6,
    AGE_7;

    private static final WheatState[] finalValues = values();

    public static WheatState valueOf(int data) {
        return finalValues[data];
    }

    @Override
    public int toInt() {
        return ordinal();
    }
}