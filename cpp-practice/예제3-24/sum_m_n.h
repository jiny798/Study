#pragma once


// main ���� sum_m_n(10, 20) �� ȣ���Ͽ� ��� ���� 
int sum_m_n(int m, int n)
{
	int sum = 0;
	for (int i = m; i <= n; i++) 
	{
		sum += i;
	}
	return sum;
}