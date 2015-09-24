package gg.uhc.gamestatus;

import com.google.common.base.Optional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.Map;

public class MotdController implements Listener {

    protected Map<String, String> motds;
    protected Optional<String> chosenMotd;

    public MotdController(Map<String, String> motds, String initialKey) {
        this.motds = motds;

        chosenMotd = Optional.fromNullable(motds.get(initialKey));
    }

    public Optional<String> chooseMotd(String id) {
        chosenMotd = Optional.fromNullable(motds.get(id));

        return chosenMotd;
    }

    public void clearMotd() {
        chosenMotd = Optional.absent();
    }

    @EventHandler
    public void on(ServerListPingEvent event) {
        if (!chosenMotd.isPresent()) return;

        event.setMotd(chosenMotd.get());
    }
}
