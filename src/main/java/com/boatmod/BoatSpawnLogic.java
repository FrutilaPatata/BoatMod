package com.boatmod;

import net.minecraft.block.Blocks;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.block.enums.RailShape;
import net.minecraft.world.World;

public class BoatSpawnLogic {
    public static void spawnBigBoat(ServerPlayerEntity player) {
        World world = player.world;
        Direction facing = player.getHorizontalFacing();

        Vec3d front = player.getPos().add(facing.getOffsetX() * 3.0, 0.0, facing.getOffsetZ() * 3.0);
        double x = front.x;
        double y = front.y;
        double z = front.z;

        MinecartEntity cart1 = new MinecartEntity(world, x, y, z);
        MinecartEntity cart2 = new MinecartEntity(world, x, y + 0.875, z);
        BoatEntity boat = new BoatEntity(world, x, y + 1.75, z);

        float yaw = facing.asRotation();
        cart1.setYaw(yaw);
        cart2.setYaw(yaw);
        boat.setYaw(yaw);

        world.spawnEntity(cart1);
        world.spawnEntity(cart2);
        world.spawnEntity(boat);
    }

    public static void spawnSmallBoat(ServerPlayerEntity player) {
        World world = player.world;
        Direction facing = player.getHorizontalFacing();

        Vec3d front = player.getPos().add(facing.getOffsetX() * 3.0, 0.0, facing.getOffsetZ() * 3.0);
        BlockPos railPos = new BlockPos((int) Math.floor(front.x), (int) Math.floor(front.y), (int) Math.floor(front.z));

        RailShape shape;
        switch (facing) {
            case NORTH: shape = RailShape.ASCENDING_NORTH; break;
            case SOUTH: shape = RailShape.ASCENDING_SOUTH; break;
            case EAST:  shape = RailShape.ASCENDING_EAST;  break;
            case WEST:  shape = RailShape.ASCENDING_WEST;  break;
            default:    shape = RailShape.ASCENDING_NORTH; break;
        }

        BlockState railState = Blocks.POWERED_RAIL.getDefaultState()
            .with(PoweredRailBlock.SHAPE, shape)
            .with(PoweredRailBlock.POWERED, true);
        world.setBlockState(railPos, railState);

        double cx = railPos.getX() + 0.5;
        double cy = railPos.getY() + 0.5;
        double cz = railPos.getZ() + 0.5;

        MinecartEntity cart = new MinecartEntity(world, cx, cy, cz);
        float yaw = facing.asRotation();
        cart.setYaw(yaw);
        world.spawnEntity(cart);

        BoatEntity boat = new BoatEntity(world, cx, cy + 0.6, cz);
        boat.setYaw(yaw);
        world.spawnEntity(boat);
        boat.startRiding(cart, true);

        Vec3i vec = facing.getVector();
        cart.setVelocity(vec.getX() * 0.2, 0.0, vec.getZ() * 0.2);
    }
}
