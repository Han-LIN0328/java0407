public class HeightMeasurement {

    // 計算兩點之間的距離
    public static double distance(double[] p1, double[] p2) {
        return Math.sqrt(Math.pow(p1[0] - p2[0], 2) + Math.pow(p1[1] - p2[1], 2));
    }

    // 透過兩點計算直線 (外積)
    public static double[] getLine(double[] p1, double[] p2) {
        return new double[]{
            p1[1] * p2[2] - p1[2] * p2[1],
            p1[2] * p2[0] - p1[0] * p2[2],
            p1[0] * p2[1] - p1[1] * p2[0]
        };
    }

    // 透過兩條直線計算交點 (外積)
    public static double[] getIntersection(double[] line1, double[] line2) {
        double[] pt = new double[]{
            line1[1] * line2[2] - line1[2] * line2[1],
            line1[2] * line2[0] - line1[0] * line2[2],
            line1[0] * line2[1] - line1[1] * line2[0]
        };
        // 轉回 2D 歐式座標 (將 Z 軸正規化為 1)
        if (pt[2] != 0) {
            pt[0] /= pt[2];
            pt[1] /= pt[2];
            pt[2] = 1.0;
        }
        return pt;
    }

    public static void main(String[] args) {
        // =================================================================
        // 請填入 pic1.jpg 照片中實際點擊到的像素座標 {x, y, 1.0}
        // =================================================================

        // 1. 找地板上「往畫面深處延伸」的兩條平行線 (求消失點 1)
        // 可以利用天花板的格線邊緣，或是走廊柱子的底部邊界
        double[] depthLine1_p1 = {120, 1080, 1.0}; // 左側延伸線-近端
        double[] depthLine1_p2 = {280, 700, 1.0}; // 左側延伸線-遠端
        double[] depthLine2_p1 = {1080, 1080, 1.0}; // 右側延伸線-近端
        double[] depthLine2_p2 = {920, 700, 1.0}; // 右側延伸線-遠端

        // 2. 找畫面中「橫向」的兩條平行線 (求消失點 2)
        // 可以利用天花板橫向的橫樑，或是後方機車棚的水平屋頂線
        double[] horizLine1_p1 = {100, 300, 1.0}; // 上方橫線-左端
        double[] horizLine1_p2 = {1100, 300, 1.0}; // 上方橫線-右端
        double[] horizLine2_p1 = {200, 600, 1.0}; // 下方橫線-左端
        double[] horizLine2_p2 = {1000, 600, 1.0}; // 下方橫線-右端

        // 3. 基準學生 (右側，身穿 JUST DO IT) 的頭頂與腳底座標
        double[] refTop = {850, 400, 1.0}; // JUST DO IT 頭頂
        double[] refBot = {850, 1100, 1.0}; // JUST DO IT 腳底
        double refHeightReal = 180.0;      // 基準真實身高為 180 公分

        // 4. 目標學生 (左側，身穿灰衣) 的頭頂與腳底座標
        double[] targetTop = {350, 410, 1.0}; // 灰衣同學 頭頂
        double[] targetBot = {350, 1100, 1.0}; // 灰衣同學 腳底

        // =================================================================
        // 以下為自動計算邏輯，不需修改
        // =================================================================

        // A. 計算兩個消失點 (Vanishing Points)
        double[] vp1 = getIntersection(getLine(depthLine1_p1, depthLine1_p2), getLine(depthLine2_p1, depthLine2_p2));
        double[] vp2 = getIntersection(getLine(horizLine1_p1, horizLine1_p2), getLine(horizLine2_p1, horizLine2_p2));

        // B. 連接兩個消失點，得到「消失線 (Vanish Line)」
        double[] vanishLine = getLine(vp1, vp2);

        // C. 將兩人的腳底連線，並向外延伸找出與消失線的交點 V
        double[] groundLine = getLine(refBot, targetBot);
        double[] vPoint = getIntersection(groundLine, vanishLine);

        // D. 從交點 V 連接到基準學生的頭頂，建立一條「180cm 的虛擬高度天花板線」
        double[] heightRefLine = getLine(vPoint, refTop);

        // E. 畫一條通過目標學生全身的垂直線，找出該位置的 180cm 基準頂點
        double[] targetVerticalLine = getLine(targetTop, targetBot);
        double[] targetRefTop = getIntersection(heightRefLine, targetVerticalLine);

        // F. 計算像素高度比例，換算實際身高
        double targetPixelHeight = distance(targetTop, targetBot);
        double refPixelHeightAtTarget = distance(targetRefTop, targetBot);

        double targetRealHeight = (targetPixelHeight / refPixelHeightAtTarget) * refHeightReal;

        System.out.println("====== Pic1 計算結果 ======");
        System.out.printf("基準學生 (JUST DO IT) 身高設定: %.1f 公分\n", refHeightReal);
        System.out.printf("計算出的目標學生 (灰色上衣) 身高為: %.2f 公分\n", targetRealHeight);
    }
}