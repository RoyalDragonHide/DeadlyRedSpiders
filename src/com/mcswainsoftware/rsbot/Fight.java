package com.mcswainsoftware.rsbot;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Npc;

import java.util.concurrent.Callable;

public class Fight extends Task {

    public static final int DEADLY_RED_SPIDER_IO = 63;
    public int spidersKilled = 0;
    private boolean active = true;
    private boolean isHealing = false;
    private boolean isGoingToHeal = false;
    private int lastAdrenaline = 0;

    private int getHealthPercent() {
        String healthString = ctx.widgets.widget(1430).component(6).component(7).text();
        if (healthString == "-1") {
            return -1;
        } else {
            String[] health = healthString.split("/");
            int healthPercent = -1;
            try {
                healthPercent = (int) (Float.parseFloat(health[0]) / Float.parseFloat(health[1]) * 100.0f);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return healthPercent;
        }
    }

    private int getAdrenalinePercent() {
        return Integer.parseInt(ctx.widgets.widget(1430).component(50).component(7).text().split("%")[0]);
    }

    public Fight(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() {
        int hitpointsXp = ctx.skills.experience(Constants.SKILLS_HITPOINTS);
        int startingHealth = getHealthPercent();

        if (isHealing || isGoingToHeal) {
            failed = false;
            if (lastAdrenaline > 0 && getAdrenalinePercent() <= 0) {
                isHealing = false;
                isGoingToHeal = false;
            }
            return;
        }
        lastAdrenaline = getAdrenalinePercent();
        final Npc spider = ctx.npcs.select().id(Fight.DEADLY_RED_SPIDER_IO).select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                return npc.interacting().equals(ctx.players.local()) || (!npc.interacting().valid() && npc.healthPercent() > 0);
            }
        }).nearest().poll();
        if (!spider.inViewport()) {
            ctx.movement.step(spider);
            ctx.camera.turnTo(spider);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return spider.inViewport();
                }
            }, 200, 20);
        }
        spider.interact("Attack");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return (spider.interacting().valid() && !spider.interacting().equals(ctx.players.local()) || spider.healthPercent() == 0 || playerIsIdle());
            }
        }, 1000, 35);
        if (startingHealth < 50 && getAdrenalinePercent() >= 70 && !isGoingToHeal) {
            ctx.widgets.widget(1430).component(8).click();
            isGoingToHeal = true;
        }
        if (ctx.skills.experience(Constants.SKILLS_HITPOINTS) > hitpointsXp) {
            spidersKilled++;
            failed = false;
        }
        if (startingHealth < 50 && getAdrenalinePercent() >= 80) {
            isHealing = true;
        } else {
            failed = false;
            return;
        }
        failed = true;
    }

    @Override
    public boolean activate() {
        if (!active || !spidersAround() || !playerIsIdle()) {
            return false;
        }
        return true;
    }

    private boolean spidersAround() {
        if (ctx.npcs.select().id(Fight.DEADLY_RED_SPIDER_IO).select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                return !npc.interacting().valid() && npc.healthPercent() > 0;
            }
        }).isEmpty()) {
            return false;
        }
        return true;
    }
}
