package cn.lanink.gamecore.mailapi;

import cn.lanink.gamecore.utils.ConfigUtils;
import cn.nukkit.utils.Config;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;

/**
 * @author LT_Name
 */
@Getter
public class PluginConfig {

    private final MailAPI mailAPI;
    private final Config config;

    private String smtpServerHost;
    private String smtpServerPort;
    private String smtpUseSSL;
    private String smtpUserUsername;
    private String smtpUserPassword;

    public PluginConfig(MailAPI mailAPI) {
        this.mailAPI = mailAPI;
        mailAPI.saveDefaultConfig();
        this.config = mailAPI.getConfig();

        this.smtpServerHost = config.getString("smtpServer.host");
        this.smtpServerPort = config.getString("smtpServer.port");
        this.smtpUseSSL = config.getString("smtpServer.ssl");

        this.smtpUserUsername = config.getString("smtpUser.username");
        this.smtpUserPassword = config.getString("smtpUser.password");

        this.save();
    }

    public void save() {
        this.config.set("smtpServer.host", this.smtpServerHost);
        this.config.set("smtpServer.port", this.smtpServerPort);
        this.config.set("smtpServer.ssl", this.smtpUseSSL);

        this.config.set("smtpUser.username", this.smtpUserUsername);
        this.config.set("smtpUser.password", this.smtpUserPassword);

        this.config.save();

        Config description = new Config();
        description.load(this.mailAPI.getResource("ConfigDescription.yml"));
        ConfigUtils.addDescription(this.config, description);
    }

    @NotNull
    public Properties getProperties() {
        Properties properties = new Properties();

        properties.put("mail.smtp.host", this.getSmtpServerHost());
        properties.put("mail.smtp.port", this.getSmtpServerPort());
        properties.put("mail.smtp.ssl", this.getSmtpUseSSL());
        // 需要认证
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.user", this.getSmtpUserUsername());
        properties.put("mail.smtp.pass", this.getSmtpUserPassword());
        // 使用ssl
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        return properties;
    }
}
