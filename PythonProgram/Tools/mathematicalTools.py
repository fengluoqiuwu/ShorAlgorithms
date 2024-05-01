from math import gcd as gcd1
from sympy import mod_inverse

def gcd(a, b):
    """
    最大公因数
    """
    return gcd1(a, b)
def inverse(a, m):
    """
    求a模m的逆
    """
    return mod_inverse(a, m)
def modPow(base, exponent, modulus):
    return pow(base, exponent, modulus)