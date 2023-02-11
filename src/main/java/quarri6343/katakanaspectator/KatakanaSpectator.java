package quarri6343.katakanaspectator;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kunmc.lab.commandlib.CommandLib;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class KatakanaSpectator extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        CommandLib.register(this, new OutCommand());
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent e){
        String message = new PlainComponentSerializer().serialize(e.message());

        String outMessage = "";
        outMessage = extractIllegal(message);
        
        if(!outMessage.equals("")){
            if(e.getPlayer().getGameMode() == GameMode.SURVIVAL){
                message = message.replace(outMessage, ChatColor.RED + outMessage + ChatColor.RESET);
                e.message(Component.text(message));
                Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                    @Override
                    public void run() {
                        e.getPlayer().getLocation().createExplosion(4, false, false);
                        e.getPlayer().setGameMode(GameMode.SPECTATOR);
                        Bukkit.broadcast(Component.text(e.getPlayer().getName() + "が外来語を使用しました！").color(NamedTextColor.RED));
                    }
                }, 0);
            }
            else if(e.getPlayer().getGameMode() == GameMode.SPECTATOR){
                e.setCancelled(true);
                e.getPlayer().sendMessage(Component.text("スペクテイター中はひらがなと漢字以外は送信できません").color(NamedTextColor.RED));
            }
        }
        else{
            if(e.getPlayer().getGameMode() == GameMode.SPECTATOR){
                e.message(e.message().color(NamedTextColor.GRAY));
            }
            else{
                e.message(e.message());
            }
        }
    }
    
    public static String extractIllegal(String value) {
        if (value != null) {
            Pattern pattern = Pattern.compile("([ -~])+|([ァ-ヴ])+|([･-ﾟ])+|([０-９Ａ-Ｚａ-ｚ])+");
            Matcher matcher = pattern.matcher(value);
            if (matcher.find()) {
                // マッチした部分文字列の表示を行う
                return matcher.group(0);
            }
        }
        return "";
    }
}
