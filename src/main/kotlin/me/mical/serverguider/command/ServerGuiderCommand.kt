package me.mical.serverguider.command

import me.mical.serverguider.ConfigReader
import me.mical.serverguider.database.PluginDatabase
import me.mical.serverguider.guide.GuideReader
import me.mical.serverguider.ui.GuideMenu
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.module.database.Host
import taboolib.module.ui.MenuHolder
import taboolib.platform.util.onlinePlayers

/**
 * ServerGuider
 * me.mical.serverguider.command.ServerGuiderCommand
 *
 * @author xiaomu
 * @since 2023/2/14 10:53 PM
 */
@CommandHeader(name = "serverguider", aliases = ["sg", "guide"], permission = "serverguider.use")
object ServerGuiderCommand {

    @CommandBody(permission = "serverguider.use")
    val main = mainCommand {
        execute<Player> { sender, _, _ ->
            GuideMenu.open(sender)
        }
    }

    @CommandBody(permission = "serverguider.reload")
    val reload = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            for (player in onlinePlayers){
                if (player.inventory is MenuHolder){
                    player.closeInventory()
                }
            }
            ConfigReader.config.reload()
            Host.invokeMethod<Any>("release")
            PluginDatabase.init()
            GuideReader.load()
            GuideMenu.reload()
            sender.sendMessage("重载完成")
        }
    }
}