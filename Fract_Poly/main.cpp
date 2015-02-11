/* 
 * File:   main.cpp
 * Author: abinashmeher999
 *
 * Created on 10 February, 2015, 12:35 AM
 */

#include <cstdlib>
#include <iostream>
#include <Fraction.hxx>
#include <Polynomial.hxx>
using namespace std;

/*
 * 
 */

int main(int argc, char** argv) {
    int option = 0, sec_option = 0;
    Fraction f1, f2;
    while (option != -1) {
        cout << "1: Unary operations\n";
        cout << "2: Binary operations\n";
        cout<<"3: Exit\n";
        cin >> option;
        switch (option) {
            case 1:
                while (sec_option != -1) {
                    cout << "1:+ 2:- 3:! 4:Back\n";
                    cin>>sec_option;
                    switch (sec_option) {
                        case 1:
                            cout<<"f1 :";
                            cin>>f1;
                            cout << "Ans: " << (-f1) << "\n";
                            sec_option = 0;
                            break;
                        case 2:
                            cout<<"f1 :";
                            cin>>f1;
                            cout << "Ans: " << (+f1) << "\n";
                            sec_option = 0;
                            break;
                        case 3:
                            cout<<"f1 :";
                            cin>>f1;
                            cout << "Ans: " << (!f1) << "\n";
                            sec_option = 0;
                            break;
                        case 4:
                            sec_option = -1;
                            break;
                        default:

                            cout << "Invalid input, try again\n";
                            sec_option = 0;
                            break;
                    }
                }
                option = 0;
                break;
            case 2:
                while (sec_option != -1) {
                    cout << "1:+ 2:- 3:* 4:/ 5:% 6:Back\n";
                    cin>>sec_option;
                    switch (sec_option) {
                        case 1:
                            cout<<"f1 :";
                            cin>>f1;
                            cout<<"f2 :";
                            cin>>f2;
                            cout << "Ans :" << (f1 + f2) << "\n";
                            sec_option = 0;
                            break;
                        case 2:
                            cout<<"f1 :";
                            cin>>f1;
                            cout<<"f2 :";
                            cin>>f2;
                            cout << "Ans :" << (f1 - f2) << "\n";
                            sec_option = 0;
                            break;
                        case 3:
                            cout<<"f1 :";
                            cin>>f1;
                            cout<<"f2 :";
                            cin>>f2;
                            cout << "Ans :" << (f1 * f2) << "\n";
                            sec_option = 0;
                            break;
                        case 4:
                            cout<<"f1 :";
                            cin>>f1;
                            cout<<"f2 :";
                            cin>>f2;
                            cout << "Ans :" << (f1 / f2) << "\n";
                            sec_option = 0;
                            break;
                        case 5:
                            cout<<"f1 :";
                            cin>>f1;
                            cout<<"f2 :";
                            cin>>f2;
                            cout << "Ans :" << (f1 % f2) << "\n";
                            sec_option = 0;
                            break;
                        case 6:
                            sec_option = -1;
                            break;
                        default:

                            cout << "Invalid input, try again\n";
                            sec_option = 0;
                            break;
                    }
                }
                option = 0;
                break;
            case 3:
                cout<<"Exiting\n";
                option = -1;
                break;
            default:
                cout << "Invalid input, Try again\n";
                option = 0;
                break;
        }
    }
    //    cout << "\nTest Fraction Data Type" << endl;
    //
    //    // CONSTRUCTORS
    //    // ------------
    //    Fraction f1(5, 3);
    //    Fraction f2(7.2);
    //    Fraction f3;
    //
    //    cout << "Fraction f1(5, 3) = " << f1 << endl;
    //    cout << "Fraction f2(7.2) = " << f2 << endl;
    //    cout << "Fraction f3 = " << f3 << endl;
    //
    //    // BASIC ASSIGNEMENT OPERATOR
    //    // --------------------------
    //    // Fraction& operator=(const Fraction&);
    //    cout << "Assingment (Before): f3 = " << f3 << ". f1 = " << f1 << endl;
    //    f3 = f1;
    //    cout << "Assingment (After): f3 = " << f3 << ". f1 = " << f1 << endl;
    //
    //    f3 = Fraction::sc_fUnity;
    //
    //    // UNARY ARITHMETIC OPERATORS
    //    // --------------------------
    //    // Fraction operator-();		// Operand 'this' implicit
    //    f3 = -f1;
    //    cout << "Unary Minus: f3 = " << f3 << ". f1 = " << f1 << endl;
    //
    //    // Fraction operator+();
    //
    //    // Fraction operator--();		// Pre-decrement. Dividendo
    //    f3 = Fraction::sc_fUnity;
    //    cout << "Pre-Decrement (Before): f3 = " << f3 << ". f1 = " << f1 << endl;
    //    f3 = --f1;
    //    cout << "Pre-Decrement (After): f3 = " << f3 << ". f1 = " << f1 << endl;
    //
    //    // Fraction operator--(int);	// Post-decrement. Lazy Dividendo
    //    f3 = Fraction::sc_fUnity;
    //    cout << "Post-Decrement (Before): f3 = " << f3 << ". f1 = " << f1 << endl;
    //    f3 = f1--;
    //    cout << "Post-Decrement (After): f3 = " << f3 << ". f1 = " << f1 << endl;
    //
    //    // Fraction operator++();		// Pre-increment. Componendo
    //    f3 = Fraction::sc_fUnity;
    //    cout << "Pre-Increment (Before): f3 = " << f3 << ". f1 = " << f1 << endl;
    //    f3 = ++f1;
    //    cout << "Pre-Increment (After): f3 = " << f3 << ". f1 = " << f1 << endl;
    //
    //    // Fraction operator++(int);	// Post-increment. Lazy Componendo
    //    f3 = Fraction::sc_fUnity;
    //    cout << "Post-Increment (Before): f3 = " << f3 << ". f1 = " << f1 << endl;
    //    f3 = f1++;
    //    cout << "Post-Increment (After): f3 = " << f3 << ". f1 = " << f1 << endl;
    //
    //    // BINARY ARITHMETIC OPERATORS USING FRIEND FUNCTIONS
    //    // --------------------------------------------------
    //    // friend Fraction operator+(const Fraction&, const Fraction&);
    //    f1 = Fraction(5, 12);
    //    f2 = Fraction(7, 18);
    //    f3 = f1 + f2;
    //    cout << "Binary Plus: f3 = " << f3 << ". f1 = " << f1 << ". f2 = " << f2 << endl;
    //
    //    // friend Fraction operator-(const Fraction&, const Fraction&);
    //    f1 = Fraction(16, 3);
    //    f2 = Fraction(22, 13);
    //    f3 = f1 - f2;
    //    cout << "Binary Minus: f3 = " << f3 << ". f1 = " << f1 << ". f2 = " << f2 << endl;
    //
    //    // friend Fraction operator*(const Fraction&, const Fraction&);
    //    f1 = Fraction(5, 12);
    //    f2 = Fraction(18, 25);
    //    f3 = f1 * f2;
    //    cout << "Multiply: f3 = " << f3 << ". f1 = " << f1 << ". f2 = " << f2 << endl;
    //
    //    // friend Fraction operator/(const Fraction&, const Fraction&);
    //    f1 = Fraction(5, 12);
    //    f2 = Fraction(7, 18);
    //    f3 = f1 / f2;
    //    cout << "Divide: f3 = " << f3 << ". f1 = " << f1 << ". f2 = " << f2 << endl;
    //
    //    // friend Fraction operator%(const Fraction&, const Fraction&);
    //    f1 = Fraction(5, 12);
    //    f2 = Fraction(7, 18);
    //    f3 = f1 % f2;
    //    cout << "Residue: f3 = " << f3 << ". f1 = " << f1 << ". f2 = " << f2 << endl;
    //
    //    // BINARY RELATIONAL OPERATORS
    //    // ---------------------------
    //    // bool operator==(const Fraction&);
    //    f1 = Fraction(5, 12);
    //    f2 = Fraction(7, 18);
    //    bool bTest = f1 == f2;
    //    cout << "Equal: Test = " << ((bTest) ? "true" : "false") << ". f1 = " << f1 << ". f2 = " << f2 << endl;
    //
    //    // bool operator!=(const Fraction&);
    //    bTest = f1 != f2;
    //    cout << "Not Equal: Test = " << ((bTest) ? "true" : "false") << ". f1 = " << f1 << ". f2 = " << f2 << endl;
    //
    //    // bool operator<(const Fraction&);
    //    bTest = f1 < f2;
    //    cout << "Less: Test = " << ((bTest) ? "true" : "false") << ". f1 = " << f1 << ". f2 = " << f2 << endl;
    //
    //    // bool operator<=(const Fraction&);
    //    f1 = Fraction(5, 12);
    //    f2 = Fraction(7, 18);
    //    f3 = Fraction(5, 12);
    //    bTest = f1 <= f2;
    //    cout << "Less Equal: Test = " << ((bTest) ? "true" : "false") << ". f1 = " << f1 << ". f2 = " << f2 << endl;
    //    bTest = f1 <= f3;
    //    cout << "Less Equal: Test = " << ((bTest) ? "true" : "false") << ". f1 = " << f1 << ". f3 = " << f3 << endl;
    //
    //    // bool operator>(const Fraction&);
    //    bTest = f1 > f2;
    //    cout << "Greater: Test = " << ((bTest) ? "true" : "false") << ". f1 = " << f1 << ". f2 = " << f2 << endl;
    //
    //    // bool operator>=(const Fraction&);
    //    bTest = f1 >= f2;
    //    cout << "Greater Equal: Test = " << ((bTest) ? "true" : "false") << ". f1 = " << f1 << ". f2 = " << f2 << endl;
    //    bTest = f1 >= f3;
    //    cout << "Greater Equal: Test = " << ((bTest) ? "true" : "false") << ". f1 = " << f1 << ". f3 = " << f3 << endl;
    //
    //    // ADVANCED ASSIGNEMENT OPERATORS
    //    // ------------------------------
    //    // Fraction& operator+=(const Fraction&);
    //    f1 = Fraction(5, 12);
    //    f2 = Fraction(7, 18);
    //    f3 = f2;
    //    f2 += f1;
    //    cout << "+=: f2 = " << f2 << ". f1 = " << f1 << ". f2 (before) = " << f3 << endl;
    //    f3 = f2;
    //    f2 += f2;
    //    cout << "+=: f2 = " << f2 << ". f2 (before) = " << f3 << endl;
    //
    //    // Fraction& operator-=(const Fraction&);
    //    f1 = Fraction(5, 12);
    //    f2 = Fraction(7, 18);
    //    f3 = f2;
    //    f2 -= f1;
    //    cout << "-=: f2 = " << f2 << ". f1 = " << f1 << ". f2 (before) = " << f3 << endl;
    //    f3 = f2;
    //    f2 -= f2;
    //    cout << "-=: f2 = " << f2 << ". f2 (before) = " << f3 << endl;
    //
    //    // Fraction& operator*=(const Fraction&);
    //    f1 = Fraction(5, 12);
    //    f2 = Fraction(7, 18);
    //    f3 = f2;
    //    f2 *= f1;
    //    cout << "*=: f2 = " << f2 << ". f1 = " << f1 << ". f2 (before) = " << f3 << endl;
    //    f3 = f2;
    //    f2 *= f2;
    //    cout << "*=: f2 = " << f2 << ". f2 (before) = " << f3 << endl;
    //
    //    // Fraction& operator/=(const Fraction&);
    //    f1 = Fraction(5, 12);
    //    f2 = Fraction(7, 18);
    //    f3 = f2;
    //    f2 /= f1;
    //    cout << "/=: f2 = " << f2 << ". f1 = " << f1 << ". f2 (before) = " << f3 << endl;
    //    f3 = f2;
    //    f2 /= f2;
    //    cout << "/=: f2 = " << f2 << ". f2 (before) = " << f3 << endl;
    //
    //    // Fraction& operator%=(const Fraction&);
    //    f1 = Fraction(7, 18);
    //    f2 = Fraction(5, 12);
    //    f3 = f2;
    //    f2 %= f1;
    //    cout << "%=: f2 = " << f2 << ". f1 = " << f1 << ". f2 (before) = " << f3 << endl;
    //    f3 = f2;
    //    f2 %= f2;
    //    cout << "%=: f2 = " << f2 << ". f2 (before) = " << f3 << endl;
    //    cout << "------------------------------------------------" << endl;
    //
    //    cout << "\nTest Poly Data Type" << endl;
    //
    //    // Polynomial with int value and int coefficients
    //    Poly<int, int> p(10);
    //
    //    cout << "Input Poly<int, int>: p(x)" << endl;
    //    cin >> p;
    //    cout << "\np(x) = " << p << endl;
    //    cout.flush();
    //
    //    int x = 5;
    //    cout << "p(" << x << ") = " << p.Evaluate(5) << endl;
    //    cout.flush();
    //
    //    Poly<int, int> q = p;
    //    cout << "Copied Polynomial: " << q << endl;
    //
    //    Poly<int, int> r;
    //    r = p;
    //    cout << "Assigned Polynomial: " << r << endl;
    //
    //    r = -p;
    //    cout << "Negated Polynomial -p(x) = " << r << endl;
    //
    //    cout << "Input Poly<int, int>: q(x)" << endl;
    //    cin >> q;
    //    cout << "\nq(x) = " << q << endl;
    //
    //    r = p + q;
    //    cout << "p(x) + q(x) = " << r << endl;
    //
    //    r = p - q;
    //    cout << "p(x) - q(x) = " << r << endl;
    //
    //    p += q;
    //    cout << "p(x) <-- p(x) + q(x): " << p << endl;
    //
    //    q -= p;
    //    cout << "q(x) <-- q(x) - p(x): " << q << endl;
    //
    //    // Polynomial with Fraction value and int coefficients
    //    Poly<Fraction, int> pFi(10);
    //
    //    cout << "Input Poly<Fraction, int>: pFi(x)" << endl;
    //    cin >> pFi;
    //    cout << "pFi(x) = " << pFi << endl;
    //    Fraction f;
    //    cout << "Input Fraction" << endl;
    //    cin >> f;
    //    cout << "At " << f << ": " << pFi.Evaluate(f) << endl;
    //
    //    // Polynomial with Fraction value and Fraction coefficients
    //    Poly<Fraction, Fraction> piF(10);
    //
    //    cout << "Input Poly<Fraction, Fraction>: piF(x)" << endl;
    //
    //    cin >> piF;
    //    cout << "piF(x) = " << piF << "\n" << endl;
    //
    //    cout << "At " << f << ": " << piF.Evaluate(f) << endl;

    return 0;
}

