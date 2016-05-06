package com.wavemaker.jasper.utils;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Created by kishorer on 2/5/16.
 */
public class FtlUtils {

    private static Configuration cfg;

    static {
        init();
    }

    private static void init() {
        cfg = new Configuration();
        cfg.setClassForTemplateLoading(FtlUtils.class, "/");
    }

    public static void write(String templateName, Map<String, Object> input, Writer writer) {
        try {
            Template template = cfg.getTemplate(templateName);
            template.process(input, writer);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
