package es.upm.aled.lab1.measurements;

/**
 * Filter that extracts the specified channels from an EEGModel.
 * 
 * @author mmiguel, rgarciacarmona
 *
 */
public class FilterExtractChannels implements Filter {   

	/**
	 * Builds the Filter. The use from an array of valid channels.
	 * 
	 * @param validChannels The channel numbers to be extracted, starting from 0.
	 */
	
	//Ej. tengo un muestra con 10 canales, pero yo quiero quedarme solo con los canales 3,6 y 8, el filtro generara un nuevo EEG con solo 3 canales (3 -> 0, 6 -> 1, 8 -> 2)
	
	
	
	private int[] validChannels;
	
	public FilterExtractChannels(int[] validChannels) {       //constructor
		
		this.validChannels = validChannels;
	}

	@Override
	public EEGModel applyFilter(EEGModel eeg) {
		// TODO
		
		Measurement[] medidas = eeg.getMeasurements();
		Measurement[] medidasFiltradas = new Measurement[medidas.length];   //array donde voy a meter las medidas filtradas (aquellas que tienen los canales validos)
		
		for(int i  = 0; i < medidas.length; i++) {
			float[] canalesFiltrados = new float[validChannels.length];    //array donde guardo los valores de los canales filtrados
			
			Measurement medida = medidas[i];   //i=0, medida1, su array [x,x,x,x] x: valor del canal (j=0, 1,2,3)
			int indice = 0;
			for(int j = 0; j < medida.numChannels(); j++) { 
				if(j == validChannels[indice]) {
					canalesFiltrados[indice] = medida.getChannel(j);
					indice++;
				}
				
			}
			
			medidasFiltradas[i] = new Measurement(canalesFiltrados); 
		}
		EEGModel eegFiltrado = new EEGModel(medidasFiltradas);
		
		return eegFiltrado;
	}

}
