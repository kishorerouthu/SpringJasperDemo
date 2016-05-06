package com.wavemaker.jasper.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.query.JRHibernateQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 * Created by kishorer on 16/3/16.
 */
@Controller
public class BaseController {

    private static final String VIEW_INDEX = "index";
    private static final String VIEW_REPORT = "reports";

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private SessionFactory sessionFactory;

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcome(ModelMap model) {
        model.addAttribute("message", "Welcome to JasperReports Demo Application");
        return VIEW_INDEX;
    }


    @RequestMapping(value = "/address", method = RequestMethod.GET)
    public String addressReport(ModelMap model,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                @RequestParam String[] filter) {

        String filePath = "/WEB-INF/HibernateQueryReport.jrxml";
        JasperReport jasperReport = generateJasperReport(request, filePath);
        List<String> cityFilter = new ArrayList<>();
        for (String str : filter) {
            cityFilter.add(str);
        }
        Map<String, Object> parameters = getParameters(cityFilter);
        submitReportResponse(response, jasperReport, parameters, "Address");
        return VIEW_REPORT;
    }

    @RequestMapping(value = "/employee", method = RequestMethod.GET)
    public String employeeReport(ModelMap model, HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(value = "stockLimit") int stockLimit) throws JRException {
            String filePath = "/WEB-INF/EmployeeReports.jrxml";
            generateJasperReport(request, filePath);
            JasperReport jasperReport = (JasperReport)JRLoader.loadObject(new File("/tmp/employee.jasper"));
            JRParameter[] defaultParameters = jasperReport.getParameters();
            for (JRParameter parameter : defaultParameters) {
                logger.info("Parameter : " + parameter);
            }
            Map<String, Object> parameters = getEmpParameters(stockLimit);
            submitReportResponse(response, jasperReport, parameters, "EmployeeStocks");
            return VIEW_REPORT;
    }

    private JasperReport generateJasperReport(HttpServletRequest request, String filePath) {
        try {
            String path = request.getSession().getServletContext().getRealPath(filePath);
            //return JasperCompileManager.compileReport(path);
            JasperDesign jasperDesign = JRXmlLoader.load(path);
            JasperCompileManager.compileReportToFile(jasperDesign, "/tmp/employee.jasper");
        } catch (JRException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void submitReportResponse(
            final HttpServletResponse response, final JasperReport jasperReport,
            final Map<String, Object> parameters, String fileName) {
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
            //JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, getConnection());
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename="+fileName+".pdf");
            response.getOutputStream().write(pdfBytes);

            response.getOutputStream().flush();
            response.getOutputStream().close();
            response.flushBuffer();
        } catch (IOException ioe ) {
            ioe.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> getParameters(List<String> cityFilter) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, createSession());
        parameters.put("ReportTitle", "Address Report");
        parameters.put("CityFilter", cityFilter);
        parameters.put("OrderClause", "city");
        return parameters;
    }

    private Map<String, Object> getEmpParameters(int stockLimit) {
        Map<String, Object> parameters = new HashMap<>();
       // parameters.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, createSession());
        parameters.put("REPORT_CONNECTION", getConnection());
        parameters.put("ReportTitle", "Employee Stock Report");
        parameters.put("StockLimit", stockLimit);
        parameters.put("OrderClause", "noOfStocks");
        return parameters;
    }

    private Session createSession() {
        Session session = sessionFactory.openSession();
        return session;
    }

    private Connection getConnection() {
        Session session = createSession();
        SessionImpl sessionImpl = (SessionImpl) session;
        Connection connection = sessionImpl.connection();
        return connection;
    }
}
