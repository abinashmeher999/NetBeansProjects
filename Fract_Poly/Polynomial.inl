/* 
 * File:   Polynomial.inl
 * Author: abinashmeher999
 *
 * Created on 10 February, 2015, 2:15 AM
 */

#ifndef POLYNOMIAL_INL
#define	POLYNOMIAL_INL

#include <Polynomial.hxx>

template<class T, class U>
inline Poly<T, U>::Poly(unsigned int a) : degree_(a), coefficients_(a + 1) {
}

template <class T,class U>
inline void Poly<T,U>::trimzero(){
    typename std::vector<U>::reverse_iterator rit ;
    rit = this->coefficients_.rbegin();
    while(*rit==0){
        if (this->coefficients_.size()==1){
            this->coefficients_.at(0)=0;
            break;
        }
        this->coefficients_.pop_back();
        this->degree_--;
        ++rit;
    }
}

template<class T, class U>
inline Poly<T, U>::Poly(const Poly& pol) {
    this->degree_ = pol.degree_;
    this->coefficients_ = pol.coefficients_;
    this->trimzero();
}

template<class T, class U>
inline Poly<T, U>& Poly<T, U>::operator=(const Poly& pol) {
    this->degree_ = pol.degree_;
    this->coefficients_ = pol.coefficients_;
    return *this;
}

template<class T, class U>
inline Poly<T, U> Poly<T, U>::operator-() {
    Poly<T, U> pol(*this);
    typename vector<U>::iterator it;
    for (it = pol.coefficients_.begin(); it != pol.coefficients_.end(); it++) {
        *it = -*it;
    }
    return pol;
}

template<class T, class U>
inline Poly<T, U> Poly<T, U>::operator+() {
    return *this;
}

template<class T, class U>
inline Poly<T, U> Poly<T, U>::operator+(const Poly& pol) {
    Poly<T, U> tpol;
    Poly<T,U> spol = pol;
    tpol.coefficients_.pop_back();
    typename std::vector<U>::iterator itpol, itthis;
    itpol = spol.coefficients_.begin();
    itthis = this->coefficients_.begin();
    while (itpol != spol.coefficients_.end() && itthis != this->coefficients_.end()) {
        tpol.coefficients_.push_back(*itthis + *itpol);
        ++tpol.degree_;
        ++itpol;
        ++itthis;
    }
    while (itthis == this->coefficients_.end() && itpol != spol.coefficients_.end()) {
        tpol.coefficients_.push_back(*itpol);
        ++tpol.degree_;
        ++itpol;
    }
    while (itpol == spol.coefficients_.end() && itthis != this->coefficients_.end()) {
        tpol.coefficients_.push_back(*itthis);
        ++tpol.degree_;
        ++itthis;
    }
    tpol.degree_--;
    tpol.trimzero();
    return tpol;
}

template<class T, class U>
inline Poly<T, U> Poly<T, U>::operator-(const Poly& pol) {
    Poly<T, U> tpol;
    tpol.coefficients_.pop_back();
    Poly<T,U> spol = pol;
    typename std::vector<U>::iterator itpol, itthis;
    itpol = spol.coefficients_.begin();
    itthis = this->coefficients_.begin();
    while (itpol != spol.coefficients_.end() && itthis != this->coefficients_.end()) {
        tpol.coefficients_.push_back(*itthis - *itpol);
        ++tpol.degree_;
        ++itpol;
        ++itthis;
    }
    while (itthis == this->coefficients_.end() && itpol != spol.coefficients_.end()) {
        tpol.coefficients_.push_back(-*itpol);
        ++tpol.degree_;
        ++itpol;
    }
    while (itpol == spol.coefficients_.end() && itthis != this->coefficients_.end()) {
        tpol.coefficients_.push_back(*itthis);
        ++tpol.degree_;
        ++itthis;
    }
    tpol.degree_--;
    tpol.trimzero();
    return tpol;
}

template<class T, class U>
inline Poly<T, U>& Poly<T, U>::operator+=(const Poly& pol) {
    typename std::vector<U>::iterator itpol, itthis;
    Poly<T,U> spol = pol;
    itpol = spol.coefficients_.begin();
    itthis = this->coefficients_.begin();
    while (itpol != spol.coefficients_.end() && itthis != this->coefficients_.end()) {
        *itthis = *itthis + *itpol;
        ++itpol;
        ++itthis;
    }
    while (itthis == this->coefficients_.end() && itpol != spol.coefficients_.end()) {
        this->coefficients_.push_back(*itpol);
        ++this->degree_;
        ++itpol;
    }
    while (itpol == spol.coefficients_.end() && itthis != this->coefficients_.end()) {
        break;
    }
    this->trimzero();
    return *this;
}

template<class T, class U>
inline Poly<T, U>& Poly<T, U>::operator-=(const Poly& pol) {
    Poly<T,U> spol = pol;
    typename std::vector<U>::iterator itpol = spol.coefficients_.begin();
    typename std::vector<U>::iterator itthis = this->coefficients_.begin();
    while (itpol != spol.coefficients_.end() && itthis != this->coefficients_.end()) {
        *itthis = *itthis - *itpol;
        ++itpol;
        ++itthis;
    }
    while (itthis == this->coefficients_.end() && itpol != spol.coefficients_.end()) {
        this->coefficients_.push_back(-*itpol);
        ++this->degree_;
        ++itpol;
    }
    while (itpol == spol.coefficients_.end() && itthis != this->coefficients_.end()) {
        break;
    }
    this->trimzero();
    return *this;
}

template<class T, class U>
inline ostream& operator<<(ostream& os, const Poly<T,U>& p) {
    typename std::vector<U>::reverse_iterator it;
    Poly<T,U> sp =p;
    it = sp.coefficients_.rbegin();
    unsigned int counter = sp.degree_;
    while(it!=sp.coefficients_.rend()){
        os<<"+("<<*it<<")x^"<<counter<<" ";
        counter--;
        it++;
    }
    return os;
}

template<class T, class U>
//inline istream& operator>>(istream& os, Poly<T, U>& p) {
//    p.degree_ = 0;
//    p.coefficients_.clear();
//    U temp;
//    
//    while (os.peek() != '\n' && os >> temp) {
//        cout << "read "<<temp<<"\n";
//        cout.flush();
//        p.degree_++;
//        p.coefficients_.insert(p.coefficients_.begin(),temp);
//    }
//    //char c;
//    //os.get(c);
//    p.degree_--;
//    os.get();
//    os.get();
//    //os.clear;
//    //os.setstate(os.eofbit);
//    //os.clear();
//    return os;
//}
inline istream& operator>>(istream& is, Poly<T, U>& p){
    Poly<T, U> p_new(0);
    U temp;
    p_new.coefficients_.clear();
    while(is.peek() == '\n'){
        is.get();
    }
    while (is.peek() != '\n' && is >> temp) {
        p_new.degree_++;
        p_new.coefficients_.insert(p_new.coefficients_.begin(),temp);
    }
    p_new.degree_--;
    p_new.trimzero();
    p = p_new;
    return is;
}


template<class T, class U>
T Poly<T, U>::Evaluate(const T& x) {
    T result = *(this->coefficients_.rbegin());
    typename std::vector<U>::reverse_iterator it;
    it = this->coefficients_.rbegin();
    ++it;
    while (it != this->coefficients_.rend()) {
        result = (result) * x + *it;
        ++it;
    }
    return result;
}

#endif	/* POLYNOMIAL_INL */

