package com.mcswainsoftware.rsbot;

import org.powerbot.script.rt6.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;

public abstract class Task extends ClientAccessor {

    public Task(ClientContext ctx) {
        super(ctx);
    }

    public abstract boolean activate();
    public abstract void execute();
    protected boolean failed = false;

    public boolean getFailed()
    {
        return failed;
    }

    protected boolean playerIsIdle()
    {
        if(ctx.players.local().animation() != -1 || ctx.players.local().inMotion() || ctx.players.local().interacting().valid()) return false;
        return true;
    }
}