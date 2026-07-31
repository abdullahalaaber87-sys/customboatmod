package com.example.boatmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Vec3d;

public class CustomBoatClient implements ClientModInitializer {

    private static final double FLY_SPEED = 0.4;
    private static final double MAX_SPEED = 1.2;

    public static boolean flyEnabled = false;

    private static KeyBinding guiKey;

    @Override
    public void onInitializeClient() {

        // تسجيل زر Right Shift - متوافق مع 1.21.11
        guiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.customboatmod.gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "key.categories.misc"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null || client.world == null) return;

            // فتح/إغلاق القائمة
            while (guiKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new ClickGuiScreen());
                } else if (client.currentScreen instanceof ClickGuiScreen) {
                    client.setScreen(null);
                }
            }

            if (!flyEnabled) return;

            if (!(client.player.getVehicle() instanceof BoatEntity boat)) return;

            Vec3d vel = boat.getVelocity();

            if (client.options.jumpKey.isPressed()) {
                double newX = clamp(vel.x * 1.05, -MAX_SPEED, MAX_SPEED);
                double newZ = clamp(vel.z * 1.05, -MAX_SPEED, MAX_SPEED);
                boat.setVelocity(new Vec3d(newX, FLY_SPEED, newZ));
            } else if (vel.y > 0) {
                boat.setVelocity(vel.x, 0, vel.z);
            }
        });
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
