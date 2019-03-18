package com.ilyaeremin.graphicmodule;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ChartParserTest {

    @Test
    public void parse() {
        // Arrange
        int expectedColumnCount = 2;

        int     expectedFirstColumnColor  = 0xFF3DC23F;
        String  expectedFirstColumnName   = "#0";
        Type    expectedFirstColumnType   = Type.LINE;
        float[] expectedFirstColumnPoints = {0L, 37, 1542499200000L, 20, 1542585600000L, 32, 1542672000000L, 39, 1542758400000L, 32, 1542844800000L, 35, 1542931200000L, 19, 1543017600000L, 65, 1543104000000L, 36, 1543190400000L, 62, 1543276800000L, 113, 1543363200000L, 69, 1543449600000L, 120, 1543536000000L, 60, 1543622400000L, 51, 1543708800000L, 49, 1543795200000L, 71, 1543881600000L, 122, 1543968000000L, 149, 1544054400000L, 69, 1544140800000L, 57, 1544227200000L, 21, 1544313600000L, 33, 1544400000000L, 55, 1544486400000L, 92, 1544572800000L, 62, 1544659200000L, 47, 1544745600000L, 50, 1544832000000L, 56, 1544918400000L, 116, 1545004800000L, 63, 1545091200000L, 60, 1545177600000L, 55, 1545264000000L, 65, 1545350400000L, 76, 1545436800000L, 33, 1545523200000L, 45, 1545609600000L, 64, 1545696000000L, 54, 1545782400000L, 81, 1545868800000L, 180, 1545955200000L, 123, 1546041600000L, 106, 1546128000000L, 37, 1546214400000L, 60, 1546300800000L, 70, 1546387200000L, 46, 1546473600000L, 68, 1546560000000L, 46, 1546646400000L, 51, 1546732800000L, 33, 1546819200000L, 57, 1546905600000L, 75, 1546992000000L, 70, 1547078400000L, 95, 1547164800000L, 70, 1547251200000L, 50, 1547337600000L, 68, 1547424000000L, 63, 1547510400000L, 66, 1547596800000L, 53, 1547683200000L, 38, 1547769600000L, 52, 1547856000000L, 109, 1547942400000L, 121, 1548028800000L, 53, 1548115200000L, 36, 1548201600000L, 71, 1548288000000L, 96, 1548374400000L, 55, 1548460800000L, 58, 1548547200000L, 29, 1548633600000L, 31, 1548720000000L, 55, 1548806400000L, 52, 1548892800000L, 44, 1548979200000L, 126, 1549065600000L, 191, 1549152000000L, 73, 1549238400000L, 87, 1549324800000L, 255, 1549411200000L, 278, 1549497600000L, 219, 1549584000000L, 170, 1549670400000L, 129, 1549756800000L, 125, 1549843200000L, 126, 1549929600000L, 84, 1550016000000L, 65, 1550102400000L, 53, 1550188800000L, 154, 1550275200000L, 57, 1550361600000L, 71, 1550448000000L, 64, 1550534400000L, 75, 1550620800000L, 72, 1550707200000L, 39, 1550793600000L, 47, 1550880000000L, 52, 1550966400000L, 73, 1551052800000L, 89, 1551139200000L, 156, 1551225600000L, 86, 1551312000000L, 105, 1551398400000L, 88, 1551484800000L, 45, 1551571200000L, 33, 1551657600000L, 56, 1551744000000L, 142, 1551830400000L, 124, 1551916800000L, 114, 1552003200000L, 64};

        int     expectedSecondColumnColor  = 0xFFF34C44;
        String  expectedSecondColumnName   = "#1";
        Type    expectedSecondColumnType   = Type.LINE;
        float[] expectedSecondColumnPoints = {1542412800000L, 22, 1542499200000L, 12, 1542585600000L, 30, 1542672000000L, 40, 1542758400000L, 33, 1542844800000L, 23, 1542931200000L, 18, 1543017600000L, 41, 1543104000000L, 45, 1543190400000L, 69, 1543276800000L, 57, 1543363200000L, 61, 1543449600000L, 70, 1543536000000L, 47, 1543622400000L, 31, 1543708800000L, 34, 1543795200000L, 40, 1543881600000L, 55, 1543968000000L, 27, 1544054400000L, 57, 1544140800000L, 48, 1544227200000L, 32, 1544313600000L, 40, 1544400000000L, 49, 1544486400000L, 54, 1544572800000L, 49, 1544659200000L, 34, 1544745600000L, 51, 1544832000000L, 51, 1544918400000L, 51, 1545004800000L, 66, 1545091200000L, 51, 1545177600000L, 94, 1545264000000L, 60, 1545350400000L, 64, 1545436800000L, 28, 1545523200000L, 44, 1545609600000L, 96, 1545696000000L, 49, 1545782400000L, 73, 1545868800000L, 30, 1545955200000L, 88, 1546041600000L, 63, 1546128000000L, 42, 1546214400000L, 56, 1546300800000L, 67, 1546387200000L, 52, 1546473600000L, 67, 1546560000000L, 35, 1546646400000L, 61, 1546732800000L, 40, 1546819200000L, 55, 1546905600000L, 63, 1546992000000L, 61, 1547078400000L, 105, 1547164800000L, 59, 1547251200000L, 51, 1547337600000L, 76, 1547424000000L, 63, 1547510400000L, 57, 1547596800000L, 47, 1547683200000L, 56, 1547769600000L, 51, 1547856000000L, 98, 1547942400000L, 103, 1548028800000L, 62, 1548115200000L, 54, 1548201600000L, 104, 1548288000000L, 48, 1548374400000L, 41, 1548460800000L, 41, 1548547200000L, 37, 1548633600000L, 30, 1548720000000L, 28, 1548806400000L, 26, 1548892800000L, 37, 1548979200000L, 65, 1549065600000L, 86, 1549152000000L, 70, 1549238400000L, 81, 1549324800000L, 54, 1549411200000L, 74, 1549497600000L, 70, 1549584000000L, 50, 1549670400000L, 74, 1549756800000L, 79, 1549843200000L, 85, 1549929600000L, 62, 1550016000000L, 36, 1550102400000L, 46, 1550188800000L, 68, 1550275200000L, 43, 1550361600000L, 66, 1550448000000L, 50, 1550534400000L, 28, 1550620800000L, 66, 1550707200000L, 39, 1550793600000L, 23, 1550880000000L, 63, 1550966400000L, 74, 1551052800000L, 83, 1551139200000L, 66, 1551225600000L, 40, 1551312000000L, 60, 1551398400000L, 29, 1551484800000L, 36, 1551571200000L, 27, 1551657600000L, 54, 1551744000000L, 89, 1551830400000L, 50, 1551916800000L, 73, 1552003200000L, 52};

        String expectedJson = "{\"columns\":[[\"x\",1542412800000,1542499200000,1542585600000,1542672000000,1542758400000,1542844800000,1542931200000,1543017600000,1543104000000,1543190400000,1543276800000,1543363200000,1543449600000,1543536000000,1543622400000,1543708800000,1543795200000,1543881600000,1543968000000,1544054400000,1544140800000,1544227200000,1544313600000,1544400000000,1544486400000,1544572800000,1544659200000,1544745600000,1544832000000,1544918400000,1545004800000,1545091200000,1545177600000,1545264000000,1545350400000,1545436800000,1545523200000,1545609600000,1545696000000,1545782400000,1545868800000,1545955200000,1546041600000,1546128000000,1546214400000,1546300800000,1546387200000,1546473600000,1546560000000,1546646400000,1546732800000,1546819200000,1546905600000,1546992000000,1547078400000,1547164800000,1547251200000,1547337600000,1547424000000,1547510400000,1547596800000,1547683200000,1547769600000,1547856000000,1547942400000,1548028800000,1548115200000,1548201600000,1548288000000,1548374400000,1548460800000,1548547200000,1548633600000,1548720000000,1548806400000,1548892800000,1548979200000,1549065600000,1549152000000,1549238400000,1549324800000,1549411200000,1549497600000,1549584000000,1549670400000,1549756800000,1549843200000,1549929600000,1550016000000,1550102400000,1550188800000,1550275200000,1550361600000,1550448000000,1550534400000,1550620800000,1550707200000,1550793600000,1550880000000,1550966400000,1551052800000,1551139200000,1551225600000,1551312000000,1551398400000,1551484800000,1551571200000,1551657600000,1551744000000,1551830400000,1551916800000,1552003200000],[\"y0\",37,20,32,39,32,35,19,65,36,62,113,69,120,60,51,49,71,122,149,69,57,21,33,55,92,62,47,50,56,116,63,60,55,65,76,33,45,64,54,81,180,123,106,37,60,70,46,68,46,51,33,57,75,70,95,70,50,68,63,66,53,38,52,109,121,53,36,71,96,55,58,29,31,55,52,44,126,191,73,87,255,278,219,170,129,125,126,84,65,53,154,57,71,64,75,72,39,47,52,73,89,156,86,105,88,45,33,56,142,124,114,64],[\"y1\",22,12,30,40,33,23,18,41,45,69,57,61,70,47,31,34,40,55,27,57,48,32,40,49,54,49,34,51,51,51,66,51,94,60,64,28,44,96,49,73,30,88,63,42,56,67,52,67,35,61,40,55,63,61,105,59,51,76,63,57,47,56,51,98,103,62,54,104,48,41,41,37,30,28,26,37,65,86,70,81,54,74,70,50,74,79,85,62,36,46,68,43,66,50,28,66,39,23,63,74,83,66,40,60,29,36,27,54,89,50,73,52]],\"types\":{\"y0\":\"line\",\"y1\":\"line\",\"x\":\"x\"},\"names\":{\"y0\":\"#0\",\"y1\":\"#1\"},\"colors\":{\"y0\":\"#3DC23F\",\"y1\":\"#F34C44\"}}";

        // Act
        Chart actualChart = ChartParser.parse(expectedJson);

        // Assert
        int actualColumnCount = actualChart.columns.size();
        Assert.assertEquals(actualColumnCount, expectedColumnCount);

        Column actualFirstColumn = actualChart.columns.get(0);
        Assert.assertEquals(expectedFirstColumnColor, actualFirstColumn.color);
        Assert.assertEquals(expectedFirstColumnName, actualFirstColumn.name);
        Assert.assertEquals(expectedFirstColumnType, actualFirstColumn.type);
        Assert.assertArrayEquals(expectedFirstColumnPoints, actualFirstColumn.points, 0.00001f);

        Column actualSecondColumn = actualChart.columns.get(1);
        Assert.assertEquals(expectedSecondColumnColor, actualSecondColumn.color);
        Assert.assertEquals(expectedSecondColumnName, actualSecondColumn.name);
        Assert.assertEquals(expectedSecondColumnType, actualSecondColumn.type);
        Assert.assertArrayEquals(expectedSecondColumnPoints, actualSecondColumn.points, 0.00001f);
    }
}
