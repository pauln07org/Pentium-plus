 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.modules.render.search;

import pauln07.pentiumplus.gui.GuiTheme;
import pauln07.pentiumplus.gui.WindowScreen;
import pauln07.pentiumplus.renderer.ShapeMode;
import pauln07.pentiumplus.settings.*;
import pauln07.pentiumplus.utils.render.color.SettingColor;
import net.minecraft.block.Block;

public class SBlockDataScreen extends WindowScreen {
    private final SBlockData blockData;
    private final Block block;
    private final BlockDataSetting<SBlockData> setting;

    public SBlockDataScreen(GuiTheme theme, SBlockData blockData, Block block, BlockDataSetting<SBlockData> setting) {
        super(theme, "Configure Block");

        this.blockData = blockData;
        this.block = block;
        this.setting = setting;
    }

    @Override
    public void initWidgets() {
        Settings settings = new Settings();
        SettingGroup sgGeneral = settings.getDefaultGroup();
        SettingGroup sgTracer = settings.createGroup("Tracer");

        sgGeneral.add(new EnumSetting.Builder<ShapeMode>()
            .name("shape-mode")
            .description("How the shape is rendered.")
            .defaultValue(ShapeMode.Lines)
            .onModuleActivated(shapeModeSetting -> shapeModeSetting.set(blockData.shapeMode))
            .onChanged(shapeMode -> {
                blockData.shapeMode = shapeMode;
                changed(blockData, block, setting);
            })
            .build()
        );

        sgGeneral.add(new ColorSetting.Builder()
            .name("line-color")
            .description("Color of lines.")
            .defaultValue(new SettingColor(0, 255, 200))
            .onModuleActivated(settingColorSetting -> settingColorSetting.set(blockData.lineColor))
            .onChanged(settingColor -> {
                blockData.lineColor.set(settingColor);
                changed(blockData, block, setting);
            })
            .build()
        );

        sgGeneral.add(new ColorSetting.Builder()
            .name("side-color")
            .description("Color of sides.")
            .defaultValue(new SettingColor(0, 255, 200, 25))
            .onModuleActivated(settingColorSetting -> settingColorSetting.set(blockData.sideColor))
            .onChanged(settingColor -> {
                blockData.sideColor.set(settingColor);
                changed(blockData, block, setting);
            })
            .build()
        );

        sgTracer.add(new BoolSetting.Builder()
            .name("tracer")
            .description("If tracer line is allowed to this block.")
            .defaultValue(true)
            .onModuleActivated(booleanSetting -> booleanSetting.set(blockData.tracer))
            .onChanged(aBoolean -> {
                blockData.tracer = aBoolean;
                changed(blockData, block, setting);
            })
            .build()
        );

        sgTracer.add(new ColorSetting.Builder()
            .name("tracer-color")
            .description("Color of tracer line.")
            .defaultValue(new SettingColor(0, 255, 200, 125))
            .onModuleActivated(settingColorSetting -> settingColorSetting.set(blockData.tracerColor))
            .onChanged(settingColor -> {
                blockData.tracerColor = settingColor;
                changed(blockData, block, setting);
            })
            .build()
        );

        settings.onActivated();
        add(theme.settings(settings)).expandX();
    }

    private void changed(SBlockData blockData, Block block, BlockDataSetting<SBlockData> setting) {
        if (!blockData.isChanged() && block != null && setting != null) {
            setting.get().put(block, blockData);
            setting.onChanged();
        }

        blockData.changed();
    }
}
