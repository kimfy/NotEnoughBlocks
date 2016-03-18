package com.kimfy.notenoughblocks.rewrite.integration;

import com.kimfy.notenoughblocks.util.Constants;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;

@Getter
public class NotEnoughItems implements Serializable
{
    public static final transient String MOD_ID = "NotEnoughItems";
    protected static final transient Logger logger = LogManager.getLogger(Constants.MOD_NAME + ":" + MOD_ID);

    /**
     * True if the block or item should be hidden from the NEI menu. Used for full slabs, doors
     **/
    protected boolean hide = false;

    public NotEnoughItems setHidden(boolean hide)
    {
        this.hide = hide;
        return this;
    }
}