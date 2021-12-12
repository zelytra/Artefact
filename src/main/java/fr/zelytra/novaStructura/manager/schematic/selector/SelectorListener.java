package fr.zelytra.novaStructura.manager.schematic.selector;

import fr.zelytra.novaStructura.utils.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class SelectorListener implements Listener {

    @EventHandler
    public void onClickEvent(PlayerInteractEvent e) {
        if (!e.getPlayer().isOp()) return;
        if (e.getHand() != EquipmentSlot.HAND) return;

        if (e.getItem() == null) return;
        else if (e.getItem().getType() != Selector.selectorMaterial) return;

        Selector selector = Selector.getPlayerSelection(e.getPlayer());

        if (selector == null) {
            selector = new Selector(e.getPlayer());
        }

        switch (e.getAction()) {
            case LEFT_CLICK_BLOCK:
                selector.setCorner1(e.getClickedBlock().getLocation());
                e.getPlayer().sendMessage(Message.PLAYER_PREFIX + "§6First selection set at: x=§8" + e.getClickedBlock().getLocation().getBlockX() + " §6y=§8" + e.getClickedBlock().getLocation().getBlockY() + " §6z=§8" + e.getClickedBlock().getLocation().getBlockZ());
                break;
            case RIGHT_CLICK_BLOCK:
                selector.setCorner2(e.getClickedBlock().getLocation());
                e.getPlayer().sendMessage(Message.PLAYER_PREFIX + "§6Second selection set at: x=§8" + e.getClickedBlock().getLocation().getBlockX() + " §6y=§8" + e.getClickedBlock().getLocation().getBlockY() + " §6z=§8" + e.getClickedBlock().getLocation().getBlockZ());
                break;
        }
        e.setCancelled(true);
    }
}
