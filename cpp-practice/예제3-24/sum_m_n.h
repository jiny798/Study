#pragma once


// main 에서 sum_m_n(10, 20) 를 호출하여 사용 가능 
int sum_m_n(int m, int n)
{
	int sum = 0;
	for (int i = m; i <= n; i++) 
	{
		sum += i;
	}
	return sum;
}