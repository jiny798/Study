#pragma once

class Cmplx {
private:
	double re;
	double im;

public:
	Cmplx(double, double);
	void prntCmplx(); // ���Ҽ� ���
	inline double absCmplx(); // ���Ҽ� ���밪 
};


Cmplx::Cmplx(double RE, double IM) {
	re = RE;
	im = IM;
}