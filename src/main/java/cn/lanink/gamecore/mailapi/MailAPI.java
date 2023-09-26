package cn.lanink.gamecore.mailapi;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.api.Info;
import cn.lanink.gamecore.hotswap.ModuleBase;
import cn.nukkit.scheduler.AsyncTask;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.function.Consumer;

/**
 * @author LT_Name
 */
public class MailAPI extends ModuleBase {

    private static MailAPI mailAPI;

    private PluginConfig pluginConfig;

    private Session session;

    public static MailAPI getInstance() {
        return mailAPI;
    }

    @Override
    protected void onEnable() {
        mailAPI = this;

        this.pluginConfig = new PluginConfig(this);

        this.session = Session.getInstance(this.pluginConfig.getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailAPI.this.pluginConfig.getSmtpUserUsername(), MailAPI.this.pluginConfig.getSmtpUserPassword());
            }
        });
    }



    @Override
    protected void onDisable() {

    }

    @Info("异步发送邮件")
    public void sendMail(@NotNull String mail, @NotNull String title, @NotNull String content) {
        this.sendMail(mail, title, content, null);
    }

    @Info("异步发送邮件")
    public void sendMail(@NotNull String mail, @NotNull String title, @NotNull String content, Consumer<CallBack> callBackConsumer) {
        this.getServer().getScheduler().scheduleAsyncTask(GameCore.getInstance(), new AsyncTask() {
            @Override
            public void onRun() {
                CallBack callBack = sendMailSync(mail, title, content);

                if (callBackConsumer != null) {
                    callBackConsumer.accept(callBack);
                }
            }
        });
    }

    @Info("同步发送邮件，请尽量异步调用防止卡主线程！")
    public CallBack sendMailSync(@NotNull String mail, @NotNull String title, @NotNull String content) {
        CallBack callBack = new CallBack(true, "发送成功");
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(MailAPI.this.pluginConfig.getSmtpUserUsername());
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject(title);
            message.setContent(content, "text/html;charset=UTF-8");
            message.setSentDate(new Date());
            Transport.send(message);
        } catch (Exception e) {
            getLogger().error("发送邮件失败", e);
            callBack = new CallBack(false, "发送失败");
        }


        return callBack;
    }

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class CallBack {
        private boolean success;
        private String message;
    }
}
