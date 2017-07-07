package com.mcswainsoftware.rsbot;

import org.powerbot.script.PaintListener;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.GeItem;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(name="DeadlyRedSpiders", description="P2P: Kills Deadly Red Spiders in Varrock Sewer, collects eggs and charms, uses regenerate", properties = "client = 6;")

public class DeadlyRedSpiders extends PollingScript<ClientContext> implements PaintListener {

    public static final Font TAHOMA_TITLE = new Font("Tahoma", Font.BOLD, 20);
    public static final Font TAHOMA_HEADER = new Font("Tahoma", Font.BOLD | Font.ITALIC, 16);
    public static final Font TAHOMA = new Font("Tahoma", Font.PLAIN, 12);
    public static final int RED_SPIDERS_EGGS_PRICE = new GeItem(Loot.RED_SPIDERS_EGGS_ID).price;

    private int constitutionXP = ctx.skills.experience(Constants.SKILLS_HITPOINTS);
    private int magicXP = ctx.skills.experience(Constants.SKILLS_MAGIC);
    private int attackXP = ctx.skills.experience(Constants.SKILLS_ATTACK);
    private int strengthXP = ctx.skills.experience(Constants.SKILLS_STRENGTH);
    private int defenseXP = ctx.skills.experience(Constants.SKILLS_DEFENSE);
    private int rangedXP = ctx.skills.experience(Constants.SKILLS_RANGE);

    private List<Task> taskList = new ArrayList<>();
    private Fight fight = new Fight(ctx);
    private Loot loot = new Loot(ctx);

    @Override
    public void start() {
        super.start();
        taskList.add(fight);
        taskList.add(loot);
    }

    @Override
    public void poll() {
        for (Task task : taskList) {
            if (task.activate()) {
                task.execute();
            }
        }
    }

    @Override
    public void repaint(Graphics graphics) {
        final Graphics2D g = (Graphics2D) graphics;

        int spiderKilled = fight.spidersKilled;

        int spidersHr = (int) ((spiderKilled * 3600000D) / getRuntime());
        int constitutionHr = (int) (((ctx.skills.experience(Constants.SKILLS_HITPOINTS)-constitutionXP) * 3600000D) / getRuntime());
        int attackHr = (int) (((ctx.skills.experience(Constants.SKILLS_ATTACK)-attackXP) * 3600000D) / getRuntime());
        int defenseHr = (int) (((ctx.skills.experience(Constants.SKILLS_DEFENSE)-defenseXP) * 3600000D) / getRuntime());
        int strengthHr = (int) (((ctx.skills.experience(Constants.SKILLS_STRENGTH)-strengthXP) * 3600000D) / getRuntime());
        int rangedHr = (int) (((ctx.skills.experience(Constants.SKILLS_RANGE)-rangedXP) * 3600000D) / getRuntime());
        int magicHr = (int) (((ctx.skills.experience(Constants.SKILLS_MAGIC)-magicXP) * 3600000D) / getRuntime());


        g.setColor(new Color(0f,0f,0f,.75f));
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        g.setComposite(alphaComposite);
        g.fillRect(0, 0, 435, 320);
        g.setColor(Color.WHITE);

        g.setFont(TAHOMA_TITLE);
        g.drawString("DeadlyRedSpiders by RoyalDragonHide", 10, 20);

        g.setFont(TAHOMA_HEADER);
        g.drawString("Bot Stats:", 10, 50);

        g.setFont(TAHOMA);
        g.drawString(String.format("Spiders Killed: %,d (%,d /Hr)", spiderKilled, spidersHr), 10, 65);

        int spacing = 90;
        int spacingIncrement = 15;

        if(spiderKilled > 0) {
            g.setFont(TAHOMA_HEADER);
            g.drawString("Experience Earned:", 10, spacing);
            spacing+=spacingIncrement;
        }


        g.setFont(TAHOMA);
        if(constitutionHr != 0) {
            g.drawString(String.format("Constitution XP: %,d/Hr", constitutionHr), 10, spacing);
            spacing += spacingIncrement;
        }
        if(attackHr != 0) {
            g.drawString(String.format("Attack XP: %,d/Hr", attackHr), 10, spacing);
            spacing += spacingIncrement;
        }
        if(strengthHr != 0) {
            g.drawString(String.format("Strength XP: %,d/Hr", strengthHr), 10, spacing);
            spacing += spacingIncrement;
        }
        if(rangedHr != 0) {
            g.drawString(String.format("Ranged XP: %,d/Hr", rangedHr), 10, spacing);
            spacing += spacingIncrement;
        }
        if(magicHr != 0) {
            g.drawString(String.format("Magic XP: %,d/Hr", magicHr), 10, spacing);
            spacing += spacingIncrement;
        }
        if(defenseHr != 0) {
            g.drawString(String.format("Defense XP: %,d/Hr", defenseHr), 10, spacing);
            spacing += spacingIncrement;
        }

        if(loot.goldCharmLooted > 0 || loot.greenCharmLooted > 0 || loot.crimsonCharmLooted > 0 || loot.blueCharmLooted > 0 || loot.redSpidersEggsCollected > 0) {
            g.setFont(TAHOMA_HEADER);
            spacing += 10;
            g.drawString("Loot Collected:", 10, spacing);
            spacing+=spacingIncrement;
        }

        g.setFont(TAHOMA);
        if(loot.goldCharmLooted > 0) {
            g.drawString(String.format("Gold Charms Looted: %d", loot.goldCharmLooted), 10, spacing);
            spacing += spacingIncrement;
        }
        if(loot.greenCharmLooted > 0) {
            g.drawString(String.format("Green Charms Looted: %d", loot.greenCharmLooted), 10, spacing);
            spacing += spacingIncrement;
        }
        if(loot.crimsonCharmLooted > 0) {
            g.drawString(String.format("Crimson Charms Looted: %d", loot.crimsonCharmLooted), 10, spacing);
            spacing += spacingIncrement;
        }
        if(loot.blueCharmLooted > 0) {
            g.drawString(String.format("Blue Charms Looted: %d", loot.blueCharmLooted), 10, spacing);
            spacing += spacingIncrement;
        }
        if(loot.redSpidersEggsCollected > 0) {
            g.drawString(String.format("Red Spiders' Eggs Looted: %d (%d gp)", loot.redSpidersEggsCollected, (loot.redSpidersEggsCollected*RED_SPIDERS_EGGS_PRICE)), 10, spacing);
        }
    }
}
