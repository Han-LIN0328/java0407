public class Pic3HeightMeasurement {

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
        // 請用小畫家打開 pic3.jpg，把以下 (x, y) 替換成你滑鼠點擊到的實際像素座標
        // 格式為：{x座標, y座標, 1.0}
        // =================================================================

        // 1. 找地板上「往深處延伸」的兩條平行線 (求消失點 1)
        double[] depthLine1_p1 = {150, 900, 1.0}; // 左側延伸線-近端 (請替換)
        double[] depthLine1_p2 = {300, 550, 1.0}; // 左側延伸線-遠端 (請替換)
        double[] depthLine2_p1 = {1050, 900, 1.0}; // 右側延伸線-近端 (請替換)
        double[] depthLine2_p2 = {900, 550, 1.0}; // 右側延伸線-遠端 (請替換)

        // 2. 找地板上「橫向」的兩條平行線 (求消失點 2)
        double[] horizLine1_p1 = {150, 750, 1.0}; // 近處橫縫-左端 (請替換)
        double[] horizLine1_p2 = {1050, 750, 1.0}; // 近處橫縫-右端 (請替換)
        double[] horizLine2_p1 = {200, 500, 1.0}; // 遠處橫縫-左端 (請替換)
        double[] horizLine2_p2 = {1000, 500, 1.0}; // 遠處橫縫-右端 (請替換)

        // 3. 基準學生 (身穿 JUST DO IT) 的頭頂與腳底座標
        double[] refTop = {600, 380, 1.0}; // JUST DO IT 頭頂 (請替換)
        double[] refBot = {600, 850, 1.0}; // JUST DO IT 腳底 (請替換)
        double refHeightReal = 180.0;      // 基準真實身高為 180 公分

        // 4. 目標學生 (左側，身穿黑衣) 的頭頂與腳底座標
        double[] targetTop = {350, 380, 1.0}; // 黑衣同學 頭頂 (請替換)
        double[] targetBot = {350, 850, 1.0}; // 黑衣同學 腳底 (請替換)

        // =================================================================
        // 以下為自動計算邏輯，不需修改
        // =================================================================

        // A. 計算兩個消失點 (Vanishing Points)
        double[] vp1 = getIntersection(getLine(depthLine1_p1, depthLine1_p2), getLine(depthLine2_p1, depthLine2_p2));
        double[] vp2 = getIntersection(getLine(horizLine1_p1, horizLine1_p2), getLine(horizLine2_p1, horizLine2_p2));

        // B. 連接兩個消失點，得到「消失線 / 地平線 (Vanish Line)」
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

        System.out.println("====== Pic3 計算結果 ======");
        System.out.printf("基準學生 (JUST DO IT) 身高設定: %.1f 公分\n", refHeightReal);
        System.out.printf("計算出的目標學生 (黑衣) 身高為: %.2f 公分\n", targetRealHeight);
    }
}
