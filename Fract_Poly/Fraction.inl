/* 
 * File:   Fraction.inl
 * Author: abinashmeher999
 *
 * Created on 10 February, 2015, 2:16 AM
 */

#ifndef FRACTION_INL
#define	FRACTION_INL

#include <Fraction.hxx>

inline int Fraction::gcd(int a, int b) {
    int r = 100;
    if (a < 0) {
        a = -a;
    }
    if (b < 0) {
        b = -b;
    }
    if (b > a) {
        int t = b;
        b = a;
        a = t;
    }
    while (b > 0) {
        r = a % b;
        a = b;
        b = r;
    }
    return a;
}

inline int Fraction::lcm(int a, int b) {
    return (a * b) / gcd(a, b);
}

inline Fraction& Fraction::Normalize() {
    int gcd = Fraction::gcd(this->iNumerator_, this->uiDenominator_);

    this->iNumerator_ /= gcd;
    this->uiDenominator_ /= gcd;
    return *this;
}

inline Fraction::Fraction(int num, int den) {
    if (den < 0) {
        den = -den;
        num = -num;
    }
    this->iNumerator_ = num;
    this->uiDenominator_ = (unsigned int) den;
    this->Normalize();
}

inline Fraction::Fraction(double d) {
    this->iNumerator_=(int)(d*Fraction::precision());
    this->uiDenominator_=(unsigned int)Fraction::precision();
    this->Normalize();
}

inline Fraction::Fraction(const Fraction& cf) {
    this->iNumerator_ = cf.iNumerator_;
    this->uiDenominator_ = cf.uiDenominator_;
    this->Normalize();
}

inline Fraction::~Fraction(){}

inline Fraction& Fraction::operator=(const Fraction& F) {
    this->iNumerator_ = F.iNumerator_;
    this->uiDenominator_ = F.uiDenominator_;
    this->Normalize();
    return *this;
}

const Fraction Fraction::sc_fUnity = 1;
const Fraction Fraction::sc_fZero = 0;

inline Fraction Fraction::operator-() {
    Fraction f(-this->iNumerator_, this->uiDenominator_);
    return f;
}

inline Fraction Fraction::operator+() {
    return *this;
}

//Some BINARY ARITHMETIC

inline Fraction operator+(const Fraction& a, const Fraction& b) {
    Fraction f(a.iNumerator_ * b.uiDenominator_ + b.iNumerator_ * a.uiDenominator_, a.uiDenominator_ * b.uiDenominator_);
    return f;
}

inline Fraction operator-(const Fraction& a, const Fraction& b) {
    Fraction f(a.iNumerator_ * b.uiDenominator_ - b.iNumerator_ * a.uiDenominator_, a.uiDenominator_ * b.uiDenominator_);
    return f;
}

//UNARY OPERATORS

inline Fraction& Fraction::operator--() {
    *this = *this -sc_fUnity;
    return *this;
}
inline Fraction& Fraction::operator++() {
    *this = *this +sc_fUnity;
    return *this;
}

inline Fraction Fraction::operator--(int) {
    Fraction f(*this);
    *this = *this-sc_fUnity;
    return f;
}

inline Fraction Fraction::operator++(int) {
    Fraction f(*this);
    *this = *this+sc_fUnity;
    return f;
}

inline Fraction Fraction::operator!() {
    Fraction f(this->uiDenominator_, this->iNumerator_);
    return f;
}

//BINARY ARITHMETIC

inline Fraction operator*(const Fraction& a, const Fraction& b) {
    Fraction f(a.iNumerator_ * b.iNumerator_, a.uiDenominator_ * b.uiDenominator_);
    return f;
}

inline Fraction operator/(const Fraction& a, const Fraction& b) {
    Fraction f(a.iNumerator_ * b.uiDenominator_, a.uiDenominator_ * b.iNumerator_);
    return f;
}

inline Fraction operator%(const Fraction& a, const Fraction& b) {
    Fraction f = a / b;
    Fraction temp;
    temp.iNumerator_ = f.iNumerator_ % f.uiDenominator_;
    temp.uiDenominator_ = f.uiDenominator_;
    temp = temp*b;
    return temp;
}

inline bool Fraction::operator==(const Fraction& rhs) {
    if (this->iNumerator_ == rhs.iNumerator_ && this->uiDenominator_ == rhs.uiDenominator_) {
        return true;
    } else {
        return false;
    }
}

inline bool Fraction::operator!=(const Fraction& rhs) {
    if (this->iNumerator_ != rhs.iNumerator_ || this->uiDenominator_ != rhs.uiDenominator_) {
        return true;
    } else {
        return false;
    }
}

inline bool Fraction::operator<(const Fraction& rhs) {
    if (this->iNumerator_ * rhs.uiDenominator_ < rhs.iNumerator_ * rhs.uiDenominator_) {
        return true;
    } else {
        return false;
    }
}

inline bool Fraction::operator<=(const Fraction& rhs) {
    if (this->iNumerator_ * rhs.uiDenominator_ <= rhs.iNumerator_ * rhs.uiDenominator_) {
        return true;
    } else {
        return false;
    }
}

inline bool Fraction::operator>(const Fraction& rhs) {
    if (this->iNumerator_ * rhs.uiDenominator_ > rhs.iNumerator_ * rhs.uiDenominator_) {
        return true;
    } else {
        return false;
    }
}

inline bool Fraction::operator>=(const Fraction& rhs) {
    if (this->iNumerator_ * rhs.uiDenominator_ >= rhs.iNumerator_ * rhs.uiDenominator_) {
        return true;
    } else {
        return false;
    }
}

inline Fraction& Fraction::operator+=(const Fraction& rhs) {
    *this = *this +rhs;
    return *this;
}

inline Fraction& Fraction::operator-=(const Fraction& rhs) {
    *this = *this -rhs;
    return *this;
}

inline Fraction& Fraction::operator*=(const Fraction& rhs) {
    *this = *this*rhs;
    return *this;
}

inline Fraction& Fraction::operator/=(const Fraction& rhs) {
    *this = *this / rhs;
    return *this;
}

inline Fraction& Fraction::operator%=(const Fraction& rhs) {
    *this = *this % rhs;
    return *this;
}

inline ostream& operator<<(ostream& output,const Fraction& f){
    output << f.iNumerator_<<"/"<<f.uiDenominator_;
    return output;
}

inline istream& operator>>(istream& input,Fraction& f){
    char c;
    input >> f.iNumerator_;
    input.get(c);
    if (c==' '){
        f.uiDenominator_=1;
        return input;
    }
    input>>f.uiDenominator_;
    return input;
}

#endif	/* FRACTION_INL */

