package testing;

import java.util.Random;

public class firstclass {

	public static void main(String[] args) {
		//dimensions
		String output="";
	      int minD = 5;
	      int maxD = 15;
	      int m = (int)Math.floor(Math.random()*(maxD-minD+1)+minD);
	      int n=(int)Math.floor(Math.random()*(maxD-minD+1)+minD);
	      int[][] grid = new int[m][n];
	      //c value
	      int minC=1;
	      int maxC=4;
	      int c=(int)Math.floor(Math.random()*(maxC-minC+1)+minC);
	      
	      System.out.print("M"+m);
	      System.out.print("N"+n);
	      System.out.println("C"+c);
	      output=m+","+n+";"+c+";";
	      

	      
	      //positions

		      //positon of Neo
		      Random rand = new Random(); //instance of random class
		      int x = rand.nextInt(m); 
		      int y = rand.nextInt(n); 
		      grid[x][y]=1;
		      System.out.print("x"+x);
		      System.out.println("y"+y); 
		      //positon of telephone booth
		      int xTB = rand.nextInt(m); 
		      int yTB = rand.nextInt(n); 
		      while(grid[xTB][yTB]==1) {
			       xTB = rand.nextInt(m); 
			       yTB = rand.nextInt(n); 
		      }
		      grid[xTB][yTB]=1;
		      System.out.print("xTB"+xTB);
		      System.out.println("yTB"+yTB);


		      output+=x+","+y+";"+xTB+","+yTB+";";
	      
	      //hostages
	      String hostages="";
	      int minHostages=3;
	      int maxHostages=10;
	      int num_hostages=(int)Math.floor(Math.random()*(maxHostages-minHostages+1)+minHostages);
	      
	      for(int i=0;i<num_hostages;i++) {
		      int minDamage=1;
		      int maxDamage=99;
		      int damage=(int)Math.floor(Math.random()*(maxDamage-minDamage+1)+minDamage);
		      
		      int xH = rand.nextInt(m); 
		      int yH = rand.nextInt(n); 
		      while(grid[xH][yH]==1) {
			       xH = rand.nextInt(m); 
			       yH = rand.nextInt(n); 
		      }
		      grid[xH][yH]=1;
		      
	    	  System.out.print("xH"+xH);
	    	  System.out.print("yH"+yH);
	    	  System.out.println("Damage"+damage);
		      if(i==num_hostages-1) {
		    	  hostages+=xH+","+yH+","+damage;  
		      }
		      else {
		    	  hostages+=xH+","+yH+","+damage+",";
		      }
		      
	    	  
	      }
	      
	      //pills
	      String pills="";
	      int minPills=1;
	      int maxPills=maxHostages;
	      int num_Pills=(int)Math.floor(Math.random()*(maxPills-minPills+1)+minPills);
	      
	      for(int i=0;i<num_Pills;i++) {

		      
		      int xP = rand.nextInt(m); 
		      int yP = rand.nextInt(n); 
		      while(grid[xP][yP]==1) {
			       xP = rand.nextInt(m); 
			       yP = rand.nextInt(n); 
		      }
		      grid[xP][yP]=1;
		      
	    	  System.out.print("xP"+xP);
	    	  System.out.println("yP"+yP);
	    	  
	    	  
		      if(i==num_Pills-1) {
		    	  pills+=xP+","+yP;
		    	 
		      }
		      else {
		    	  pills+=xP+","+yP+","; 
		      }
	    	  
	      }
	      
	      //agents
	      String agents="";
	      int minAgents=5;
	      int maxAgents=(m*n)-(2+num_Pills+num_hostages);
	      int num_Agents=(int)Math.floor(Math.random()*(maxAgents-minAgents+1)+minAgents);
	      
	      for(int i=0;i<num_Agents;i++) {

		      
		      int xA = rand.nextInt(m); 
		      int yA = rand.nextInt(n); 
		      while(grid[xA][yA]==1) {
			       xA = rand.nextInt(m); 
			       yA = rand.nextInt(n); 
		      }
		      grid[xA][yA]=1;
		      
	    	  System.out.print("xA"+xA);
	    	  System.out.println("yA"+yA);
	    	  //agents+=xA+","+yA;
	    	  
		      if(i==num_Agents-1) {
		    	  agents+=xA+","+yA;
		    	 
		      }
		      else {
		    	  agents+=xA+","+yA+",";
		      }
	    	  
	    	  
	      }
	      
	      //PADS
	      String pads="";
	      int minPads=1;
	      int maxPads=((m*n)-(2+num_Pills+num_hostages+num_Agents));
	      int num_Pads=(int)Math.floor(Math.random()*(maxPads-minPads+1)+minPads)/2;
	      int xPad1=-1;
	      int yPad1=-1;
	      int xPad2=-1;
	      int yPad2=-1;
	      for(int i=0;i<num_Pads;i++) {

		      
		       xPad1 = rand.nextInt(m); 
		       yPad1 = rand.nextInt(n); 
		      while(grid[xPad1][yPad1]==1) {
		    	  xPad1 = rand.nextInt(m); 
		    	  yPad1 = rand.nextInt(n); 
		      }
		      grid[xPad1][yPad1]=1;
		      
	    	  xPad2 = rand.nextInt(m); 
	    	  yPad2 = rand.nextInt(n); 
		      while(grid[xPad2][yPad2]==1) {
		    	  xPad2 = rand.nextInt(m); 
		    	  yPad2 = rand.nextInt(n); 
		      }
		      grid[xPad2][yPad2]=1;
	    	  System.out.print("xPad1 "+xPad1+" ");
	    	  System.out.println("yPad1 "+yPad1+" ");
	    	  System.out.print("xPad2 "+xPad2+" ");
	    	  System.out.println("yPad2 "+yPad2+" ");
	    	  
	    	  System.out.print("xPad2 "+xPad2+" ");
	    	  System.out.println("yPad2 "+yPad2+" ");
	    	  System.out.print("xPad1 "+xPad1+" ");
	    	  System.out.println("yPad1 "+yPad1+" ");
		      
		      //pads+=xPad1+","+yPad1+","+xPad2+","+yPad2+",";
	    	  //System.out.print("xPad1 "+xPad1+" ");
	    	  //System.out.println("yPad1 "+yPad1+" ");
		      
		      if(i==num_Pads-1) {
		    	  pads+=xPad1+","+yPad1+","+xPad2+","+yPad2+","+xPad2+","+yPad2+","+xPad1+","+yPad1;
		    	 
		      }
		      else {
		    	  pads+=xPad1+","+yPad1+","+xPad2+","+yPad2+","+xPad2+","+yPad2+","+xPad1+","+yPad1+",";
		      }
	    	  
	      }

	      /*String pads="";
	      int minPads=1;
	      int maxPads=((m*n)-(2+num_Pills+num_hostages+num_Agents))/2;
	      int num_Pads=(int)Math.floor(Math.random()*(maxPads-minPads+1)+minPads);
	      int xPad1=-1;
	      int yPad1=-1;
	      int xPad2=-1;
	      int yPad2=-1;
	      
	      for(int i=0;i<num_Pads;i++) {

		      
		       xPad1 = rand.nextInt(m); 
		       yPad1 = rand.nextInt(n); 
		      while(grid[xPad1][yPad1]==1) {
		    	  xPad1 = rand.nextInt(m); 
		    	  yPad1 = rand.nextInt(n); 
		      }
		      grid[xPad1][yPad1]=1;
		      
	    	  //System.out.print("xPad1 "+xPad1+" ");
	    	  //System.out.println("yPad1 "+yPad1+" ");
	    	  
	      }
	      for(int i=0;i<num_Pads;i++) {

		      
		       xPad2 = rand.nextInt(m); 
		       yPad2 = rand.nextInt(n); 
		      while(grid[xPad2][yPad2]==1) {
		    	  xPad2 = rand.nextInt(m); 
		    	  yPad2 = rand.nextInt(n); 
		      }
		      grid[xPad2][yPad2]=1;
		      
	    	  //System.out.print("xPad2 "+xPad2+" ");
	    	  //System.out.println("yPad2 "+yPad2+" ");
	    	  
	      }
    	  System.out.print("xPad1 "+xPad1+" ");
    	  System.out.println("yPad1 "+yPad1+" ");
    	  System.out.print("xPad2 "+xPad2+" ");
    	  System.out.println("yPad2 "+yPad2+" ");
    	  
    	  System.out.print("xPad2 "+xPad2+" ");
    	  System.out.println("yPad2 "+yPad2+" ");
    	  System.out.print("xPad1 "+xPad1+" ");
    	  System.out.println("yPad1 "+yPad1+" ");
    	  
    	  */
	      
    	  output+=agents+";"+pills+";"+pads+";"+hostages;
    	  System.out.println(output);
	}

}
