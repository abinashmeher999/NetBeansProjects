#ifndef __POLYNOMIAL_HXX	// Control inclusion of header files
#define __POLYNOMIAL_HXX

/************ C++ Headers ************************************/
#include <iostream>	// Defines istream & ostream for IO
#include <vector>
#include <Fraction.hxx>
using namespace std;

/************ Project Headers ********************************/
//#include "Fraction.hxx"

/************ CLASS Declaration ******************************/
template <typename T, typename U> // Type of Value // Type of Coefficients 
class Poly {
public:

    // CONSTRUCTOR
    // -----------
    Poly(unsigned int = 0); // Uses default parameters. Overloads to
    // Poly(...); 

    // Copy Constructor 
    Poly(const Poly&); // Param cannot be changed (const)

    // DESTRUCTOR
    // ----------

    ~Poly() {
    } // No virtual destructor needed

    // BASIC ASSIGNEMENT OPERATOR
    // --------------------------
    Poly& operator=(const Poly&);

    // UNARY ARITHMETIC OPERATORS
    // --------------------------
    Poly operator-(); // Operand 'this' implicit
    Poly operator+();

    // BINARY ARITHMETIC OPERATORS
    // ---------------------------
    Poly operator+(const Poly&);
    Poly operator-(const Poly&);

    // ADVANCED ASSIGNEMENT OPERATORS
    // ------------------------------
    Poly& operator+=(const Poly&);
    Poly& operator-=(const Poly&);

    // BASIC I/O using FRIEND FUNCTIONS
    // --------------------------------
    template<class X,class Y>
    friend ostream& operator<<(ostream& os, const Poly<X, Y>& p);

    template<class X,class Y>
    friend istream& operator>>(istream& is, Poly<X, Y>& p);

    // METHODS
    // -------
    T Evaluate(const T&); // Evaluates the polynomial - use Horner's Rule

private:

    // DATA MEMBERS
    // ------------
    unsigned int degree_;
    std::vector<U> coefficients_;
    void trimzero();
};

#include "Polynomial.inl"

#endif // __POLYNOMIAL_HXX