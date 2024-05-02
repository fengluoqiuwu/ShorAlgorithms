from qiskit import QuantumCircuit
from sympy import mod_inverse

from QuantumGate import basicGate

"""
常数计算量子门
"""
def const_QFT_addition_circuit(N, c):
    """
    N位的基于QFT实现的常数加法器，0位为高位
    :param N: 被加数的位数
    :param c: 要向量子电路中加上的常数
    :return: N位的基于QFT实现的常数加法器
    """
    c = bin(c).replace('0b', '')
    c = c.zfill(N)
    circuit = QuantumCircuit(N)

    circuit.append(basicGate.QFT(N), range(N))

    for i in range(N):
        if int(c[i]) == 1:
            for j in range(i + 1):
                circuit.append(basicGate.R_Gate(i + 1 - j), [N - 1 - j])

    circuit.append(basicGate.QFT_dagger(N), range(N))

    circuit.name = 'const_QFT_addition+='+str(c)
    const_QFT_addition = circuit.to_gate()
    return const_QFT_addition
def const_QFT_subtraction_circuit(N, c):
    """
    N位的基于QFT实现的常数加减器，0位为高位
    :param N: 被减数的位数
    :param c: 要向量子电路中减去的常数
    :return: N位的基于QFT实现的常数减法器
    """
    c = bin(c).replace('0b', '')
    c = c.zfill(N)
    circuit = QuantumCircuit(N)

    circuit.append(basicGate.QFT(N), range(N))

    for i in range(N):
        if int(c[i]) == 1:
            for j in range(i + 1):
                circuit.append(basicGate.R_daggerGate(i + 1 - j), [N - 1 - j])

    circuit.append(basicGate.QFT_dagger(N), range(N))

    circuit.name = 'const_QFT_subtraction-='+str(c)
    const_QFT_subtraction = circuit.to_gate()
    return const_QFT_subtraction
def const_QFT_comparator_circuit(N,c):
    """
    N位的基于QFT实现的常数比较器，0位为高位
    0位表示被比较的位，1-N为被比较的数a
    若a>=c，则0位为0；若a<c则0位为1
    c严格小于2^N
    :param N: 比较器比较的两数的位数
    :return: N位的基于QFT实现的常数比较器
    """
    circuit=QuantumCircuit(N+1)

    circuit.append(const_QFT_subtraction_circuit(N+1,c),range(N+1))
    circuit.append(const_QFT_addition_circuit(N,c),[1+i for i in range(N)])

    circuit.name = 'const_QFT_comparator_?_'+str(c)
    const_QFT_comparator=circuit.to_gate()

    return const_QFT_comparator
def const_QFT_controlAddition_circuit(N,c):
    """
    N位的基于QFT实现的控制常数加法器，0位为高位
    :param N: 被加数的位数
    :param c: 要向量子电路中加上的常数
    :return: N位的基于QFT实现的控制常数加法器
    """
    circuit = QuantumCircuit(N)
    circuit.append(const_QFT_addition_circuit(N,c),range(N))
    circuit.name = 'const_QFT_addition+='+str(c)
    const_QFT_addition = circuit.to_gate().control(1)
    return const_QFT_addition
def const_QFT_controlSubtraction_circuit(N,c):
    """
    N位的基于QFT实现的控制常数减法器，0位为高位
    :param N: 被减数的位数
    :param c: 要向量子电路中减去的常数
    :return: N位的基于QFT实现的控制常数减法器
    """
    circuit = QuantumCircuit(N)
    circuit.append(const_QFT_subtraction_circuit(N,c),range(N))
    circuit.name = 'const_QFT_subtraction-='+str(c)
    const_QFT_subtraction = circuit.to_gate().control(1)
    return const_QFT_subtraction
def const_QFT_modularAddition_circuit(N,c,m):
    """
    N位的基于QFT实现的常数模加器，0位为高位
    0位为辅助比特，1-N位为操作数a
    :param N: 操作数a，常数c，模数m的最大位数
    :param c: 加上的常数c
    :param m: 模运算的模数m
    :return: N位的基于QFT实现的常数模加器
    """
    circuit = QuantumCircuit(N + 1)

    circuit.append(const_QFT_comparator_circuit(N, m - c), range(N + 1))
    circuit.append(const_QFT_controlAddition_circuit(N, c), [0] + [1 + i for i in range(N)])
    circuit.x(0)
    circuit.append(const_QFT_controlSubtraction_circuit(N, m - c), [0] + [1 + i for i in range(N)])
    circuit.x(0)
    circuit.append(const_QFT_comparator_circuit(N, c), range(N + 1))
    circuit.x(0)

    circuit.name = 'const_QFT_modularAddition+'+str(c)+' mod '+str(m)
    const_QFT_modularAddition = circuit.to_gate()
    return const_QFT_modularAddition
def const_QFT_modularSubtraction_circuit(N, c, m):
    """
    N位的基于QFT实现的常数模减器，0位为高位
    0位为辅助比特，1-N位为操作数a
    :param N: 操作数a，常数c，模数m的最大位数
    :param c: 减去的常数c
    :param m: 模运算的模数m
    :return: N位的基于QFT实现的常数模减器
    """
    circuit = QuantumCircuit(N + 1)

    circuit.append(const_QFT_comparator_circuit(N, c), range(N + 1))
    circuit.append(const_QFT_controlAddition_circuit(N, m - c), [0] + [1 + i for i in range(N)])
    circuit.x(0)
    circuit.append(const_QFT_controlSubtraction_circuit(N, c), [0] + [1 + i for i in range(N)])
    circuit.x(0)
    circuit.append(const_QFT_comparator_circuit(N, m-c), range(N + 1))
    circuit.x(0)

    circuit.name = 'const_QFT_modularSubtraction-'+str(c)+' mod '+str(m)
    const_QFT_modularSubtraction = circuit.to_gate()
    return const_QFT_modularSubtraction
def const_QFT_controlModularAddition_circuit(N,c,m):
    """
    N位的基于QFT实现的控制常数模加器，0位为高位
    0位为辅助比特，1-N位为操作数a
    :param N: 操作数a，常数c，模数m的最大位数
    :param c: 加上的常数c
    :param m: 模运算的模数m
    :return: N位的基于QFT实现的控制常数模加器
    """
    circuit = QuantumCircuit(N+1)
    circuit.append(const_QFT_modularAddition_circuit(N, c, m), range(N + 1))
    circuit.name = 'const_QFT_modularAddition+' + str(c) + ' mod ' + str(m)
    const_QFT_modularAddition = circuit.to_gate().control(1)
    return const_QFT_modularAddition
def const_QFT_controlModularSubtraction_circuit(N,c,m):
    """
    N位的基于QFT实现的控制常数模减器，0位为高位
    0位为辅助比特，1-N位为操作数a
    :param N: 操作数a，常数c，模数m的最大位数
    :param c: 减去的常数c
    :param m: 模运算的模数m
    :return: N位的基于QFT实现的控制常数模减器
    """
    circuit = QuantumCircuit(N + 1)
    circuit.append(const_QFT_modularSubtraction_circuit(N, c, m), range(N + 1))
    circuit.name = 'const_QFT_modularSubtraction-' + str(c) + ' mod ' + str(m)
    const_QFT_modularSubtraction = circuit.to_gate().control(1)
    return const_QFT_modularSubtraction
def const_QFT_modularAddMultiplication_circuit(N,c,m):
    """
    N位的基于QFT实现的常数模倍加器，0位为高位
    输入：0-N-1位，基数x；N位辅助比特；N+1-2N位，被加数b
    输出：0-N-1位，基数x；N位辅助比特；N+1-2N位，结果b+cx mod m
    :param N:被加数，x，b位数
    :param c: 乘上的常数c
    :param m: 模运算的模数m
    :return:N位的基于QFT实现的常数模倍加器
    """
    circuit = QuantumCircuit(2 * N + 1)

    for i in range(N):
        circuit.append(const_QFT_controlModularAddition_circuit(N,(c*(2**i))%m,m),[i]+[N+j for j in range(N+1)])

    circuit.name = "const_QFT_modularAddMultiplication"
    const_QFT_modularAddMultiplication =circuit.to_gate()

    return const_QFT_modularAddMultiplication
def const_QFT_modularSubMultiplication_circuit(N,c,m):
    """
    N位的基于QFT实现的常数模倍减器，0位为高位
    输入：0-N-1位，基数x；N位辅助比特；N+1-2N位，被减数b
    输出：0-N-1位，基数x；N位辅助比特；N+1-2N位，结果b-cx mod m
    :param N:被减数，x，b位数
    :param c: 乘上的常数c
    :param m: 模运算的模数m
    :return:N位的基于QFT实现的常数模倍减器
    """
    circuit = QuantumCircuit(2 * N + 1)

    for i in range(N):
        circuit.append(const_QFT_controlModularSubtraction_circuit(N,(c*(2**i))%m,m),[i]+[N+j for j in range(N+1)])

    circuit.name = "const_QFT_modularSubMultiplication"
    const_QFT_modularSubMultiplication =circuit.to_gate()

    return const_QFT_modularSubMultiplication
def const_QFT_modularMultiplication_circuit(N,c,m):
    """
    N位的基于QFT实现的常数模乘器，0位为高位
    输入：0-N-1位，基数x；N-2N位，辅助比特
    输出：0-N-1位，cx；N-2N位，辅助比特
    :param N:x,c位数
    :param c:乘上的常数c
    :param m:模运算的模数m
    :return:N位的基于QFT实现的常数模乘器
    """
    circuit = QuantumCircuit(2*N+1)

    circuit.append(const_QFT_modularAddMultiplication_circuit(N,c,m),range(2*N+1))

    for i in range(N):
        circuit.swap(i,N+i+1)

    circuit.append(const_QFT_modularSubMultiplication_circuit(N,mod_inverse(c,m), m), range(2 * N + 1))

    circuit.name = 'const_QFT_modularMultiplication'
    const_QFT_modularMultiplication = circuit.to_gate()

    return const_QFT_modularMultiplication
def const_QFT_controlModularMultiplication_circuit(N,c,m):
    """
    N位的基于QFT实现的控制常数模乘器，0位为高位
    输入：0-N-1位，基数x；N-2N位，辅助比特
    输出：0-N-1位，cx；N-2N位，辅助比特
    :param N:x,c位数
    :param c:乘上的常数c
    :param m:模运算的模数m
    :return:N位的基于QFT实现的控制常数模乘器
    """
    circuit = QuantumCircuit(2*N+1)

    circuit.append(const_QFT_modularMultiplication_circuit(N,c,m),range(2*N+1))

    circuit.name = 'const_QFT_modularMultiplication'
    const_QFT_modularMultiplication = circuit.to_gate().control(1)

    return const_QFT_modularMultiplication
def const_QFT_modularPower_circuit(N1,N2,c,m):
    """
    N1,N2位的基于QFT实现的常数模幂器，0位为高位
    输入：0-N1-1位，指数x；N1-(N1+N2-1)位，运算位输入x0；(N1+N2)-(N1+2*N2)位，辅助比特0
    输出：0-N1-1位，指数x；N1-(N1+N2-1)位，运算位输出x0*c^x；(N1+N2)-(N1+2*N2)位，辅助比特0
    :param N1:指数x的位数
    :param N2:基数c，模数m等的位数
    :param c:基数c
    :param m:模运算的模数m
    :return:N1,N2位的基于QFT实现的常数模幂器
    """
    circuit = QuantumCircuit(N1+2*N2+1)

    for i in range(N1):
        circuit.append(const_QFT_controlModularMultiplication_circuit(N2,(c**i)%m,m),[N1-1-i]+[N1+j for j in range(2*N2+1)])

    circuit.name = 'const_QFT_modularPower'
    const_QFT_modularPower = circuit.to_gate().control(1)

    return const_QFT_modularPower