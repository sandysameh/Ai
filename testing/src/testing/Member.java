package testing;

public class Member {
	int x;
	int y;
	public Member(int m, int n) {

		boolean found;
		
		do {
			
			x=(int) (Math.random() * ((m-1 ) + 1));
			y=(int) (Math.random() * ((n-1 ) + 1));
		
			found = false;
			
			for(int i=0; i<TheMatrix.members.length; i++) {
				if(TheMatrix.members[i]!=null) {
					if(TheMatrix.members[i].x == x && TheMatrix.members[i].y == y) {
						found = true;
						break;
					}
				}
				
			}
			
		} while(found);
		
	  }

}
