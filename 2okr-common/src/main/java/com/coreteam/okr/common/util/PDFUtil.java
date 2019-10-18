package com.coreteam.okr.common.util;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.css.apply.ChunkCssApplier;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

public class PDFUtil {

    private static SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");

    public static File htmlToPDF(String htmlString,String pdfName) {
        try {
            File pdfFile=new File(createDefaultDir()+"/"+pdfName);
            InputStream htmlFileStream = IOUtils.toInputStream(htmlString, StandardCharsets.UTF_8.name());
            // 创建一个document对象实例
            Document document = new Document(PageSize.A4,15.f,15.f,36.f,36.f);
            // 为该Document创建一个Writer实例
            PdfWriter pdfwriter = PdfWriter.getInstance(document,
                    new FileOutputStream(pdfFile));
            // 打开当前的document;
            document.open();
            CSSResolver cssResolver = new StyleAttrCSSResolver();
            CssFile cssFile = XMLWorkerHelper.getCSS(new ByteArrayInputStream("body {font-family:tsc fming s tt}".getBytes()));
            cssResolver.addCss(cssFile);

            CssAppliers cssAppliers = new CssAppliersImpl();
            cssAppliers.setChunkCssAplier(new MyChunkCssApplier());
            HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

            // Pipelines
            PdfWriterPipeline pdf = new PdfWriterPipeline(document, pdfwriter);

            HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
            CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

            // XML Worker
            XMLWorker worker = new XMLWorker(css, true);
            XMLParser p = new XMLParser(worker);
            p.parse(htmlFileStream, Charset.forName("UTF-8"));
            // step 5
            document.close();

            return pdfFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String createDefaultDir() throws FileNotFoundException {
        File rootDir=new File("./temp");
        if(!rootDir.exists()){
            rootDir.mkdir();
        }
        String date=df.format(new Date());
        File dir=new File("./temp/"+date);
        if(!dir.exists()){
            dir.mkdir();
        }
        return dir.getPath();

    }

    public static void clearYestadayDefaultDir() throws FileNotFoundException {
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,-1);
        String date=df.format(calendar.getTime());
        deleteDir(new File("./temp/"+date));

    }

    public  static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }



}

class MyChunkCssApplier extends ChunkCssApplier {

    public static BaseFont chinessFont = null;
    static {
        try {
            // 中文支持，需要引入 itext-asian.jar
            chinessFont = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否存在中文
     * @param str
     * @return
     */
    private static boolean isChinese(String str){
        if(str == null ){
            return false;
        }
        //存在中文
        String regex = ".*[\\u4e00-\\u9faf].*";
        return Pattern.matches(regex, str);
    }

    /**
     *
     * 重写apply方法
     */
    @Override
    public Chunk apply(Chunk c, Tag t) {
        Font f = applyFontStyles(t);
        // 增加此段代码 如果中文 ，则返回中文字体
        if (null != chinessFont && isChinese(c.getContent())) {
            f = new Font(chinessFont, f.getSize(), f.getStyle(), f.getColor());
        }
        // 下面代码从源码中copy
        float size = f.getSize();
        Map<String, String> rules = t.getCSS();
        for (Map.Entry<String, String> entry : rules.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (CSS.Property.FONT_STYLE.equalsIgnoreCase(key)) {
                if (value.equalsIgnoreCase(CSS.Value.OBLIQUE)) {
                    c.setSkew(0, 12);
                }
            } else if (CSS.Property.LETTER_SPACING.equalsIgnoreCase(key)) {
                String letterSpacing = rules.get(CSS.Property.LETTER_SPACING);
                float letterSpacingValue = 0f;
                if (utils.isRelativeValue(value)) {
                    letterSpacingValue = utils.parseRelativeValue(letterSpacing, f.getSize());
                } else if (utils.isMetricValue(value)){
                    letterSpacingValue = utils.parsePxInCmMmPcToPt(letterSpacing);
                }
                c.setCharacterSpacing(letterSpacingValue);
            } else if (null != rules.get(CSS.Property.XFA_FONT_HORIZONTAL_SCALE)) { // only % allowed; need a catch block NumberFormatExc?
                c.setHorizontalScaling(Float.parseFloat(rules.get(CSS.Property.XFA_FONT_HORIZONTAL_SCALE).replace("%", "")) / 100);
            }
        }
        // following styles are separate from the for each loop, because they are based on font settings like size.
        if (null != rules.get(CSS.Property.VERTICAL_ALIGN)) {
            String value = rules.get(CSS.Property.VERTICAL_ALIGN);
            if (value.equalsIgnoreCase(CSS.Value.SUPER) || value.equalsIgnoreCase(CSS.Value.TOP) || value.equalsIgnoreCase(CSS.Value.TEXT_TOP)) {
                c.setTextRise((float) (size / 2 + 0.5));
            } else if (value.equalsIgnoreCase(CSS.Value.SUB) || value.equalsIgnoreCase(CSS.Value.BOTTOM) || value.equalsIgnoreCase(CSS.Value.TEXT_BOTTOM)) {
                c.setTextRise(-size / 2);
            } else {
                c.setTextRise(utils.parsePxInCmMmPcToPt(value));
            }
        }
        String xfaVertScale = rules.get(CSS.Property.XFA_FONT_VERTICAL_SCALE);
        if (null != xfaVertScale) {
            if (xfaVertScale.contains("%")) {
                size *= Float.parseFloat(xfaVertScale.replace("%", "")) / 100;
                c.setHorizontalScaling(100 / Float.parseFloat(xfaVertScale.replace("%", "")));
            }
        }
        if (null != rules.get(CSS.Property.TEXT_DECORATION)) {
            String[] splitValues = rules.get(CSS.Property.TEXT_DECORATION).split("\\s+");
            for (String value : splitValues) {
                if (CSS.Value.UNDERLINE.equalsIgnoreCase(value)) {
                    c.setUnderline(null, 0.75f, 0, 0, -0.125f, PdfContentByte.LINE_CAP_BUTT);
                }
                if (CSS.Value.LINE_THROUGH.equalsIgnoreCase(value)) {
                    c.setUnderline(null, 0.75f, 0, 0, 0.25f, PdfContentByte.LINE_CAP_BUTT);
                }
            }
        }
        if (null != rules.get(CSS.Property.BACKGROUND_COLOR)) {
            c.setBackground(HtmlUtilities.decodeColor(rules.get(CSS.Property.BACKGROUND_COLOR)));
        }
        f.setSize(size);
        c.setFont(f);

        Float leading = null;
        if(rules.get(CSS.Property.LINE_HEIGHT) != null) {
            String value = rules.get(CSS.Property.LINE_HEIGHT);
            if(utils.isNumericValue(value)) {
                leading = Float.parseFloat(value) * c.getFont().getSize();
            } else if (utils.isRelativeValue(value)) {
                leading = utils.parseRelativeValue(value, c.getFont().getSize());
            } else if (utils.isMetricValue(value)){
                leading = utils.parsePxInCmMmPcToPt(value);
            }
        }

        if (leading != null) {
            c.setLineHeight(leading);
        }
        return c;
    }
}