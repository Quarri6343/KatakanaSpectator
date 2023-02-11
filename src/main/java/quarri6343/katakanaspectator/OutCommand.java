package quarri6343.katakanaspectator;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.argument.PlayerArgument;
import net.kunmc.lab.commandlib.argument.StringArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;

public class OutCommand extends Command {
    public OutCommand() {
        super("out");
        
        argument(new PlayerArgument("player"),new StringArgument("word"), (player, string, ctx) -> {
            player.getLocation().createExplosion(4, false, false);
            player.setGameMode(GameMode.SPECTATOR);
            Bukkit.broadcast(Component.text(player.getName() + "が外来語「" + string  + "」を使用しました！").color(NamedTextColor.RED));
        });
    }
}
