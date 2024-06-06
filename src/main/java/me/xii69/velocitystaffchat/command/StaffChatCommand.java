package me.xii69.velocitystaffchat.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.xii69.velocitystaffchat.VelocityStaffChat;
import me.xii69.velocitystaffchat.data.PlayerData;
import me.xii69.velocitystaffchat.data.database.databases.ChatStorage;
import me.xii69.velocitystaffchat.registry.PlayerDataRegistry;
import me.xii69.velocitystaffchat.settings.PluginSettings;
import me.xii69.velocitystaffchat.util.TextUtils;

import java.util.UUID;

public class StaffChatCommand implements SimpleCommand {

    private final VelocityStaffChat plugin;
    private final PluginSettings settings;
    private final PlayerDataRegistry playerDataRegistry;

    public StaffChatCommand(VelocityStaffChat plugin) {
        this.plugin = plugin;
        this.settings = plugin.getSettings();
        this.playerDataRegistry = plugin.getPlayerDataRegistry();
    }

    @Override
    public void execute(Invocation invocation) {
        ChatStorage chatStorage = plugin.getChatStorage();
        String[] args = invocation.arguments();
        CommandSource source = invocation.source();

        if (!(source instanceof Player player)) {
            source.sendMessage(TextUtils.color(settings.getMessageOnlyPlayers()));
            return;
        }

        if (!source.hasPermission("velocitystaffchat.staff")) {
            source.sendMessage(TextUtils.color(settings.getMessageNoPermission()));
            return;
        }

        if (args.length != 0) {
            plugin.sendStaffMessage(player, player.getCurrentServer().get(), String.join(" ", args));
            return;
        }

        UUID playerId = player.getUniqueId();
        PlayerData playerData = playerDataRegistry.get(playerId);
        playerData.toggle();

        boolean toggled = playerData.isToggled();
        plugin.sendToggleMessage(player, toggled);

        if (chatStorage == null) {
            return;
        }

        chatStorage.setToggled(playerId, toggled);
    }
}
