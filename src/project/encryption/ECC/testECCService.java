package project.encryption.ECC;


public class testECCService {
    public static void main(String[] args) {
        String test= """
                kjafkjkakfehbkjahjDJKAKKJABKEFBKkjjkfsgbfkkshfhkewhealfdaljkbfkjaaellkandsjhdldhaelndajefjal
                kjafkjkakfehbkjahjDJKAKKJABKEFBKkjjkfsgbfkkshfhkewhealfdaljkbfkjaaellkandsjhdldhaelndajefjal
                kjafkjkakfehbkjahjDJKAKKJABKEFBKkjjkfsgbfkkshfhkewhealfdaljkbfkjaaellkandsjhdldhaelndajefjal
                kjafkjkakfehbkjahjDJKAKKJABKEFBKkjjkfsgbfkkshfhkewhealfdaljkbfkjaaellkandsjhdldhaelndajefjal
                kjafkjkakfehbkjahjDJKAKKJABKEFBKkjjkfsgbfkkshfhkewhealfdaljkbfkjaaellkandsjhdldhaelndajefjal
                kjafkjkakfehbkjahjDJKAKKJABKEFBKkjjkfsgbfkkshfhkewhealfdaljkbfkjaaellkandsjhdldhaelndajefjal
                kjafkjkakfehbkjahjDJKAKKJABKEFBKkjjkfsgbfkkshfhkewhealfdaljkbfkjaaellkandsjhdldhaelndajefjal
                kjafkjkakfehbkjahjDJKAKKJABKEFBKkjjkfsgbfkkshfhkewhealfdaljkbfkjaaellkandsjhdldhaelndajefjal
                kjafkjkakfehbkjahjDJKAKKJABKEFBKkjjkfsgbfkkshfhkewhealfdaljkbfkjaaellkandsjhdldhaelndajefjal
                kjafkjkakfehbkjahjDJKAKKJABKEFBKkjjkfsgbfkkshfhkewhealfdaljkbfkjaaellkandsjhdldhaelndajefjal
                kjafkjkakfehbkjahjDJKAKKJABKEFBKkjjkfsgbfkkshfhkewhealfdaljkbfkjaaellkandsjhdldhaelndajefjal
                kjafkjkakfehbkjahjDJKAKKJABKEFBKkjjkfsgbfkkshfhkewhealfdaljkbfkjaaellkandsjhdldhaelndajefjal
                kjafkjkakfehbkjahjDJKAKKJABKEFBKkjjkfsgbfkkshfhkewhealfdaljkbfkjaaellkandsjhdldhaelndajefjal
                """;

        try{
            ECCService ecc = new ECCService();
            ecc.generateKeyPair();
            System.out.println(ecc.getPublicKey());
            System.out.println(ecc.getPrivateKey());

            System.out.println(test);

            String encrypted = ecc.encrypt(test);
            System.out.println(encrypted);

            String decrypted = ecc.decrypt(encrypted);
            System.out.println(decrypted);
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
