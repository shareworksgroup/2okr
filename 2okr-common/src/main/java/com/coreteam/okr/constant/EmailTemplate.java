package com.coreteam.okr.constant;

/**
 * @ClassName: EmailTemplate
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/05/09 15:43
 * @Version 1.0.0
 */
public class EmailTemplate {
    public static final String template="<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "\n" +
            "<head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
            "    <title>2OKR</title>\n" +
            "</head>\n" +
            "\n" +
            "<body style=\"background: #fbfbfb;\">\n" +
            "\t<table style=\"width:100%;border:none;background: #fbfbfb;\">\n" +
            "\t\t<tbody>\n" +
            "\t\t\t<tr>\n" +
            "\t\t\t\t<td>\n" +
            "\t\t\t\t\t<table style=\"width:440px;background:#fff;border:none;margin:auto;margin-top:100px;box-shadow: 0 0 2px 0 rgba(0,0,0,0.04), 0 4px 8px 0 rgba(0,0,0,0.04);\" align=\"center\">\n" +
            "\t\t\t\t\t\t<tbody>\n" +
            "\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t<td style=\"padding:30px;\">\n" +
            "\t\t\t\t\t\t\t\t\t<img src=\"https://2okr.com/assets/images/logo@2x.png\" style=\"height:24px;\">\n" +
            "\t\t\t\t\t\t\t\t\t<p style=\"padding:20px 0 10px;font-size:16px;\">{greeting}</p>\n" +
            "\t\t\t\t\t\t\t\t\t<p style=\"padding:10px 0;font-size:16px;word-break:break-all;word-wrap:break-word;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t{context}\n" +
            "\t\t\t\t\t\t\t\t\t</p>\n" +
            "\t\t\t\t\t\t\t\t\t<p style=\"padding:10px 0;font-size:16px;word-break:break-all;word-wrap:break-word;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t<a href=\"{action_url}\">{action_url}</a>\n" +
            "\t\t\t\t\t\t\t\t\t</p>\n" +
            "\t\t\t\t\t\t\t\t\t<p style=\"padding:10px 0;font-size:16px;text-align: center;background: #222222;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t<a href=\"{action_url}\" style=\"color:#ffffff;text-decoration: none;\">{action}</a>\n" +
            "\t\t\t\t\t\t\t\t\t</p>\n" +
            "\t\t\t\t\t\t\t\t\t<p style=\"padding:10px 0 20px;font-size:16px;\">\n" +
            "\t\t\t\t\t\t\t\t\t\tHave a nice day, and don't hesitate to contact us with your feedback.\n" +
            "\t\t\t\t\t\t\t\t\t</p>\n" +
            "\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t\t</tbody>\n" +
            "\t\t\t\t\t</table>\n" +
            "\t\t\t\t</td>\n" +
            "\t\t\t</tr>\n" +
            "\t\t</tbody>\n" +
            "\t</table>\n" +
            "</body>\n" +
            "\n" +
            "</html>";
}
