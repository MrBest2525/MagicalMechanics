package com.github.mrbestcreator.magicalmechanics.content.block.machine.frame;

import net.minecraft.core.Direction;

public enum FrameSlot {
    UP, DOWN, NORTH, SOUTH, EAST, WEST, CORE;
    
    public static FrameSlot fromDirection(Direction direction) {
        return switch (direction) {
            case UP -> UP;
            case DOWN -> DOWN;
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
        };
    }
}
