/*
 
 */
package optimalfitting;

public class OptimalFitting {
    
    public static void main(String[] args) {
        //test case 1
        String one = "CAGCACTTGGATTCTCGG";
        String two = "CAGCGTGG";
                
        //make sure that strand one is longer than strand 2
        DNA dna = new DNA(one, two);
        dna.fillMatrix(); 
        System.out.println("Table:");
        dna.printMatrix();
        System.out.println("----");
        System.out.println("Best Alignment:");
        String[] s = dna.buildPath();
        System.out.println(s[0]);
        System.out.println(s[1]);
        System.out.println("-------------------------");
        
        //test case 2
        one = "TCCCAGTTATGTCAGGGGACACGAGCATGCAGAGAC";
        two = "AATTGCCGCCGTCGTTTTCAGCAGTTATGTCAGATC";
        dna = new DNA(one, two);
        dna.fillMatrix();
        System.out.println("Table");        
        dna.printMatrix();
        System.out.println("----");
        System.out.println("Best Alignment:");
        s = dna.buildPath();
        System.out.println(s[0]);
        System.out.println(s[1]);
        System.out.println("-------------------------");
        
        
        
    }
    
}

class DNA{
    //set vars
    String strandOne, strandTwo;
    int[][] matrix;
    
    //constructor
    DNA(String o, String t){
        strandOne = o;
        strandTwo = t;
        matrix = new int[o.length() +1][t.length() +1];
        
        matrix[matrix.length-1][matrix[matrix.length-1].length-1] = 0;   
    }
    
    //run through optimization and fill matrix
    void fillMatrix(){
        for (int i = matrix.length-1; i >= 0; i--) {
            for (int j = matrix[i].length-1; j >= 0; j--) {
                matrix[i][j] = optimization(i, j);                
            }
        }
    }
    
   //print the matrix
    void printMatrix(){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                //simple formatting
                if(matrix[i][j] < 10){
                    System.out.print(matrix[i][j] + "  ");
                }
                else{
                    System.out.print(matrix[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
    
    //optimize the best pathTwo
    int optimization(int i, int j){
        //
        if(i == matrix.length-1){
            return 2 * (matrix[0].length-1 - j);
        }
        else if (j == matrix[0].length-1){
            return 2 * (matrix.length-1 - i);
        }
        else{
            int penalty;
            if(strandOne.charAt(i) == strandTwo.charAt(j)){
                penalty = 0;                
            }
            else{
                penalty = 1;
            }
            
            int opOne = matrix[i+1][j+1] + penalty;
            int opTwo = matrix[i+1][j] + 2;
            int opThree = matrix[i][j+1] + 2;
            
            return Math.min(Math.min(opOne, opTwo), opThree);
            
//            return Math.min(optimization(i+1, j+1) + penalty, 
//                    Math.min(optimization(i+1, j) + 2, 
//                            optimization(i, j+1) + 2));
        }        
    }
    
    String[] buildPath(){
        String pathOne = "";
        String pathTwo = "";
        //this whold the coordinates of the point we are on, we start at [0][0]
        int[] currentPoint = {0, 0};
        while(pathTwo.length() < strandOne.length()){
            //calculate the penalty that should occur
            int penalty = 0;
            if(strandOne.charAt(currentPoint[0]) != strandTwo.charAt(currentPoint[1])){
                penalty = 1;
            }
            
            //check to the right
            if(optimization(currentPoint[0], currentPoint[1] + 1) + 2 ==
                    matrix[currentPoint[0]][currentPoint[1]]){
                
                //add black space to topPath
                pathOne += "-" ;
                //continue with path two
                pathTwo += strandTwo.charAt(currentPoint[1]);
                
                
                //adjust where our new pointer is
                currentPoint[1] += 1;
            }
            
            //check below            
            else if(optimization(currentPoint[0]+1, currentPoint[1]) + 2 ==
                    matrix[currentPoint[0]][currentPoint[1]]){
                
                //add a blank space to pathTwo
                pathTwo += "-";
                //continue on with pathONe
                pathOne += strandOne.charAt(currentPoint[0]);
                
                //adjsut where the pointer is
                currentPoint[0] += 1;
            }
            
            //check diagonally
            else if(optimization(currentPoint[0] + 1, currentPoint[1] + 1) + penalty ==
                    matrix[currentPoint[0]][currentPoint[1]]){
                
                //add to both paths
                pathTwo += strandTwo.charAt(currentPoint[1]);
                pathOne += strandOne.charAt(currentPoint[0]);
                
                //adjsut pointer
                currentPoint[0] += 1;
                currentPoint[1] += 1;
            }            
        }
        String[] s = {pathOne, pathTwo};
        s = finishPaths(s);
        return s;
    }
    
    String[] finishPaths(String[] s){
        String one = s[0];
        //make sure its not missing letters on the end
        int count = 0;
        for (int i = 0; i < one.length(); i++) {
            if(one.charAt(i) != '-'){
                count++;
            }
        }
        
        //if there are letters we didn't include in the first loop
        if(count != strandOne.length()){
            for (int i = count; i < strandOne.length(); i++) {
                one += strandOne.charAt(i);
            }
        }
        
        //do the same for strand 2
        String two = s[1];
        //make sure its not missing letters on the end
        count = 0;
        for (int i = 0; i < two.length(); i++) {
            if(two.charAt(i) != '-'){
                count++;
            }
        }
        
        //if there are letters we didn't include in the first loop
        if(count != strandTwo.length()){
            for (int i = count; i < strandTwo.length(); i++) {
                two += strandTwo.charAt(i);
            }
        }
        s[0] = one;
        s[1] = two;
        return s;        
    }
}
