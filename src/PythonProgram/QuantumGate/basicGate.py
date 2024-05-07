from qiskit import QuantumCircuit
from numpy import pi

"""
基础操作量子门
"""
def toffoli(N):
    """
    N位Tof门
    :param N: 量子电路的控制电路位数
    :return: N位控制位的量子Tof门
    """
    circuit = QuantumCircuit(1)
    circuit.x(0)

    circuit.name = 'Toffoli'
    Toffoli = circuit.to_gate().control(N)

    return Toffoli
def R_Gate(N):
    """
    e^((2i*pi)/(2^N))的旋转门
    :return: 旋转R门
    """
    circuit = QuantumCircuit(1)
    circuit.p(2 * pi / float(2 ** N), 0)

    circuit.name = 'R_' + str(N)
    R_Gate = circuit.to_gate()

    return R_Gate
def controlR_Gate(N):
    """
    e^((2i*pi)/(2^N))的控制旋转门
    :return: 控制旋转R门
    """

    return R_Gate(N).control(1)
def R_daggerGate(N):
    """
    e^(-(2i*pi)/(2^N))的旋转门
    :return: 旋转R门的逆门
    """
    circuit = QuantumCircuit(1)
    circuit.p(-2 * pi / float(2 ** N), 0)

    circuit.name = 'R_' + str(N)+'†'
    R_Gate = circuit.to_gate()

    return R_Gate
def controlR_daggerGate(N):
    """
    e^(-(2i*pi)/(2^N))的控制旋转门
    :return: 控制旋转R门的逆门
    """

    return R_daggerGate(N).control(1)
def QFT(N):
    """
    N位量子傅里叶变换QFT的门
    :param N:量子门的位数
    :return:N位量子傅里叶变换QFT的门
    """
    circuit = QuantumCircuit(N)
    # Don't forget the Swaps!
    for j in range(N):
        circuit.h(j)
        for i in range(N - j - 1):
            circuit.append(controlR_Gate(i + 2), [i + j + 1] + [j])

    for qubit in range(N // 2):
        circuit.swap(N - 1 - qubit, qubit)

    circuit.name = "QFT"
    QFT = circuit.to_gate()

    return QFT
def QFT_dagger(N):
    """
    N位逆量子傅里叶变换QFT†的门
    :param N:量子门的位数
    :return:N位逆量子傅里叶变换QFT†的门
    """
    circuit = QuantumCircuit(N)
    # Don't forget the Swaps!
    for qubit in range(N // 2):
        circuit.swap(N - 1 - qubit, qubit)

    for j in range(N - 1, -1, -1):
        for i in range(N - j - 2, -1, -1):
            circuit.append(controlR_daggerGate(i + 2), [i + j + 1] + [j])
        circuit.h(j)

    circuit.name = "QFT†"
    QFT_dagger = circuit.to_gate()
    return QFT_dagger