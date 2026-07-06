public class Main {
    static final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; //up,right,down,left

    public static void main(String[] args) throws Exception {
        try{
            AstarAlgorithm.solve();
        }catch(OutOfMemoryError e){
            throw new Exception("Out of memory. Available memory is not enough to solve this problem");
        }
    }
}