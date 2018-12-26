package cn.finalabproject.smartdesklamp.smartdesklamp.service;

public interface EmailService {
    //发送邮箱验证码
    public boolean sentVerificationCode(String email);

    //发送欢迎邮件
    public boolean sentWelcomePage(String email);
}
