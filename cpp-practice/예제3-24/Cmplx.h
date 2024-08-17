#pragma once

class Cmplx {
private:
	double re;
	double im;

public:
	Cmplx(double, double);
	void prntCmplx(); // 복소수 출력
	inline double absCmplx(); // 복소수 절대값 
};


Cmplx::Cmplx(double RE, double IM) {
	re = RE;
	im = IM;
}