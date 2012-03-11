import javax.swing.JButton;

public class Board {

	int N;
	int arr[][];
	protected int count = 0;

	/*
	 * 0- is empty
	 * 1- is O 
	 * 2 -is x
	 * arr[i][N] and arr[N][j] is o--
	 */
	public Board(int n) {
		N = n;
		arr = new int[N+2][N+2];
	}
	
	public void reset()
	{
		count = 0;
		for(int i = 0;i < N+2;i++)
		{
			for(int j = 0;j < N+2;j++)
			{
				arr[i][j] = 0;
			}
		}
		
	}
	
	public void undomov(int i,int j)
	{
		count--;
		int n;
		if(arr[i][j] == 1)
		{
			n = 0;
		}
		else
		{
			n = 1;
		}
		arr[i][N+n] --;
		arr[N+n][j] --;
		if(i == j)
		{
			arr[N+n][N]--;			
		}
		else
			if(i+j == N-1)
			{
				arr[N+n][N+1]--;
			}
		
	}
	public int[] move(int x, int y) {
		int ans[] = new int[4];

			if (count % 2 == 0) {
				if (arr[x][y] == 0) {
					arr[x][y] = 1;
					count++;
					arr[x][N]++;
					arr[N][y]++;
					if(x == y)
					{
						arr[N][N]++;
					}
					else
						if((x+y) == N-1 )
						{
							arr[N][N+1]++;
						}
				}
			} else {
				if (arr[x][y] == 0) {
					arr[x][y] = 2;
					count++;
					arr[x][N+1]++;
					arr[N+1][y]++;
				}
				
				if(x == y)
				{
					arr[N+1][N]++;
				}
				else
					if((x+y) == N-1 )
					{
						arr[N+1][N+1]++;
					}
			}
		
		//return 0;
		status(ans);
		return ans;
	}
	
	public void status(int ar[])
	{
		//int f_o,f_x;
		for(int i = 0;i < N;i++)
		{
			if((arr[i][N] == N)||(arr[N][i] == N))//||(arr[N][N] == N)||(arr[N][N+1] == N))
			{
				ar[0] = 1;
				ar[1] = (arr[i][N] == N)?1:0;
				ar[1] = (arr[N][i] == N)?2:0;
				ar[1] = ((arr[N][N] == N)||(arr[N][N+1] == N))?3:0;
				ar[2] = i;
				if((arr[N][N] == N))
				{
					ar[2] = 1;
				}
				else
					if((arr[N][N+1] == N))
					{
						ar[2] = 2;
					}
					
				ar[3] = 0;
				return;
			}
			else if((arr[i][N+1] == N)||(arr[N+1][i] == N))//||(arr[N+1][N] == N)||(arr[N+1][N+1] == N))
			{
				ar[0] = 2;
				ar[1] = (arr[i][N] == N)?1:0;
				ar[1] = (arr[N][i] == N)?2:0;
				ar[1] = ((arr[N+1][N] == N)||(arr[N+1][N+1] == N))?3:0;
				ar[2] = i;
				if((arr[N+1][N] == N))
				{
					ar[2] = 1;
				}
				else
					if((arr[N+1][N+1] == N))
					{
						ar[2] = 2;
					}
					
				ar[3] = 0;
				return;
			}
			
		}
		if((arr[N][N] == N)||(arr[N][N+1] == N))
		{
			ar[0] = 1;
			ar[1] = 3;
			ar[2] = (arr[N][N] == N)?1:2;
		}
		if((arr[N+1][N] == N)||(arr[N+1][N+1] == N))
		{
			ar[0] = 2;
			ar[1] = 3;
			ar[2] = (arr[N+1][N] == N)?1:2;
		}
		
		int s = 0;
		for(int ii = 0; ii < N;ii++)
		{
			int jj = N-ii-1;
			if(arr[ii][jj] == 1)
			{
				s++;
			}
		}
		if(s == N)
		{
			ar[0] = 1;
			ar[1] = 3;
			ar[2] = 2;
			
			return;
		}
		
		s = 0;
		for(int ii = 0; ii < N;ii++)
		{
			int jj = N-ii-1;
			if(arr[ii][jj] == 2)
			{
				s++;
			}
		}
		if(s == N)
		{
			ar[0] = 2;
			ar[1] = 3;
			ar[2] = 2;
			
			return;
		}
		
		if(count == N*N)
		{
			ar[0] = 3;
			ar[1] = ar[2] = ar[3] = 0;
			return;
		}
		else
		{
			ar[0] = ar[1] = ar[2] = -1;
			ar[3] = count%2 ;
			return;
		}
		
	
	}
	
	public int[] AIM()
	{
		int c = AI();
			if(c == 0)
				Random();
			else {
				
				int rw = ((c-1)/3);
				int col = c%3;
				arr[rw][col] = 2;
				return move(rw,col);
				//btnEmpty[computerButton].setText("O");
				//btnEmpty[computerButton].setEnabled(false);
			}
			return null;
	}
	
	public int AI()
	{
		
		if(arr[0][0]== 1 && arr[0][1]== 1 && arr[0][2]== 0)
			return 3;
		else if(arr[1][0]== 1 && arr[1][1]== 1 && arr[1][2]== 0)
			return 6;
		else if(arr[2][0]== 1 && arr[2][1]== 1 && arr[2][2]== 0)
			return 9;
		
		else if(arr[0][1]== 1 && arr[0][2]== 1 && arr[0][0]== 0)
			return 1;
		else if(arr[1][1]== 1 && arr[1][2]== 1 && arr[1][0]== 0)
			return 4;
		else if(arr[2][1]== 1 && arr[2][2]== 1 && arr[2][0]== 0)
			return 7;
		
		else if(arr[0][0]== 1 && arr[0][2]== 1 && arr[0][1]== 0)
			return 2;
		else if(arr[1][0]== 1 && arr[1][2]== 1 && arr[1][1]== 0)
			return 5;
		else if(arr[2][0]== 1 && arr[2][2]== 1 && arr[2][1]== 0)
			return 8;
		
		else if(arr[0][0]== 1 && arr[1][0]== 1 && arr[2][0]== 0)
			return 7;
		else if(arr[0][1]== 1 && arr[1][1]== 1 && arr[2][1]== 0)
			return 8;
		else if(arr[0][2]== 1 && arr[1][2]== 1 && arr[2][2]== 0)
			return 9;
		
		else if(arr[1][0]== 1 && arr[2][0]== 1 && arr[0][0]== 0)
			return 1;
		else if(arr[1][1]== 1 && arr[2][1]== 1 && arr[0][1]== 0)
			return 2;
		else if(arr[1][2]== 1 && arr[2][2]== 1 && arr[0][2]== 0)
			return 3;
		
		else if(arr[0][0]== 1 && arr[2][0]== 1 && arr[1][0]== 0)
			return 4;
		else if(arr[0][1]== 1 && arr[2][1]== 1 && arr[1][1]== 0)
			return 5;
		else if(arr[0][2]== 1 && arr[2][2]== 1 && arr[1][2]== 0)
			return 6;
		
		else if(arr[0][0]== 1 && arr[1][1]== 1 && arr[2][2]== 0)
			return 9;
		else if(arr[1][1]== 1 && arr[2][2]== 1 && arr[0][0]== 0)
			return 1;
		else if(arr[0][0]== 1 && arr[2][2]== 1 && arr[1][1]== 0)
			return 5;
		
		else if(arr[0][2]== 1 && arr[1][1]== 1 && arr[2][0]== 0)
			return 7;
		else if(arr[2][0]== 1 && arr[1][1]== 1 && arr[0][2]== 0)
			return 3;
		else if(arr[2][0]== 1 && arr[0][2]== 1 && arr[1][1]== 0)
			return 5;
		
		else if(arr[0][0]== 2 && arr[0][1]== 2 && arr[0][2]== 0)
			return 3;
		else if(arr[1][0]== 2 && arr[1][1]== 2 && arr[1][2]== 0)
			return 6;
		else if(arr[2][0]== 2 && arr[2][1]== 2 && arr[2][2]== 0)
			return 9;
		
		else if(arr[0][1]== 2 && arr[0][2]== 2 && arr[0][0]== 0)
			return 1;
		else if(arr[1][1]== 2 && arr[1][2]== 2 && arr[1][0]== 0)
			return 4;
		else if(arr[2][1]== 2 && arr[2][2]== 2 && arr[2][0]== 0)
			return 7;
		
		else if(arr[0][0]== 2 && arr[0][2]== 2 && arr[0][1]== 0)
			return 2;
		else if(arr[1][0]== 2 && arr[1][2]== 2 && arr[1][1]== 0)
			return 5;
		else if(arr[2][0]== 2 && arr[2][2]== 2 && arr[2][1]== 0)
			return 8;

		else if(arr[0][0]== 2 && arr[1][0]== 2 && arr[2][0]== 0)
			return 7;
		else if(arr[0][1]== 2 && arr[1][1]== 2 && arr[2][1]== 0)
			return 8;
		else if(arr[0][2]== 2 && arr[1][2]== 2 && arr[2][2]== 0)
			return 9;
		
		else if(arr[1][0]== 2 && arr[2][0]== 2 && arr[0][0]== 0)
			return 1;
		else if(arr[1][1]== 2 && arr[2][1]== 2 && arr[0][1]== 0)
			return 2;
		else if(arr[1][2]== 2 && arr[2][2]== 2 && arr[0][2]== 0)
			return 3;
		
		else if(arr[0][0]== 2 && arr[2][0]== 2 && arr[1][0]== 0)
			return 4;
		else if(arr[0][1]== 2 && arr[2][1]== 2 && arr[1][1]== 0)
			return 5;
		else if(arr[0][2]== 2 && arr[2][2]== 2 && arr[1][2]== 0)
			return 6;
		
		else if(arr[0][0]== 2 && arr[1][1]== 2 && arr[2][2]== 0)
			return 9;
		else if(arr[1][1]== 2 && arr[2][2]== 2 && arr[0][0]== 0)
			return 1;
		else if(arr[0][0]== 2 && arr[2][2]== 2 && arr[1][1]== 0)
			return 5;
		
		else if(arr[0][2]== 2 && arr[1][1]== 2 && arr[2][0]== 0)
			return 7;
		else if(arr[2][0]== 2 && arr[1][1]== 2 && arr[0][2]== 0)
			return 3;
		else if(arr[2][0]== 2 && arr[0][2]== 2 && arr[1][1]== 0)
			return 5;
		
		else if(arr[0][0]== 2 && arr[1][1]== 1 && arr[2][2]== 2)
			return 6;
			
		else if(arr[0][2]== 2 && arr[1][1]== 1 && arr[2][0]== 2) 
			return 4;
		
		else if(arr[1][1]== 0)
			return 5;
		
		else if(arr[0][0]== 0)
			return 1;
		else
			return 0;
		
		//return null;
	}
	
	public int[] Random()	{
		int r1,r2;
		
			r1 = r2 = 0;
			
				r1 = (int)(Math.random() * 2);
				r2 = (int)(Math.random() * 2);
			
			if(doRandomMove(r1,r2))	{
				//btnEmpty[random].setText("O");
				arr[r1][r2] = 2;
				return move(r1,r2);
				//btnEmpty[random].setEnabled(false);
			} else {
				Random();
			}
			return null;
		}
	
	public boolean doRandomMove(int i,int j)	{
		if(arr[i][j] == 2 || arr[i][j] == 1)
			return false;
		else	{
			return true;
		}
	}

}
