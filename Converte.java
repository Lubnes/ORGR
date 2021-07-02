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

public class Converte { // Classe para conversoes bitwise
	
	public String hex_to_bin(String hex) {
		long i =  Long.parseLong(hex, 16);
		return String.format("%32s",Long.toBinaryString(i)).replace(" ", "0");	
	}
	public String dec_to_hex(long dec) {
		return Long.toHexString(dec);
	}
	public String bin_to_hex(String bin) {
		String hex = Long.toHexString(Long.parseLong(bin, 2));
	    return String.format("%" + (int)(Math.ceil(bin.length() / 4.0)) + "s", hex).replace(' ', '0');
	}
	public long bin_to_dec(String bin) {
		return Long.parseLong(bin, 2);
	}
	public String dec_to_bin(long dec, int bits) {
		String format = "%"+bits+"s";
		return String.format(format,Long.toBinaryString(dec)).replace(" ", "0");
	}
	public Long hex_to_dec(String hex) {
		return Long.parseLong(hex, 16);
	}
	public String fill_hex(String bin) { //preenche o codigo hex
		
		char[] fill = new char[16];
		char key = bin.charAt(0); // Pega o ultimo char do end
		
		for(int i=0; i<16; i++) {
			fill[i] = key;
		}
		
		String full32 = new String(fill);
		
		return full32;
	}
}
