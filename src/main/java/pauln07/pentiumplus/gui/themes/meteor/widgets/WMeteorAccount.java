 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.gui.themes.meteor.widgets;

import pauln07.pentiumplus.gui.WidgetScreen;
import pauln07.pentiumplus.gui.themes.meteor.MeteorWidget;
import pauln07.pentiumplus.gui.widgets.WAccount;
import pauln07.pentiumplus.systems.accounts.Account;
import pauln07.pentiumplus.utils.render.color.Color;

public class WMeteorAccount extends WAccount implements MeteorWidget {
    public WMeteorAccount(WidgetScreen screen, Account<?> account) {
        super(screen, account);
    }

    @Override
    protected Color loggedInColor() {
        return theme().loggedInColor.get();
    }

    @Override
    protected Color accountTypeColor() {
        return theme().textSecondaryColor.get();
    }
}
