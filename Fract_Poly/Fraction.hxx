#ifndef __FRACTION_HXX	// Control inclusion of header files
#define __FRACTION_HXX

/************ C++ Headers ************************************/
#include <iostream>	// Defines istream & ostream for IO
using namespace std;

/************ CLASS Declaration ******************************/
class Fraction {
public:

    // CONSTRUCTORS
    // ------------
    Fraction(int = 1, int = 1); // Uses default parameters. Overloads to
    // Fraction(int, int); 
    // Fraction(int);	
    // Fraction(); 

    // Constructor for explicit Conversion
    explicit Fraction(double); // explicit double to Fraction conversion

    // Copy Constructor
    Fraction(const Fraction&); // Param cannot be changed (const)

    // DESTRUCTOR
    // ----------
    ~Fraction(); // No virtual destructor needed

    // BASIC ASSIGNEMENT OPERATOR
    // --------------------------
    Fraction& operator=(const Fraction&);

    // UNARY ARITHMETIC OPERATORS
    // --------------------------
    Fraction operator-(); // Operand 'this' implicit
    Fraction operator+();
    Fraction& operator--(); // Pre-decrement. Dividendo. p/q <-- p/q - 1
    Fraction& operator++(); // Pre-increment. Componendo. p/q <-- p/q + 1
    Fraction operator--(int); // Post-decrement. Lazy Dividendo. p/q <-- p/q - 1. Returns old p/q
    Fraction operator++(int); // Post-increment. Lazy Componendo. p/q <-- p/q + 1. Returns old p/q

    // BINARY ARITHMETIC OPERATORS USING FRIEND FUNCTIONS
    // --------------------------------------------------
    friend Fraction operator+(const Fraction&, const Fraction&);
    friend Fraction operator-(const Fraction&, const Fraction&);
    friend Fraction operator*(const Fraction&, const Fraction&);
    friend Fraction operator/(const Fraction&, const Fraction&);
    friend Fraction operator%(const Fraction&, const Fraction&);

    // BINARY RELATIONAL OPERATORS
    // ---------------------------
    bool operator==(const Fraction&);
    bool operator!=(const Fraction&);
    bool operator<(const Fraction&);
    bool operator<=(const Fraction&);
    bool operator>(const Fraction&);
    bool operator>=(const Fraction&);

    // ADVANCED ASSIGNEMENT OPERATORS
    // ------------------------------
    Fraction& operator+=(const Fraction&);
    Fraction& operator-=(const Fraction&);
    Fraction& operator*=(const Fraction&);
    Fraction& operator/=(const Fraction&);
    Fraction& operator%=(const Fraction&);

    // SPECIAL OPERATORS
    // -----------------
    Fraction operator!(); // Inverts a fraction. !(p/q) = q/p

    // BASIC I/O using FRIEND FUNCTIONS
    // --------------------------------
    friend ostream& operator<<(ostream&, const Fraction&);
    friend istream& operator>>(istream&, Fraction&);

    // CONSTANTS OF DATATYPE
    // ---------------------
    static const Fraction sc_fUnity; // Defines 1/1
    static const Fraction sc_fZero; // Defines 0/1

    // STATIC UTILITY FUNCTIONS
    // ------------------------

    static const int precision() {
        return 1000000;
    };
    static int gcd(int, int); // Finds the gcd for two +ve integers
    static int lcm(int, int); // Finds the lcm for two +ve integers

protected:

    // COMPONENT FUNCTIONS
    //--------------------

    int GetNumerator() {
        return iNumerator_;
    }

    unsigned int GetDenominator() {
        return uiDenominator_;
    }

private:

    // DATA MEMBERS
    // ------------
    int iNumerator_; // The Numerator
    unsigned int uiDenominator_; // The Denominator 

    // OTHER METHOD MEMBERS
    // --------------------
    Fraction& Normalize(); // Normalizes a fraction
};

#include "Fraction.inl" // Methods are implemented here

#endif // __FRACTION_HXX