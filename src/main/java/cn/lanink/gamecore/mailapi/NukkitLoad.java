package cn.lanink.gamecore.mailapi;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.hotswap.manager.HotSwapManager;
import cn.lanink.gamecore.mailapi.utils.GameCoreDownload;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;

/**
 * @author LT_Name
 */
public class NukkitLoad extends PluginBase {

    private static NukkitLoad nukkitLoad;

    public static NukkitLoad getInstance() {
        return nukkitLoad;
    }

    @Override
    public void onLoad() {
        nukkitLoad = this;
    }

    @Override
    public void onEnable() {
        //检查依赖
        GameCoreDownload.checkAndDownload();
        Plugin plugin = this.getServer().getPluginManager().getPlugin("MemoriesOfTime-GameCore");
        if (!this.getServer().getPluginManager().isPluginEnabled(plugin)) {
            this.getServer().getPluginManager().enablePlugin(plugin);
        }
        //加载模块
        HotSwapManager hotSwapManager = GameCore.getInstance().hotSwapManager;
        hotSwapManager.loadModuleFromLocal(this.getFile());
        hotSwapManager.enableModule("MailAPI");
    }

}
