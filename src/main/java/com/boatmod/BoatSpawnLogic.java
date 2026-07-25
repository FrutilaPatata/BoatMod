package com.boatmod;

import net.minecraft.block.Blocks;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.block.enums.RailShape;
import net.minecraft.world.World;

public class BoatSpawnLogic {
    public static void spawnBigBoat(ServerPlayerEntity player) {
        World world = player.world;
        Direction facing = player.getHorizontalFacing();

        BlockPos playerPos = player.getBlockPos();
        BlockPos spawnPos = playerPos.down(3).offset(facing);

        world.setBlockState(spawnPos, Blocks.RED_STAINED_GLASS.getDefaultState());

        MinecartEntity minecart1 = new MinecartEntity(EntityType.FURNACE_MINECART, world);
        minecart1.setPosition(spawnPos.getX() + 0.5, spawnPos.getY() + 1.0, spawnPos.getZ() + 0.5);
        world.spawnEntity(minecart1);

        MinecartEntity minecart2 = new MinecartEntity(EntityType.FURNACE_MINECART, world);
        minecart2.setPosition(spawnPos.getX() + 0.5, spawnPos.getY() + 1.0 + 0.6999999881, spawnPos.getZ() + 0.5);
        world.spawnEntity(minecart2);

        BoatEntity boat = new BoatEntity(world, spawnPos.getX() + 0.5,
                spawnPos.getY() + 1.0 + 0.6999999881 + 0.6999999881, spawnPos.getZ() + 0.5);
        world.spawnEntity(boat);
    }

    public static void spawnSmallBoat(ServerPlayerEntity player) {
        World world = player.world;
        Direction facing = player.getHorizontalFacing();

        BlockPos playerPos = player.getBlockPos();

        // Near support: one block below player level, one block forward.
        BlockPos nearSupportPos = playerPos.down(1).offset(facing);
        // Far support: same height as player level, two blocks forward.
        BlockPos farSupportPos = playerPos.offset(facing, 2);

        // Place the staircase support blocks.
        world.setBlockState(nearSupportPos, Blocks.STONE.getDefaultState());
        world.setBlockState(farSupportPos, Blocks.STONE.getDefaultState());

        BlockPos nearRailPos = nearSupportPos.up();
        BlockPos farRailPos = farSupportPos.up();

        RailShape ascendingShape;
        RailShape straightShape;
        switch (facing) {
            case NORTH:
                ascendingShape = RailShape.ASCENDING_NORTH;
                straightShape = RailShape.NORTH_SOUTH;
                break;
            case SOUTH:
                ascendingShape = RailShape.ASCENDING_SOUTH;
                straightShape = RailShape.NORTH_SOUTH;
                break;
            case EAST:
                ascendingShape = RailShape.ASCENDING_EAST;
                straightShape = RailShape.EAST_WEST;
                break;
            case WEST:
            default:
                ascendingShape = RailShape.ASCENDING_WEST;
                straightShape = RailShape.EAST_WEST;
                break;
        }

        // Lower rail: ascending powered rail, creates the slope.
        BlockState ascendingRailState = Blocks.POWERED_RAIL.getDefaultState()
                .with(PoweredRailBlock.SHAPE, ascendingShape)
                .with(PoweredRailBlock.POWERED, true);
        world.setBlockState(nearRailPos, ascendingRailState);

        // Upper rail: flat powered rail, continues the track at the top of the slope.
        BlockState straightRailState = Blocks.POWERED_RAIL.getDefaultState()
                .with(PoweredRailBlock.SHAPE, straightShape)
                .with(PoweredRailBlock.POWERED, true);
        world.setBlockState(farRailPos, straightRailState);

        // Spawn the minecart on the lower rail, resting near its surface (not block-centered).
        MinecartEntity cart = new MinecartEntity(world,
                nearRailPos.getX() + 0.5, nearRailPos.getY() + 0.1875, nearRailPos.getZ() + 0.5);
        world.spawnEntity(cart);

        // Spawn the boat at the cart's position and let startRiding handle mounted placement.
        BoatEntity boat = new BoatEntity(world,
                nearRailPos.getX() + 0.5, nearRailPos.getY() + 0.1875, nearRailPos.getZ() + 0.5);
        world.spawnEntity(boat);
        boat.startRiding(cart, true);
    }
}