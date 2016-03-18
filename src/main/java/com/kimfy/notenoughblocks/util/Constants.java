package com.kimfy.notenoughblocks.util;

public class Constants
{
    public static final String MOD_ID = "notenoughblocks";
    public static final String MOD_NAME = "NotEnoughBlocks";
    public static final String MOD_VERSION = "1.0.0";
    public static final String LANG_DEFAULT = "en_US";
    public static final String CLIENT_PROXY = "com.kimfy.notenoughblocks.proxy.ClientProxy";
    public static final String COMMON_PROXY = "com.kimfy.notenoughblocks.proxy.CommonProxy";
    
    /** config/mod_id **/
    public static final String BASE_PATH= "config/" + MOD_ID + "/";
    /** BASE_PATH/resourcepacks/ **/
    public static final String RESOURCE_PACKS_PATH = BASE_PATH + "resourcepacks/";
    /** BASE_PATH/json/ **/
    public static final String JSON_PATH = BASE_PATH + "json/";
    /** resources/ **/
    public static final String RESOURCE_LOADER_BASE_PATH = "resources/";
    /** RESOURCE_LOADER_BASE_PATH/MOD_ID/textures/blocks/ **/
    public static final String RESOURCE_LOADER_NEB_PATH = RESOURCE_LOADER_BASE_PATH + MOD_ID + "/textures/blocks/";
}
