import com.google.gson.Gson;
import model.LiftRide;
import model.ResponseMsg;
import model.SkierVertical;
import model.SkierVerticalResorts;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;

public class SkierServlet extends javax.servlet.http.HttpServlet {

    private static final Gson gson = new Gson();

    protected void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws javax.servlet.ServletException, IOException {
        int pathLength;

        String urlPath = req.getPathInfo();

        res.setContentType("application/json");

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ResponseMsg output = new ResponseMsg().message("Invalid inputs supplied");
            res.getWriter().write(gson.toJson(output));
            return;
        }

        String[] urlParts = urlPath.split("/");

        pathLength = urlParts.length;

        if (!isUrlValid(urlParts, pathLength)) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ResponseMsg output = new ResponseMsg().message("Invalid inputs supplied");
            res.getWriter().write(gson.toJson(output));
        }

        BufferedReader reqBody = req.getReader();

        try {
            gson.fromJson(reqBody, LiftRide.class);
            res.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception ex) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ResponseMsg output = new ResponseMsg().message("Invalid inputs supplied");
            res.getWriter().write(gson.toJson(output));
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws javax.servlet.ServletException, IOException {
        int pathLength;
        int totalVertical = 10;
        String urlPath = req.getPathInfo();

        res.setContentType("application/json");

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ResponseMsg output = new ResponseMsg().message("Invalid inputs supplied");
            res.getWriter().write(gson.toJson(output));
            return;
        }

        String[] urlParts = urlPath.split("/");

        pathLength = urlParts.length;

        if (!isUrlValid(urlParts, pathLength)) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ResponseMsg output = new ResponseMsg().message("Invalid inputs supplied");
            res.getWriter().write(gson.toJson(output));
        } else {
            if (pathLength == 8) {
                res.setStatus(HttpServletResponse.SC_OK);
                // do any sophisticated processing with urlParts which contains all the url params
                res.getWriter().write(gson.toJson(totalVertical));
                return;
            }

            String resort = req.getParameter("resort");
            String season = req.getParameter("season");

            if(resort == null) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ResponseMsg output = new ResponseMsg().message("Invalid inputs supplied");
                res.getWriter().write(gson.toJson(output));
                return;
            }

            res.setStatus(HttpServletResponse.SC_OK);

            if(season == null) {
                SkierVertical output = new SkierVertical();
                SkierVerticalResorts skierVerticalResorts = new SkierVerticalResorts().seasonID("All");
                skierVerticalResorts.setTotalVert(totalVertical);
                output.addResortsItem(skierVerticalResorts);
                res.getWriter().write(gson.toJson(output));
            } else {
                SkierVertical output = new SkierVertical();
                SkierVerticalResorts skierVerticalResorts = new SkierVerticalResorts().seasonID(season);
                skierVerticalResorts.setTotalVert(totalVertical);
                output.addResortsItem(skierVerticalResorts);
                res.getWriter().write(gson.toJson(output));
            }
        }
    }

    private boolean isUrlValid(String[] urlPath, int pathLength) {
        // urlPath  = "/1/seasons/2019/day/1/skier/123"
        // urlParts = [, 1, seasons, 2019, day, 1, skier, 123]
        if (pathLength == 8) {
            return ("".equals(urlPath[0]) && isInteger(urlPath[1]) && isInteger(urlPath[7])
                    && "seasons".equals(urlPath[2]) && "days".equals(urlPath[4]) && "skiers".equals(urlPath[6])
                    && isValidId(urlPath[3]) && isValidId(urlPath[5]));
        }
        return ("".equals(urlPath[0]) && isInteger(urlPath[1]) && "vertical".equals(urlPath[2]));
    }

    private boolean isInteger(String s) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i), 10) < 0) return false;
        }
        return true;
    }

    private boolean isValidId(String s) {
        int inputLength = s.length();
        return (inputLength > 0 && inputLength <= 366);
    }

    private int getRandomNumber() {
        // create random object
        Random ran = new Random();

        // Print next int value
        // Returns number between 0-40
        return 1 + ran.nextInt(39);
    }
}
