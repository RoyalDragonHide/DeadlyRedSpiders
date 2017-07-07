package com.mcswainsoftware.rsbot;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GroundItem;
import org.powerbot.script.rt6.GroundItemQuery;

import java.util.concurrent.Callable;

public class Loot extends Task {
    public static final int GOLD_CHARM_ID = 12158;
    public static final int GREEN_CHARM_ID = 12159;
    public static final int CRIMSON_CHARM_ID = 12160;
    public static final int BLUE_CHARM_ID = 12163;
    //public static final int COINS_ID = 995;
    public static final int RED_SPIDERS_EGGS_ID = 223;

    public int goldCharmLooted, greenCharmLooted, crimsonCharmLooted, blueCharmLooted, redSpidersEggsCollected = 0;

    public Loot(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        if(lootAround() <= 0 || !playerIsIdle() || !inventoryIsSuitable()) {
            return false;
        }
        return true;
    }

    @Override
    public void execute() {
        for(int i=0; i<lootAround(); i++) {
            final GroundItem groundItemGoldCharm = ctx.groundItems.select().id(GOLD_CHARM_ID).nearest().poll();
            if((inventoryIsSuitable() && ctx.backpack.select().id(GOLD_CHARM_ID).count() > 0) && groundItemGoldCharm.interact("Take", groundItemGoldCharm.name()) & groundItemGoldCharm.name().equals("Gold charm")) {
                goldCharmLooted++;
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !groundItemGoldCharm.valid();
                    }
                }, 200, 25);
            }

            final GroundItem groundItemGreenCharm = ctx.groundItems.select().id(GREEN_CHARM_ID).nearest().poll();
            if((inventoryIsSuitable() && ctx.backpack.select().id(GREEN_CHARM_ID).count() > 0) && groundItemGreenCharm.interact("Take", groundItemGreenCharm.name()) & groundItemGreenCharm.name().equals("Green charm")) {
                greenCharmLooted++;
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !groundItemGreenCharm.valid();
                    }
                }, 200, 25);
            }

            final GroundItem groundItemCrimsonCharm = ctx.groundItems.select().id(CRIMSON_CHARM_ID).nearest().poll();
            if((inventoryIsSuitable() && ctx.backpack.select().id(CRIMSON_CHARM_ID).count() > 0) && groundItemCrimsonCharm.interact("Take", groundItemCrimsonCharm.name()) & groundItemCrimsonCharm.name().equals("Crimson charm")) {
                crimsonCharmLooted++;
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !groundItemCrimsonCharm.valid();
                    }
                }, 200, 25);
            }

            final GroundItem groundItemBlueCharm = ctx.groundItems.select().id(BLUE_CHARM_ID).nearest().poll();
            if((inventoryIsSuitable() && ctx.backpack.select().id(BLUE_CHARM_ID).count() > 0) && groundItemBlueCharm.interact("Take", groundItemBlueCharm.name()) & groundItemBlueCharm.name().equals("Blue charm")) {
                blueCharmLooted++;
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !groundItemBlueCharm.valid();
                    }
                }, 200, 25);
            }

            /*final GroundItem groundItemCoins = ctx.groundItems.select().id(COINS_ID).nearest().poll();
            if(groundItemCoins.interact("Take", groundItemCoins.name()) & groundItemCoins.name().equals("Coins")) {
                coinsCollected += groundItemCoins.stackSize();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !groundItemCoins.valid();
                    }
                }, 200, 25);
            }*/

            final GroundItem groundItemRedSpidersEggs = ctx.groundItems.select().id(RED_SPIDERS_EGGS_ID).nearest().poll();
            if(ctx.backpack.select().count()<27 && groundItemRedSpidersEggs.interact("Take", groundItemRedSpidersEggs.name()) & groundItemRedSpidersEggs.name().equals("Red spiders' eggs")) {
                redSpidersEggsCollected++;
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !groundItemRedSpidersEggs.valid();
                    }
                }, 200, 25);
            }
        }
    }

    private int lootAround() {
        GroundItemQuery<GroundItem> items = ctx.groundItems.select().id(new int[]{GOLD_CHARM_ID, GREEN_CHARM_ID, CRIMSON_CHARM_ID, BLUE_CHARM_ID, RED_SPIDERS_EGGS_ID}).nearest().within(6.0);
        if(items.peek().valid()) return items.count();
        return -1;
    }


    private boolean inventoryIsSuitable()
    {
        if(ctx.backpack.select().count()<27 || ctx.backpack.select().id(new int[]{GOLD_CHARM_ID, GREEN_CHARM_ID, CRIMSON_CHARM_ID, BLUE_CHARM_ID}).count() > 0) return true;
        return false;
    }
}
