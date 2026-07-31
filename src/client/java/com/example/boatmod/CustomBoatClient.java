package com.example.boatmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class CustomBoatClient implements ClientModInitializer {

    // سرعة الصعود وأقصى سرعة أفقية
    private static final double FLY_SPEED = 0.4;
    private static final double MAX_SPEED = 1.2;

    // حالة تفعيل الطيران (يتحكم فيها الـ GUI)
    public static boolean flyEnabled = false;

    // زر فتح القائمة (Right Shift)
    private static KeyBinding guiKey;

    @Override
    public void onInitializeClient() {

        // تسجيل زر Right Shift لفتح القائمة
        guiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.customboatmod.gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.customboatmod"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null || client.world == null) return;

            // فتح القائمة عند ضغط Right Shift
            while (guiKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new ClickGuiScreen());
                } else if (client.currentScreen instanceof ClickGuiScreen) {
                    client.setScreen(null);
                }
            }

            // تطبيق الطيران فقط إذا كان مفعّلاً من القائمة
            if (!flyEnabled) return;

            if (!(client.player.getVehicle() instanceof BoatEntity boat)) return;

            Vec3d vel = boat.getVelocity();

            if (client.options.jumpKey.isPressed()) {
                double newX = clamp(vel.x * 1.05, -MAX_SPEED, MAX_SPEED);
                double newZ = clamp(vel.z * 1.05, -MAX_SPEED, MAX_SPEED);

                boat.setVelocity(new Vec3d(newX, FLY_SPEED, newZ));
                boat.velocityDirty = true;

            } else if (vel.y > 0) {
                boat.setVelocity(vel.x, 0, vel.z);
                boat.velocityDirty = true;
            }
        });
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
