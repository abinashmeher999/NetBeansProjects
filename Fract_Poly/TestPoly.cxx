#include <iostream>
using namespace std;

#include "Fraction.hxx"
#include "Polynomial.hxx"

void TestPoly()
{
	cout << "\nTest Poly Data Type" << endl;

	// Polynomial with int value and int coefficients
	Poly<int, int> p(10);

	cout << "Input Poly<int, int>: p(x)" << endl;
	cin >> p;
	cout << "\np(x) = " << p << endl;

	int x = 5;
	cout << "p(" << x << ") = " << p.Evaluate(5) << endl;

	Poly<int, int> q = p;
	cout << "Copied Polynomial: " << q << endl;

	Poly<int, int> r;
	r = p;
	cout << "Assigned Polynomial: " << r << endl;

	r = -p;
	cout << "Negated Polynomial -p(x) = " << r << endl;

	cout << "Input Poly<int, int>: q(x)" << endl;
	cin >> q;
	cout << "\nq(x) = " << q << endl;

	r = p + q;
	cout << "p(x) + q(x) = " << r << endl;

	r = p - q;
	cout << "p(x) - q(x) = " << r << endl;

	p += q;
	cout << "p(x) <-- p(x) + q(x): " << p << endl;

	q -= p;
	cout << "q(x) <-- q(x) - p(x): " << q << endl;

	// Polynomial with Fraction value and int coefficients
	Poly<Fraction, int> pFi(10);

	cout << "Input Poly<Fraction, int>: pFi(x)" << endl;
	cin >> pFi;
	cout << "pFi(x) = " << pFi << endl;

	Fraction f;
	cout << "Input Fraction" << endl;
	cin >> f;
	cout << "At " << f << ": " << pFi.Evaluate(f) << endl;

	// Polynomial with Fraction value and Fraction coefficients
	Poly<Fraction, Fraction> piF(10);

	cout << "Input Poly<Fraction, Fraction>: piF(x)" << endl;
	cin >> piF;
	cout << "piF(x) = " << piF << endl;

	cout << "At " << f << ": " << piF.Evaluate(f) << endl;

	return;
}