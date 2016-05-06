import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.wavemaker.jasper.utils.FtlUtils;

/**
 * Created by kishorer on 2/5/16.
 */
public class GenerateModels {

    private static final String FTL_MODEL_FILE_PATH = "model.ftl";
    private static final String FTL_SERVICE_FILE_PATH = "service.ftl";

    @Test
    public void generateModel() {
        Map<String, String> properties = new HashMap<>();
        properties.put("stockLimit", "int");
        properties.put("reportName", "String");

        Map<String, Object> ftlParameters = new HashMap<>();
        ftlParameters.put("packageName", "com.report.myreport");
        ftlParameters.put("properties", properties);
        ftlParameters.put("serviceId","MyReport");

        File file = new File("/tmp/ReportModel.java");
        try {
            FtlUtils.write(FTL_MODEL_FILE_PATH, ftlParameters,  new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void generateService() {
        Map<String, Object> ftlParameters = new HashMap<>();
        ftlParameters.put("packageName", "com.report.myreport");
        ftlParameters.put("serviceId", "MyReport");
        ftlParameters.put("isQueryReport", false);
        ftlParameters.put("sessionFactory", "HRDBSessionFactory");
        File file = new File("/tmp/ReportService.java");
        try {
            FtlUtils.write(FTL_SERVICE_FILE_PATH, ftlParameters,  new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
