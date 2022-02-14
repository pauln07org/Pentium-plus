 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.modules.render.search;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import pauln07.pentiumplus.events.render.Render3DEvent;
import pauln07.pentiumplus.events.world.BlockUpdateEvent;
import pauln07.pentiumplus.events.world.ChunkDataEvent;
import pauln07.pentiumplus.events.world.TickEvent;
import pauln07.pentiumplus.renderer.ShapeMode;
import pauln07.pentiumplus.settings.*;
import pauln07.pentiumplus.systems.modules.Categories;
import pauln07.pentiumplus.systems.modules.Module;
import pauln07.pentiumplus.utils.Utils;
import pauln07.pentiumplus.utils.misc.UnorderedArrayList;
import pauln07.pentiumplus.utils.network.MeteorExecutor;
import pauln07.pentiumplus.utils.player.PlayerUtils;
import pauln07.pentiumplus.utils.render.color.RainbowColors;
import pauln07.pentiumplus.utils.render.color.SettingColor;
import pauln07.pentiumplus.utils.world.Dimension;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Search extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // General

    private final Setting<List<Block>> blocks = sgGeneral.add(new BlockListSetting.Builder()
        .name("blocks")
        .description("Blocks to search for.")
        .onChanged(blocks1 -> {
            if (isActive() && Utils.canUpdate()) onActivate();
        })
        .build()
    );

    private final Setting<SBlockData> defaultBlockConfig = sgGeneral.add(new GenericSetting.Builder<SBlockData>()
        .name("default-block-config")
        .description("Default block config.")
        .defaultValue(
            new SBlockData(
                ShapeMode.Lines,
                new SettingColor(0, 255, 200),
                new SettingColor(0, 255, 200, 25),
                true,
                new SettingColor(0, 255, 200, 125)
            )
        )
        .build()
    );

    private final Setting<Map<Block, SBlockData>> blockConfigs = sgGeneral.add(new BlockDataSetting.Builder<SBlockData>()
        .name("block-configs")
        .description("Config for each block.")
        .defaultData(defaultBlockConfig)
        .build()
    );

    private final Setting<Boolean> tracers = sgGeneral.add(new BoolSetting.Builder()
        .name("tracers")
        .description("Render tracer lines.")
        .defaultValue(false)
        .build()
    );

    private final BlockPos.Mutable blockPos = new BlockPos.Mutable();

    private final Long2ObjectMap<SChunk> chunks = new Long2ObjectOpenHashMap<>();
    private final List<SGroup> groups = new UnorderedArrayList<>();

    private Dimension lastDimension;

    public Search() {
        super(Categories.Render, "search", "Searches for specified blocks.");

        RainbowColors.register(this::onTickRainbow);
    }

    @Override
    public void onActivate() {
        synchronized (chunks) {
            chunks.clear();
            groups.clear();
        }

        for (Chunk chunk : Utils.chunks()) {
            searchChunk(chunk, null);
        }

        lastDimension = PlayerUtils.getDimension();
    }

    @Override
    public void onDeactivate() {
        synchronized (chunks) {
            chunks.clear();
            groups.clear();
        }
    }

    private void onTickRainbow() {
        if (!isActive()) return;

        defaultBlockConfig.get().tickRainbow();
        for (SBlockData blockData : blockConfigs.get().values()) blockData.tickRainbow();
    }

    SBlockData getBlockData(Block block) {
        SBlockData blockData = blockConfigs.get().get(block);
        return blockData == null ? defaultBlockConfig.get() : blockData;
    }

    private void updateChunk(int x, int z) {
        SChunk chunk = chunks.get(ChunkPos.toLong(x, z));
        if (chunk != null) chunk.update();
    }

    private void updateBlock(int x, int y, int z) {
        SChunk chunk = chunks.get(ChunkPos.toLong(x >> 4, z >> 4));
        if (chunk != null) chunk.update(x, y, z);
    }

    public SBlock getBlock(int x, int y, int z) {
        SChunk chunk = chunks.get(ChunkPos.toLong(x >> 4, z >> 4));
        return chunk == null ? null : chunk.get(x, y, z);
    }

    public SGroup newGroup(Block block) {
        synchronized (chunks) {
            SGroup group = new SGroup(block);
            groups.add(group);
            return group;
        }
    }

    public void removeGroup(SGroup group) {
        synchronized (chunks) {
            groups.remove(group);
        }
    }

    @EventHandler
    private void onChunkData(ChunkDataEvent event) {
        searchChunk(event.chunk, event);
    }

    private void searchChunk(Chunk chunk, ChunkDataEvent event) {
        MeteorExecutor.execute(() -> {
            if (!isActive()) return;
            SChunk schunk = SChunk.searchChunk(chunk, blocks.get());

            if (schunk.size() > 0) {
                synchronized (chunks) {
                    chunks.put(chunk.getPos().toLong(), schunk);
                    schunk.update();

                    // Update neighbour chunks
                    updateChunk(chunk.getPos().x - 1, chunk.getPos().z);
                    updateChunk(chunk.getPos().x + 1, chunk.getPos().z);
                    updateChunk(chunk.getPos().x, chunk.getPos().z - 1);
                    updateChunk(chunk.getPos().x, chunk.getPos().z + 1);
                }
            }

            if (event != null) ChunkDataEvent.returnChunkDataEvent(event);
        });
    }

    @EventHandler
    private void onBlockUpdate(BlockUpdateEvent event) {
        // Minecraft probably reuses the event.pos BlockPos instance because it causes problems when trying to use it inside another thread
        int bx = event.pos.getX();
        int by = event.pos.getY();
        int bz = event.pos.getZ();

        int chunkX = bx >> 4;
        int chunkZ = bz >> 4;
        long key = ChunkPos.toLong(chunkX, chunkZ);

        boolean added = blocks.get().contains(event.newState.getBlock()) && !blocks.get().contains(event.oldState.getBlock());
        boolean removed = !added && !blocks.get().contains(event.newState.getBlock()) && blocks.get().contains(event.oldState.getBlock());

        if (added || removed) {
            MeteorExecutor.execute(() -> {
                synchronized (chunks) {
                    SChunk chunk = chunks.get(key);

                    if (chunk == null) {
                        chunk = new SChunk(chunkX, chunkZ);
                        if (chunk.shouldBeDeleted()) return;

                        chunks.put(key, chunk);
                    }

                    blockPos.set(bx, by, bz);

                    if (added) chunk.add(blockPos);
                    else chunk.remove(blockPos);

                    // Update neighbour blocks
                    for (int x = -1; x < 2; x++) {
                        for (int z = -1; z < 2; z++) {
                            for (int y = -1; y < 2; y++) {
                                if (x == 0 && y == 0 && z == 0) continue;

                                updateBlock(bx + x, by + y, bz + z);
                            }
                        }
                    }
                }
            });
        }
    }

    @EventHandler
    private void onPostTick(TickEvent.Post event) {
        Dimension dimension = PlayerUtils.getDimension();

        if (lastDimension != dimension) onActivate();

        lastDimension = dimension;
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        synchronized (chunks) {
            for (Iterator<SChunk> it = chunks.values().iterator(); it.hasNext();) {
                SChunk chunk = it.next();

                if (chunk.shouldBeDeleted()) {
                    MeteorExecutor.execute(() -> {
                        for (SBlock block : chunk.blocks.values()) {
                            block.group.remove(block, false);
                            block.loaded = false;
                        }
                    });

                    it.remove();
                }
                else chunk.render(event);
            }

            if (tracers.get()) {
                for (Iterator<SGroup> it = groups.iterator(); it.hasNext();) {
                    SGroup group = it.next();

                    if (group.blocks.isEmpty()) it.remove();
                    else group.render(event);
                }
            }
        }
    }
}
