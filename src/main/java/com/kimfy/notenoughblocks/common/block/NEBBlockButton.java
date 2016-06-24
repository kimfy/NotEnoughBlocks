package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockButton;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class NEBBlockButton extends BlockButton implements IBlockProperties
{
    @Delegate
    private final BlockAgent<NEBBlockButton> agent;

    public NEBBlockButton(Material material, List<BlockJson> data)
    {
        super(data.get(0).isButtonWooden());
        this.agent = new BlockAgent<>(this, data);
    }

    @Override
    protected void playClickSound(@Nullable EntityPlayer player, World worldIn, BlockPos pos)
    {
        worldIn.playSound(player, pos, this.get(worldIn.getBlockState(pos)).getButtonOnSound(), SoundCategory.BLOCKS, 0.3F, 0.6F);
    }

    @Override
    protected void playReleaseSound(World worldIn, BlockPos pos)
    {
        worldIn.playSound(null, pos, this.get(worldIn.getBlockState(pos)).getButtonOffSound(), SoundCategory.BLOCKS, 0.3F, 0.5F);
    }
}
