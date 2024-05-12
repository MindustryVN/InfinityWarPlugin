package infinitywar;

import arc.Events;
import arc.util.*;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.game.EventType.WorldLoadEndEvent;
import mindustry.mod.*;
import mindustry.world.consumers.ConsumeItems;

public class InfinityWarPlugin extends Plugin {
    private volatile boolean isFilling = false;

    @Override
    public void init() {
        Events.on(WorldLoadEndEvent.class, (e) -> {
            Timer.schedule(() -> {
                if (isFilling)
                    return;

                isFilling = true;

                for (var tile : Vars.world.tiles) {
                    var build = tile.build;
                    var block = tile.block();

                    if (build == null || block == null)
                        continue;

                    if (build.items == null)
                        continue;

                    for (var consumer : block.consumers) {
                        if (consumer instanceof ConsumeItems consumeItems) {
                            if (block.name.equals(Blocks.thoriumReactor.name)) {
                                for (var stack : consumeItems.items) {
                                    if (build.items.get(stack.item) < block.itemCapacity) {
                                        build.items.add(stack.item, block.itemCapacity);
                                        return;
                                    }
                                }
                            }

                            for (var stack : consumeItems.items) {
                                if (build.items.get(stack.item) < block.itemCapacity) {
                                    build.items.add(stack.item, block.itemCapacity);
                                }
                            }
                        }
                    }
                }
                isFilling = false;
            }, 0, 1);
        });

    }

    @Override
    public void registerServerCommands(CommandHandler handler) {

    }

    @Override
    public void registerClientCommands(CommandHandler handler) {

    }
}
