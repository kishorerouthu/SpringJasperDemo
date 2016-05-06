 package ${packageName};

<#assign serviceName = serviceId?cap_first>
 import java.io.File;
 import java.io.FileInputStream;

 <#if isQueryReport>
 import org.hibernate.Session;
 import org.hibernate.SessionFactory;
 </#if>
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Autowired;
 <#if isQueryReport>
 import org.springframework.beans.factory.annotation.Qualifier;
 </#if>

 import com.wavemaker.runtime.file.model.DownloadResponse;
 import com.wavemaker.runtime.report.manager.ReportServiceManager;
 import com.wavemaker.runtime.service.annotations.ExposeToClient;
 import ${packageName}.model.${serviceName}Model;

 /**
  * Report service class with methods to generate, download the report in expected format.
  * This is a singleton class with all of its public methods exposed to the client via controller.
  * Their return values and parameters will be passed to the client or taken
  * from the client respectively.
  */

 @ExposeToClient
 public class ${serviceName}Service {

    private static final Logger LOGGER =LoggerFactory.getLogger(${serviceName}Service.class);

    <#if isQueryReport>
    @Autowired
    @Qualifier("${sessionFactory}")
    private SessionFactory sessionFactory;
    </#if>

     @Autowired
     private ReportServiceManager reportServiceManager;

     public DownloadResponse generateReport(${serviceName}Model reportModel, String fileName, String format) throws Exception {
        return downloadFile(reportModel, fileName, format);
     }

     private DownloadResponse downloadFile(${serviceName}Model reportModel, String fileName, String format) throws Exception {
       <#if isQueryReport>
         Session session = getSession();
         File file = reportServiceManager.generateReport(reportModel, session ,format);
       <#else>
         File file = reportServiceManager.generateReport(reportModel, format);
       </#if>
         fileName = (fileName != null && fileName.length() > 0) ? fileName : file.getName();

         DownloadResponse downloadResponse = new DownloadResponse();
         FileInputStream fileInputStream = new FileInputStream(file);
         downloadResponse.setContents(fileInputStream);
         downloadResponse.setFileName(fileName);
         return downloadResponse;
     }

     <#if isQueryReport>
     private Session getSession() {
          Session session = sessionFactory.openSession();
          return session;
     }
     </#if>
 }