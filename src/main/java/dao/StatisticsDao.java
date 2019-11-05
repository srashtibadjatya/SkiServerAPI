package dao;

import model.APIStats;
import model.APIStatsEndpointStats;
import model.RequestsLatencies;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class StatisticsDao {

    public APIStats getStats() throws SQLException, ClassNotFoundException {
        String getStatsQueryStatement = "SELECT url, operation, AVG(latency) as mean, MAX(latency) as max FROM requests_latencies GROUP BY url, operation";
        Connection conn = DBCPDataSource.getConnection();
        PreparedStatement preparedStatement;
        preparedStatement = conn.prepareStatement(getStatsQueryStatement);

        ResultSet result = preparedStatement.executeQuery();

        List<APIStatsEndpointStats> apiStatsEndpointStats = new ArrayList<>();

        while (result.next()) {
            String url = result.getString("url");
            String operation = result.getString("operation");
            int mean = result.getInt("mean");
            int max = result.getInt("max");

            APIStatsEndpointStats input = new APIStatsEndpointStats();
            input.setURL(url);
            input.setOperation(operation);
            input.setMean(mean);
            input.setMax(max);

            apiStatsEndpointStats.add(input);
        }

        conn.close();

        APIStats output = new APIStats();
        output.setEndpointStats(apiStatsEndpointStats);
        return output;
    }

    public void dumpStats(List<RequestsLatencies> input) throws SQLException, ClassNotFoundException {
        if(!input.isEmpty()) {

            Connection conn = DBCPDataSource.getConnection();

            conn.setAutoCommit(false);

            PreparedStatement preparedStatement;

            preparedStatement = conn.prepareStatement("INSERT INTO requests_latencies (url, operation, latency) VALUES(?,?,?)");

            for (RequestsLatencies requestsLatencies: input) {
                // Add each parameter to the row.
                preparedStatement.setString(1, requestsLatencies.getUrl());
                preparedStatement.setString(2, requestsLatencies.getOperation());
                preparedStatement.setInt(3, requestsLatencies.getLatency());
                // Add row to the batch.
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            conn.commit();
            conn.close();
        }
    }
}
