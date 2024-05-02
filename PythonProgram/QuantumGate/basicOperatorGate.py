from qiskit import QuantumCircuit
from QuantumGate import basicGate

"""
基础的通用计算量子门
"""
def MAJ():
    """
    3位MAJ门
    输入：0,1,2分别对应c，b，a
    输出：0,1,2分别是a+c，a+b，c（i+1）
    :return:3位MAJ门
    """
    circuit = QuantumCircuit(3)

    circuit.cx(2, 1)
    circuit.cx(2, 0)
    circuit.append(basicGate.toffoli(2), [0, 1] + [2])
    circuit.name = 'MAJ'

    MAJ_gate = circuit.to_gate()
    return MAJ_gate
def UMA():
    """
    3位UMA门
    输入：0,1,2分别对应a+c，a+b，c（i+1）
    输出：0,1,2分别是c,a+b+c,a
    :return:3位UMA门
    """
    circuit = QuantumCircuit(3)

    circuit.append(basicGate.toffoli(2), [0, 1] + [2])
    circuit.cx(2, 0)
    circuit.cx(0, 1)

    circuit.name = 'UMA'

    UMA_gate = circuit.to_gate()
    return UMA_gate
def classicalAddition(N):
    """
    经典的量子加法器
    输入：从上到下，一个初始化为0的辅助bit，ba交替，一个进位bit
    输出：从上到下，辅助bit，sa交替，一个进位bit,其中s=a+b
    :param N: 加数与被加数的位数
    :return: 作为量子门的量子加法器
    """
    circuit = QuantumCircuit(2 * N + 2)

    for i in range(N):
        circuit.append(MAJ(), [2 * i + j for j in range(3)])

    circuit.cx(2 * N, 2 * N + 1)

    for i in range(N):
        circuit.append(UMA(), [2 * (4 - i) + j for j in range(3)])

    circuit.name = 'classical addition'

    addition_gate = circuit.to_gate()
    return addition_gate
def QFT_addition_circuit(N):
    """
    N位的基于QFT实现的加法器，0位为高位
    前N位为被加数，后N位为加数
    :param N: 加法器加数和被加数的位数
    :return: N位的基于QFT实现的加法器
    """
    circuit = QuantumCircuit(2 * N)

    circuit.append(basicGate.QFT(N), range(N))

    for i in range(N):
        for j in range(i + 1):
            circuit.append(basicGate.controlR_Gate(j + 1), [N + i] + [N - i + j - 1])

    circuit.append(basicGate.QFT_dagger(N), range(N))

    circuit.name = 'QFT_addition'
    QFT_addition = circuit.to_gate()

    return QFT_addition
def QFT_subtractionPro_circuit(N):
    """
    N位的基于QFT实现的减法器，0位为高位
    0位表示是否退位，1-N位为被减数，N+1-2N位为减数
    :param N: 减法器减数和被减数的位数
    :return: N位的基于QFT实现的减法器
    """
    circuit = QuantumCircuit(2 * N+1)

    circuit.append(basicGate.QFT(N + 1), range(N + 1))

    for i in range(N):
        for j in range(i + 2):
            circuit.append(basicGate.controlR_daggerGate(j + 1), [N + i + 1] + [N - 1 - i + j])

    circuit.append(basicGate.QFT_dagger(N + 1), range(N + 1))

    circuit.name = 'QFT_subtractionPro'
    QFT_subtractionPro = circuit.to_gate()

    return QFT_subtractionPro
def QFT_comparator_circuit(N):
    """
    N位的基于QFT实现的比较器，0位为高位
    0位表示被比较的位，1-N为a，N+1-2N为b
    若a>=b，则0位为0；若a<b则0位为1
    :param N: 比较器比较的两数的位数
    :return: N位的基于QFT实现的比较器
    """
    circuit = QuantumCircuit(2 * N + 1)
    circuit.append(QFT_subtractionPro_circuit(N), range(2 * N + 1))
    circuit.append(QFT_addition_circuit(N), [1 + i for i in range(2 * N)])
    circuit.name = 'QFT_comparator'
    QFT_comparator = circuit.to_gate()
    return QFT_comparator