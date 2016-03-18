package com.kimfy.notenoughblocks.rewrite.integration;

import com.kimfy.notenoughblocks.util.Constants;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;

public class ForgeMultipart implements Serializable
{
    public static final transient String MOD_ID = "ForgeMultipart";
    protected static final transient Logger logger = LogManager.getLogger(Constants.MOD_NAME + ":" + MOD_ID);
    
    /** TODO: Implement **/
    @Getter
    protected boolean microblock = true;
}
