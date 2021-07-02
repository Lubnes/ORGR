/*
 * 
 * 
 * 
	////// Desenvolvido por Gustavo Geyer e Luiza Nunes (ambos colorados) \\\\\\\\
 * 
 *  
 *  
*/
package orgarq;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
public class Main {

	public static void main(String[] args) throws FileNotFoundException{
		Scanner kb = new Scanner(System.in);
		System.out.println("1 - Montagem" + "\n 2 - Desmontagem");
		
		int menu = kb.nextInt();
		
		if(menu == 1) {
			Decod decod = new Decod();
			decod.consome_arquivo_asm();
			
			try {
				FileWriter writer = new FileWriter("C:\\Users\\T-Gamer\\Desktop\\Faculdade\\facul 3 sem\\Organização e Arquitetura de C. I\\Trab 3 Real\\ORGR\\montagem.txt");
				
				for(int i = 0; i<decod.mipsLines.size(); i++) {
					writer.write(decod.codifica(i));
					writer.write("\n");
				}
				writer.close();
				System.out.println("Arquivo 'montagem.txt' gerado.");
			}
			catch(Exception e) {
				System.out.println("Erro ao escrever arquivo.");
				System.out.println(e);
				e.printStackTrace();
			}
			finally {}
		}
		else {
			Cod cod = new Cod();
			cod.consome_arquivo_hex();
			Converte conv = new Converte();
			
			try {
				FileWriter writer = new FileWriter("C:\\Users\\T-Gamer\\Desktop\\Faculdade\\facul 3 sem\\Organização e Arquitetura de C. I\\Trab 3 Real\\ORGR\\desmontagem.asm");
				
				for(int i = 0; i<cod.hexCodes.size(); i++) {
					writer.write(cod.decodifica(conv.hex_to_bin(cod.hexCodes.get(i))));
					writer.write("\n");
				}
				writer.close();
				System.out.println("Arquivo 'desmontagem.asm' gerado.");
			}
			catch(Exception e) {
				System.out.println("Erro ao escrever arquivo.");
				System.out.println(e);
				e.printStackTrace();
			}
			finally {}
		}
		kb.close();	
	}
	

}
