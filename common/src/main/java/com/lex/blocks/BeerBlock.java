package com.lex.blocks;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.satisfy.brewery.core.block.BeverageBlock;

public class BeerBlock extends BeverageBlock {

    public BeerBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion().instabreak(), 2);
    }
}
