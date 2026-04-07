public class KisaragiProof {
    public static void main(String[] args) {
        System.out.println("=== 如月車站平行線收斂證明 (Pinhole Camera Model) ===");
        
        // 參數設定 (參考簡報公式推導)
        double f = 50.0;     // 焦距 f
        double Z0 = 10.0;    // 起始深度 Z0
        double c = 2.0;      // 深度延伸速度 (參數 t 的係數)
        
        // 左鐵軌起始點 X0 = -5, 右鐵軌起始點 X0 = 5
        // 鐵軌高度一致 Y0 = -3
        // 因為是平行於 Z 軸延伸，所以 a = 0, b = 0
        
        System.out.printf("%-10s | %-15s | %-15s | %-15s\n", "深度參數 (t)", "左鐵軌 X 座標", "右鐵軌 X 座標", "共同 Y 座標");
        System.out.println("------------------------------------------------------------------");

        // 模擬深度 t 不斷增加，直到無限遠
        int[] tValues = {1, 10, 100, 1000, 10000, 100000, 1000000};
        
        for (int t : tValues) {
            double z = Z0 + c * t; // Z(t) = Z0 + ct
            
            // x(t) = -f(X0 + at) / (Z0 + ct)
            double leftX = -f * (-5.0) / z;  
            double rightX = -f * (5.0) / z;  
            
            // y(t) = -f(Y0 + bt) / (Z0 + ct)
            double y = -f * (-3.0) / z;      

            System.out.printf("%-11d | %-16.6f | %-16.6f | %-16.6f\n", t, leftX, rightX, y);
        }
        
        System.out.println("------------------------------------------------------------------");
        System.out.println("結論：隨著深度 t 趨近無限大，左側與右側的 X 座標皆收斂至 0，印證了平行線在消失點交會的現象。");
    }
}