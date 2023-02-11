package quarri6343.katakanaspectator;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Pattern;

public final class KatakanaSpectator extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent e){
        String message = new PlainComponentSerializer().serialize(e.message());

        if(isAlphabet(message) || isKana(message) || isKanaHalf(message) || isNumber(message)){
            if(e.getPlayer().getGameMode() == GameMode.SURVIVAL){
                Component component = Component.text(message).color(NamedTextColor.RED);
                e.message(component);
                Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                    @Override
                    public void run() {
                        e.getPlayer().setGameMode(GameMode.SPECTATOR);
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

    /**
     * 英字チェック
     * https://medium-company.com/java-%e8%8b%b1%e5%ad%97-%e3%83%81%e3%82%a7%e3%83%83%e3%82%af/
     * @param value 検証対象の値
     * @return 結果（true：英字、false：英字ではない）
     */
    public static boolean isAlphabet(String value) {
        boolean result = false;
        if (value != null) {
            Pattern pattern = Pattern.compile("^.*[a-zA-Z].*$");
            result = pattern.matcher(value).matches();
        }
        return result;
    }
    
    /**
     * 全角カタカナチェック
     * https://medium-company.com/java-%E5%85%A8%E8%A7%92%E3%82%AB%E3%83%8A-%E3%83%81%E3%82%A7%E3%83%83%E3%82%AF/
     * @param value 検証対象の値
     * @return 結果（true：全角カタカナ、false：全角カタカナではない）
     */
    public static boolean isKana(String value) {
        boolean result = false;
        if (value != null) {
            Pattern pattern = Pattern.compile("^.*[\u30a0-\u30ff].*$");
            result = pattern.matcher(value).matches();
        }
        return result;
    }
    
    /**
     * 半角カタカナチェック
     * https://medium-company.com/java-%E5%8D%8A%E8%A7%92%E3%82%AB%E3%83%8A-%E3%83%81%E3%82%A7%E3%83%83%E3%82%AF/
     * @param value 検証対象の値
     * @return 結果（true：半角カタカナ、false：半角カタカナではない）
     */
    public static boolean isKanaHalf(String value) {
        boolean result = false;
        if (value != null) {
            Pattern pattern = Pattern.compile("^.*[\uFF65-\uFF9F].*$");
            result = pattern.matcher(value).matches();
        }
        return result;
    }
    
    public static boolean isNumber(String value){
        boolean result = false;
        if (value != null) {
            Pattern pattern = Pattern.compile("^.*[0-9].*$");
            result = pattern.matcher(value).matches();
        }
        return result;
    }
}
