 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.gui.renderer;

import pauln07.pentiumplus.gui.utils.Cell;
import pauln07.pentiumplus.gui.widgets.WWidget;
import pauln07.pentiumplus.gui.widgets.containers.WContainer;
import pauln07.pentiumplus.renderer.DrawMode;
import pauln07.pentiumplus.renderer.Mesh;
import pauln07.pentiumplus.renderer.ShaderMesh;
import pauln07.pentiumplus.renderer.Shaders;
import pauln07.pentiumplus.utils.render.color.Color;
import net.minecraft.client.util.math.MatrixStack;

public class GuiDebugRenderer {
    private static final Color CELL_COLOR = new Color(25, 225, 25);
    private static final Color WIDGET_COLOR = new Color(25, 25, 225);

    private final Mesh mesh = new ShaderMesh(Shaders.POS_COLOR, DrawMode.Lines, Mesh.Attrib.Vec2, Mesh.Attrib.Color);

    public void render(WWidget widget, MatrixStack matrices) {
        if (widget == null) return;

        mesh.begin();

        renderWidget(widget);

        mesh.end();
        mesh.render(matrices);
    }

    private void renderWidget(WWidget widget) {
        lineBox(widget.x, widget.y, widget.width, widget.height, WIDGET_COLOR);

        if (widget instanceof WContainer) {
            for (Cell<?> cell : ((WContainer) widget).cells) {
                lineBox(cell.x, cell.y, cell.width, cell.height, CELL_COLOR);
                renderWidget(cell.widget());
            }
        }
    }

    private void lineBox(double x, double y, double width, double height, Color color) {
        line(x, y, x + width, y, color);
        line(x + width, y, x + width, y + height, color);
        line(x, y, x, y + height, color);
        line(x, y + height, x + width, y + height, color);
    }

    private void line(double x1, double y1, double x2, double y2, Color color) {
        mesh.line(
            mesh.vec2(x1, y1).color(color).next(),
            mesh.vec2(x2, y2).color(color).next()
        );
    }
}
