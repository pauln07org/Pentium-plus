 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus;

import pauln07.pentiumplus.addons.AddonManager;
import pauln07.pentiumplus.addons.MeteorAddon;
import pauln07.pentiumplus.events.game.OpenScreenEvent;
import pauln07.pentiumplus.events.meteor.KeyEvent;
import pauln07.pentiumplus.events.meteor.MouseButtonEvent;
import pauln07.pentiumplus.events.world.TickEvent;
import pauln07.pentiumplus.gui.GuiThemes;
import pauln07.pentiumplus.gui.WidgetScreen;
import pauln07.pentiumplus.gui.tabs.Tabs;
import pauln07.pentiumplus.systems.Systems;
import pauln07.pentiumplus.systems.config.Config;
import pauln07.pentiumplus.systems.modules.Categories;
import pauln07.pentiumplus.systems.modules.Modules;
import pauln07.pentiumplus.systems.modules.misc.DiscordPresence;
import pauln07.pentiumplus.utils.Init;
import pauln07.pentiumplus.utils.InitStage;
import pauln07.pentiumplus.utils.Utils;
import pauln07.pentiumplus.utils.misc.Version;
import pauln07.pentiumplus.utils.misc.input.KeyAction;
import pauln07.pentiumplus.utils.misc.input.KeyBinds;
import pauln07.pentiumplus.utils.network.OnlinePlayers;
import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import meteordevelopment.orbit.IEventBus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class PentiumPlus implements ClientModInitializer {
    public static final String MOD_ID = "meteor-client";
    private static final ModMetadata MOD_META = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata();
    public final static Version VERSION;
    public final static String DEV_BUILD;

    public static MinecraftClient mc;
    public static PentiumPlus INSTANCE;
    public static final IEventBus EVENT_BUS = new EventBus();
    public static final File FOLDER = new File(FabricLoader.getInstance().getGameDir().toString(), MOD_ID);
    public static final Logger LOG = LogManager.getLogger();

    @Override
    public void onInitializeClient() {
        if (INSTANCE == null) {
            INSTANCE = this;
            return;
        }

        LOG.info("Initializing Pentium plus");

        // Global minecraft client accessor
        mc = MinecraftClient.getInstance();

        // Register event handlers
        EVENT_BUS.registerLambdaFactory("pauln07.pentiumplus", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));

        // Pre-load
        Systems.addPreLoadTask(() -> {
            if (!FOLDER.exists()) {
                FOLDER.getParentFile().mkdirs();
                FOLDER.mkdir();
                Modules.get().get(DiscordPresence.class).toggle();
            }
        });

        // Pre init
        init(InitStage.Pre);

        // Register module categories
        Categories.init();

        // Load systems
        Systems.init();

        EVENT_BUS.subscribe(this);

        AddonManager.ADDONS.forEach(MeteorAddon::onInitialize);
        Modules.get().sortModules();

        // Load saves
        Systems.load();

        // Post init
        init(InitStage.Post);

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            OnlinePlayers.leave();
            Systems.save();
            GuiThemes.save();
        }));
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.currentScreen == null && mc.getOverlay() == null && KeyBinds.OPEN_COMMANDS.wasPressed()) {
            mc.setScreen(new ChatScreen(Config.get().prefix.get()));
        }
    }

    @EventHandler
    private void onKey(KeyEvent event) {
        if (event.action == KeyAction.Press && KeyBinds.OPEN_GUI.matchesKey(event.key, 0)) {
            openGui();
        }
    }

    @EventHandler
    private void onMouseButton(MouseButtonEvent event) {
        if (event.action == KeyAction.Press && KeyBinds.OPEN_GUI.matchesMouse(event.button)) {
            openGui();
        }
    }

    private void openGui() {
        if (Utils.canOpenGui()) Tabs.get().get(0).openScreen(GuiThemes.get());
    }

    // Hide HUD

    private boolean wasWidgetScreen, wasHudHiddenRoot;

    @EventHandler(priority = EventPriority.LOWEST)
    private void onOpenScreen(OpenScreenEvent event) {
        boolean hideHud = GuiThemes.get().hideHUD();

        if (hideHud) {
            if (!wasWidgetScreen) wasHudHiddenRoot = mc.options.hudHidden;

            if (event.screen instanceof WidgetScreen) mc.options.hudHidden = true;
            else if (!wasHudHiddenRoot) mc.options.hudHidden = false;
        }

        wasWidgetScreen = event.screen instanceof WidgetScreen;
    }

    // Reflection initialisation

    private static void init(InitStage initStage) {
        Reflections reflections = new Reflections("pauln07.pentiumplus", Scanners.MethodsAnnotated);
        Set<Method> initTasks = reflections.getMethodsAnnotatedWith(Init.class);
        if (initTasks == null) return;
        Map<Class<?>, List<Method>> byClass = initTasks.stream()
            .collect(Collectors.groupingBy(Method::getDeclaringClass));
        Set<Method> left = new HashSet<>(initTasks);

        for (Method m; (m = left.stream().findAny().orElse(null)) != null;) {
            reflectInit(m, initStage, left, byClass);
        }
    }

    private static void reflectInit(Method task, InitStage initStage, Set<Method> left, Map<Class<?>, List<Method>> byClass) {
        left.remove(task);
        Init init = task.getAnnotation(Init.class);
        if (!init.stage().equals(initStage)) return;
        for (Class<?> clazz : init.dependencies()) {
            for (Method m : byClass.getOrDefault(clazz, Collections.emptyList())) {
                if (left.contains(m)) {
                    reflectInit(m, initStage, left, byClass);
                }
            }
        }
        try {
            task.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    static {
        String versionString = MOD_META.getVersion().getFriendlyString();
        if (versionString.contains("-")) versionString = versionString.split("-")[0];

        VERSION = new Version(versionString);
        DEV_BUILD = MOD_META.getCustomValue("meteor-client:devbuild").getAsString();
    }
}
