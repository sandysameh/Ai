package code;

public class Member {
	int x;
	int y;
	public Member(int m, int n) {

		boolean found;
		
		do {
			
			x=(int) (Math.random() * ((m-1 ) + 1));
			y=(int) (Math.random() * ((n-1 ) + 1));
		
			found = false;
			
			for(int i=0; i<Matrix.members.length; i++) {
				if(Matrix.members[i]!=null) {
					if(Matrix.members[i].x == x && Matrix.members[i].y == y) {
						found = true;
						break;
					}
				}
				
			}
			
		} while(found);
		
	  }

}
