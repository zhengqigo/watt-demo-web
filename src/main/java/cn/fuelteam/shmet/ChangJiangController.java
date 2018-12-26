package cn.fuelteam.shmet;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.fuelteam.watt.httpclient.RequestExecutor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

@RestController
@RequestMapping(value = "/changjiang")
public class ChangJiangController {

    @RequestMapping(value = "/export")
    public View export(@PathParam("page") Integer page) {

        View view = new AbstractXlsxView() {
            @Override
            protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
                    HttpServletResponse response) throws Exception {
                export(model, workbook, request, response, page);
            }
        };
        return view;
    }

    private void export(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response, int page) throws Exception {
        // create excel sheet
        String sheetName = "长江报价";
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet(sheetName);

        // create title row
        int rownum = 0;
        XSSFRow header = sheet.createRow(rownum++);
        String[] titles = new String[] { "日期", "品种", "规格", "报价", "最低价", "最高价", "涨跌", "升贴水最低价", "升贴水最高价", "单位" };

        for (int i = 0; i < titles.length; i++) {
            header.createCell(i).setCellValue(titles[i]);
        }

        List<List<String>> list = count(page);

        XSSFRow row = null;
        for (int i = 0; i < list.size(); i++) {
            int idx = 0;
            row = sheet.createRow(rownum++);
            List<String> content = list.get(i);
            row.createCell(idx++).setCellValue(content.get(0));
            row.createCell(idx++).setCellValue(content.get(1));
            row.createCell(idx++).setCellValue(content.get(2));
            row.createCell(idx++).setCellValue(content.get(3));
            row.createCell(idx++).setCellValue(content.get(4));
            row.createCell(idx++).setCellValue(content.get(5));
            row.createCell(idx++).setCellValue(content.get(6));
            row.createCell(idx++).setCellValue(content.get(7));
            row.createCell(idx++).setCellValue(content.get(8));
            row.createCell(idx++).setCellValue(content.get(9));
        }
        String fileName = sheetName + ".xlsx";
        if ("IE".equals(getBrowser(request))) {
            fileName = new String(java.net.URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        }
        response.setContentType("application/vnd.ms-excel");
    }

    private String getBrowser(HttpServletRequest request) {
        String UserAgent = request.getHeader("User-Agent").toLowerCase();
        if (UserAgent.indexOf("firefox") >= 0) {
            return "FF";
        } else if (UserAgent.indexOf("safari") >= 0) {
            return "Chrome";
        } else {
            return "IE";
        }
    }

    private Cookie[] cookie() {
        String key = ".AspxFormAuth";
        String value = "0A971FD3C7BC4F45129215B1CBD7303B025C700C2069667AC62DCE32DBE467C6D409FC450C7AE9CE3F8106D8327A0302F2CEAB083E0ACF0C56EC2777E427CB3E297E95E6A84A15889E3532F1CCF1A172051DFEC12C8B754D16C19135";
        BasicClientCookie cookie = new BasicClientCookie(key, value);
        cookie.setDomain("www.shmet.com");
        cookie.setPath("/");
        return new Cookie[] { cookie };
    }

    public List<List<String>> count(int page) {
        int total = page;
        List<List<String>> list = Lists.newArrayList();
        for (int i = 1; i <= total; i++) {
            List<List<String>> pg = shanghaiSpot(i);
            if (pg != null) list.addAll(pg);
        }
        return list;
    }

    private String updown(String origin) {
        if (origin == null) return "";
        if (origin.contains("持平")) return "0";
        if (origin.contains("↑")) return origin.replace("↑", "").trim();
        if (origin.contains("↓")) return origin.replace("↓", "-").trim();
        return origin.trim();
    }

    private String bcl(String origin) {
        if (origin == null) return "";
        if (origin.contains("level")) return "0";
        if (origin.startsWith("c")) return origin.replace("c", "-").trim();
        if (origin.startsWith("b")) return origin.replace("b", "").trim();
        return origin.trim();
    }

    private List<List<String>> shanghaiSpot(int page) {
        String origin = "http://www.shmet.com/quotation/f-9.html%s";
        String url = "";
        if (page > 1) {
            url = String.format(origin, "?pageIndex=" + page);
        } else {
            url = String.format(origin, "");
        }
        // System.out.println(url);
        String contents = "";
        try {
            contents = new RequestExecutor<HttpGet>().build(HttpGet.class).on(String.format(url, ""), null, null, null)
                    .timeout(3000, 3000).string(cookie());
        } catch (Exception ex) {
            System.out.println("page " + page + " did not success.");
        }
        if (contents.equals("")) return null;
        Document doc = Jsoup.parse(contents);
        Elements container = doc.select("#contentItemsContainer");
        Elements lis = container.select("li");
        List<List<String>> list = Lists.newArrayList();
        for (Element element : lis) {
            Elements h3 = element.select("h3");
            String h3Date = h3.get(0).text();
            Elements trs = element.select("tr");
            for (int i = 0; i < trs.size(); i++) {
                if (i == 0) continue;
                Elements tds = trs.get(i).select("td");
                List<String> tdList = Lists.newArrayList();
                tdList.add(h3Date.replace("日期：", "").trim());
                for (int j = 0; j < tds.size(); j++) {
                    String text = tds.get(j).text();
                    if (j == 5) text = updown(text);
                    if (j == 6 || j == 7) text = bcl(text);
                    tdList.add(text);
                }
                list.add(tdList);
            }
        }
        return list;
    }
}
