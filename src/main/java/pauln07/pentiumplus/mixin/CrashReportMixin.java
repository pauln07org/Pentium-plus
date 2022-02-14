 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.mixin;

import pauln07.pentiumplus.PentiumPlus;
import pauln07.pentiumplus.systems.modules.AllowedModules;
import pauln07.pentiumplus.systems.modules.Category;
import pauln07.pentiumplus.systems.modules.Module;
import pauln07.pentiumplus.systems.modules.Modules;
import net.minecraft.util.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CrashReport.class)
public class CrashReportMixin {
    @Inject(method = "addStackTrace", at = @At("TAIL"))
    private void onAddStackTrace(StringBuilder sb, CallbackInfo info) {
        if (Modules.get() != null) {
            sb.append("\n\n");
            sb.append("-- Pentium plus --\n");
            sb.append("Version: ").append(PentiumPlus.VERSION).append("\n");

            if (!PentiumPlus.DEV_BUILD.isEmpty()) {
                sb.append("Dev Build: ").append(PentiumPlus.DEV_BUILD).append("\n");
            }

            for (Category category : Modules.loopCategories()) {
                List<Module> modules = Modules.get().getGroup(category);
                boolean active = false;

                for (Module module : modules) {
                	if (AllowedModules.isValid(module)) {
                		if (module != null && module.isActive()) {
                			active = true;
                			break;
                		}
                		
                	}
                }

                if (active) {
                    sb.append("\n");
                    sb.append("[").append(category).append("]:").append("\n");

                    for (Module module : modules) {
                    	if (AllowedModules.isValid(module)) {
                    		if (module != null && module.isActive()) {
                    			sb.append(module.name).append("\n");
                    		}
                    		
                    	}
                    }
                }
            }
        }
    }
}
