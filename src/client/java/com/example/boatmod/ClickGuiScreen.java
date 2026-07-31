package com.example.boatmod;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClickGuiScreen extends Screen {

    private static final int PANEL_X = 20;
    private static final int PANEL_Y = 20;
    private static final int PANEL_WIDTH = 160;
    private static final int BUTTON_HEIGHT = 22;
    private static final int PADDING = 8;

    public ClickGuiScreen() {
        super(Text.literal("Custom Boat Mod"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // خلفية
        context.fillGradient(0, 0, this.width, this.height, 0xC0101010, 0xD0101010);

        int panelHeight = PADDING + BUTTON_HEIGHT + PADDING;

        // لوحة التحكم
        context.fill(PANEL_X, PANEL_Y, PANEL_X + PANEL_WIDTH, PANEL_Y + panelHeight, 0xCC1a1a2e);

        // شريط العنوان
        context.fill(PANEL_X, PANEL_Y, PANEL_X + PANEL_WIDTH, PANEL_Y + 14, 0xFF16213e);
        context.drawTextWithShadow(textRenderer, "Boat Mod", PANEL_X + 4, PANEL_Y + 3, 0xFFFFFF);

        // زر الطيران
        boolean enabled = CustomBoatClient.flyEnabled;
        int btnY = PANEL_Y + 14 + PADDING / 2;
        int btnColor = enabled ? 0xFF2ecc71 : 0xFFe74c3c;
        context.fill(PANEL_X + PADDING, btnY, PANEL_X + PANEL_WIDTH - PADDING, btnY + BUTTON_HEIGHT, btnColor);

        String btnText = enabled ? "Boat Fly: ON" : "Boat Fly: OFF";
        int textX = PANEL_X + PADDING + ((PANEL_WIDTH - PADDING * 2) - textRenderer.getWidth(btnText)) / 2;
        context.drawTextWithShadow(textRenderer, btnText, textX, btnY + 7, 0xFFFFFF);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int btnY = PANEL_Y + 14 + PADDING / 2;
        int btnX1 = PANEL_X + PADDING;
        int btnX2 = PANEL_X + PANEL_WIDTH - PADDING;
        int btnY2 = btnY + BUTTON_HEIGHT;

        if (mouseX >= btnX1 && mouseX <= btnX2 && mouseY >= btnY && mouseY <= btnY2) {
            CustomBoatClient.flyEnabled = !CustomBoatClient.flyEnabled;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Right Shift يغلق القائمة
        if (keyCode == 344) {
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
