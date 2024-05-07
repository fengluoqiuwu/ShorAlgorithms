from fractions import Fraction
import random

from qiskit import QuantumCircuit, transpile
import math
from qiskit_aer import QasmSimulator
from QuantumGate import basicGate, constOperatorGate

class ShorForFactoringProblem:
    """
    计算整数分解问题的Shor算法

    Attributes:
        n(int):待分解的整数
        a(int):随机的底数
        N1(int):第一寄存器的位数
        N2(int):第二寄存器的位数
        circuit(QuantumCircuit):量子电路
    """
    n : int=0
    a : int=0
    N1 : int=0
    N2 : int=0
    simulator=None
    circuit=None
    def __init__(self, n : int,a : int):
        """
        初始化函数，包括：
        初始化参数，选择模拟器，初始化电路，保存图片，编译电路
        :param n: 待分解的整数
        :param a: 随机的底数
        """

        # 初始化参数
        self.n=n
        self.a=a
        self.N1 = math.ceil(2 * math.log2(n))
        self.N2 = math.ceil(math.log2(n))

        # 选择模拟器
        self.simulator=QasmSimulator()

        # 初始化电路
        circuit = QuantumCircuit(self.N1 + 2 * self.N2 + 1,self.N1)

        circuit.x(self.N1+self.N2-1)

        circuit.append(basicGate.QFT(self.N1), range(self.N1))
        circuit.append(constOperatorGate.const_QFT_modularPower_circuit(self.N1,self.N2,a,n),range(self.N1 + 2 * self.N2 + 1))
        circuit.append(basicGate.QFT_dagger(self.N1), range(self.N1))

        circuit.measure(range(self.N1),range(self.N1-1,-1,-1))

        # 保存图片
        fig=circuit.draw('mpl')
        fig.savefig('resources/ShorForFactoringProblem_circuit.png')

        # 编译电路
        self.circuit = transpile(circuit,self.simulator)
    def run(self):
        """
        运行一次电路，获得结果
        :return: x/q，即量子电路的结果x/(2^n)，其中n为第一寄存器的位数
        """
        job = self.simulator.run(self.circuit, shots=1,memory=True)
        result = job.result()

        readings=result.get_memory()
        phase = int(readings[0], 2) / (2 ** self.N1)
        return phase
    def find_r(self):
        """
        寻找可能的a mod n的阶r
        :return: 可能的r
        """
        attempt=0
        while True:
            attempt += 1
            phase = self.run()# Phase = s/r
            frac = Fraction(phase).limit_denominator(self.n)  # Denominator should (hopefully!) tell us r
            r : int = frac.denominator

            if phase != 0 and pow(self.a,r,self.n)==1:
                while r%2==0 and pow(self.a,r//2,self.n)==1:
                    r=r//2
                if r%2==1 or pow(self.a,r//2,self.n)==self.n-1:
                    return -1
                else:
                    return r
def factoring(n : int):
    while True:
        a = random.randint(2, n-2)

        if math.gcd(a, n)!=1:
            continue

        shor = ShorForFactoringProblem(n,a)
        r = shor.find_r()
        if r!=-1:
            return math.gcd(a ** (r // 2) - 1, n), math.gcd(a ** (r // 2) + 1, n)

if __name__ == "__main__":
    input_n = int(input("请输入待分解的整数："))
    p , q = factoring(input_n)
    print(str(p))
    print(str(q))
