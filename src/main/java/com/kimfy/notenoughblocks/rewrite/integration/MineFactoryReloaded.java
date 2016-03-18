package com.kimfy.notenoughblocks.rewrite.integration;

import com.kimfy.notenoughblocks.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;

/**
 * All interactions between NEB -> MinefactoryReloaded
 */
public class MineFactoryReloaded implements Serializable
{
    protected static final transient Logger logger = LogManager.getLogger(Constants.MOD_NAME + ":MFR");
    public static final transient String MOD_ID = "MineFactoryReloaded";
    
    /** True if it can be dropped from Mining Laser **/
    protected boolean registerLaserOre = false;
}
