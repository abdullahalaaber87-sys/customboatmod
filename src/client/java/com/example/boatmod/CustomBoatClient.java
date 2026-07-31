package com.example.boatmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Vec3d;

public class CustomBoatClient implements ClientModInitializer {

    // سرعة الصعود وأقصى سرعة أفقية
    private static final double FLY_SPEED  = 0.4;
    private static final double MAX_SPEED  = 1.2;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null || client.world == null) return;

            // التأكد أن اللاعب راكب قارب
            if (!(client.player.getVehicle() instanceof BoatEntity boat)) return;

            Vec3d vel = boat.getVelocity();

            if (client.options.jumpKey.isPressed()) {
                // تسريع أفقي مع تصحيح الاتجاهات السالبة
                double newX = clamp(vel.x * 1.05, -MAX_SPEED, MAX_SPEED);
                double newZ = clamp(vel.z * 1.05, -MAX_SPEED, MAX_SPEED);

                boat.setVelocity(new Vec3d(newX, FLY_SPEED, newZ));

                // إشعار السيرفر بتغيّر السرعة
                boat.velocityDirty = true;

            } else if (vel.y > 0) {
                // إلغاء الزخم الصاعد عند ترك زر القفز
                boat.setVelocity(vel.x, 0, vel.z);
                boat.velocityDirty = true;
            }
        });
    }

    /**
     * تحديد قيمة بين حد أدنى وأقصى (يدعم الأرقام السالبة)
     */
    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
