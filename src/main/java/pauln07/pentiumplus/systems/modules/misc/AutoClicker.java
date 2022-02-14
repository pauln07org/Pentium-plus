 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.modules.misc;

import pauln07.pentiumplus.events.world.TickEvent;
import pauln07.pentiumplus.settings.EnumSetting;
import pauln07.pentiumplus.settings.IntSetting;
import pauln07.pentiumplus.settings.Setting;
import pauln07.pentiumplus.settings.SettingGroup;
import pauln07.pentiumplus.systems.modules.Categories;
import pauln07.pentiumplus.systems.modules.Module;
import pauln07.pentiumplus.utils.Utils;
import meteordevelopment.orbit.EventHandler;

public class AutoClicker extends Module {
    public enum Mode {
        Hold,
        Press
    }

    public enum Button {
        Right,
        Left
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
            .name("mode")
            .description("The method of clicking.")
            .defaultValue(Mode.Press)
            .build()
    );

    private final Setting<Button> button = sgGeneral.add(new EnumSetting.Builder<Button>()
            .name("button")
            .description("Which button to press.")
            .defaultValue(Button.Right)
            .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
            .name("click-delay")
            .description("The amount of delay between clicks in ticks.")
            .defaultValue(2)
            .min(0)
            .sliderMax(60)
            .build()
    );

    private int timer;

    public AutoClicker() {
        super(Categories.Player, "auto-clicker", "Automatically clicks.");
    }

    @Override
    public void onActivate() {
        timer = 0;
        mc.options.keyAttack.setPressed(false);
        mc.options.keyUse.setPressed(false);
    }

    @Override
    public void onDeactivate() {
        mc.options.keyAttack.setPressed(false);
        mc.options.keyUse.setPressed(false);
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        switch (mode.get()) {
            case Hold:
                switch (button.get()) {
                    case Left:
                        mc.options.keyAttack.setPressed(true);
                        break;
                    case Right:
                        mc.options.keyUse.setPressed(true);
                        break;
                }
                break;
            case Press:
                timer++;
                if (!(delay.get() > timer)) {
                    switch (button.get()) {
                        case Left:
                            Utils.leftClick();
                            break;
                        case Right:
                            Utils.rightClick();
                            break;
                    }
                    timer = 0;
                }
                break;
        }
    }
}
