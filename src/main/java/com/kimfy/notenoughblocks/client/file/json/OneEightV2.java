package com.kimfy.notenoughblocks.client.file.json;

/**
 * The purpose of this is class is to handle the creation of all blockstate json files. The files will end up in
 * ./resourcepacks/NotEnoughBlocks/assets/notenoughblocks/blockstates.
 *
 * Each shape has it's own template blockstate file, this file is located in the mod's assets folder.
 * Expected behaviour:
 * 1. Iterate through all blocks that have been registered from NEB. Get the block's shape and get the template from
 * there.
 * 2. From the template, generate a new JsonObject and copy the variants and insert the correct information.
 * 3. Output file ends up in path mentioned above
 */
public class OneEightV2
{
}
