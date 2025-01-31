package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.block.BlockHolder;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerInteractEvent extends CancellableAbstractEvent implements SPlayerEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<@Nullable Item> item;
    private final ImmutableObjectLink<Action> action;
    private final ImmutableObjectLink<@Nullable BlockHolder> blockClicked;
    private final ImmutableObjectLink<BlockFace> blockFace;
    private final ObjectLink<AbstractEvent.Result> useClickedBlock;
    private final ObjectLink<AbstractEvent.Result> useItemInHand;
    private final ImmutableObjectLink<@Nullable EquipmentSlotHolder> hand;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    @Nullable
    public Item getItem() {
        return item.get();
    }

    public Action getAction() {
        return action.get();
    }

    @Nullable
    public BlockHolder getBlockClicked() {
        return blockClicked.get();
    }

    public BlockFace getBlockFace() {
        return blockFace.get();
    }

    public Result getUseClickedBlock() {
        return useClickedBlock.get();
    }

    public void setUseClickedBlock(Result useClickedBlock) {
        this.useClickedBlock.set(useClickedBlock);
    }

    public Result getUseItemInHand() {
        return useItemInHand.get();
    }

    public void setUseItemInHand(Result useItemInHand) {
        this.useItemInHand.set(useItemInHand);
    }

    @Nullable
    public EquipmentSlotHolder getHand() {
        return hand.get();
    }

    /**
     * Sets the cancellation state of this event. A canceled event will not be
     * executed in the server, but will still pass to other plugins
     * <p>
     * Canceling this event will prevent use of food (player won't lose the
     * food item), prevent bows/snowballs/eggs from firing, etc. (player won't
     * lose the ammo)
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        setUseClickedBlock(cancel ? AbstractEvent.Result.DENY : getUseClickedBlock() == AbstractEvent.Result.DENY ? AbstractEvent.Result.DEFAULT : getUseClickedBlock());
        setUseItemInHand(cancel ? AbstractEvent.Result.DENY : getUseItemInHand() == AbstractEvent.Result.DENY ? AbstractEvent.Result.DEFAULT : getUseItemInHand());
    }

    /**
     * Check if this event involved a block
     *
     * @return boolean true if it did
     */
    public boolean hasBlock() {
        return getBlockClicked() != null;
    }

    /**
     * Check if this event involved an item
     *
     * @return boolean true if it did
     */
    public boolean hasItem() {
        return getItem() != null;
    }

    /**
     * Convenience method. Returns the material of the item represented by
     * this event
     *
     * @return Material the material of the item used
     */
    @NotNull
    public ItemTypeHolder getMaterial() {
        if (!hasItem()) {
            return ItemTypeHolder.air();
        }

        return getItem().getMaterial();
    }

    /**
     * Gets the cancellation state of this event. Set to true if you want to
     * prevent buckets from placing water and so forth
     *
     * @return boolean cancellation state
     * @deprecated This event has two possible cancellation states. It is
     * possible a call might have the former false, but the latter true, eg in
     * the case of using a firework whilst gliding. Callers should check the
     * relevant methods individually.
     */
    @Deprecated
    @Override
    public boolean isCancelled() {
        return getUseClickedBlock() == Result.DENY;
    }

    // TODO: holder?
    public enum Action {
        LEFT_CLICK_BLOCK,
        RIGHT_CLICK_BLOCK,
        LEFT_CLICK_AIR,
        RIGHT_CLICK_AIR,
        PHYSICAL;

        public static List<Action> VALUES = Arrays.asList(values());

        public static Action convert(String name) {
            return VALUES.stream()
                    .filter(next -> next.name().equalsIgnoreCase(name))
                    .findFirst()
                    .orElse(LEFT_CLICK_AIR);
        }
    }
}
