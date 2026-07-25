package com.boatmod;

import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
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
}
