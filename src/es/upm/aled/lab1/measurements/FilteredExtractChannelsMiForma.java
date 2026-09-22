package es.upm.aled.lab1.measurements;

/**
 * Filter that extracts the specified channels from an EEGModel.
 * 
 * @author mmiguel, rgarciacarmona
 *
 */
public class FilteredExtractChannelsMiForma implements Filter {   

	/**
	 * Builds the Filter. The use from an array of valid channels.
	 * 
	 * @param validChannels The channel numbers to be extracted, starting from 0.
	 */
	
	//Ej. tengo un muestra con 10 canales, pero yo quiero quedarme solo con los canales 3,6 y 8, el filtro generara un nuevo EEG con solo 3 canales (3 -> 0, 6 -> 1, 8 -> 2)
	
	
	
	private int[] validChannels;
	
	public FilteredExtractChannelsMiForma(int[] validChannels) {       //constructor
		
		this.validChannels = validChannels;
	}

	@Override
	public EEGModel applyFilter(EEGModel eeg) {
		// TODO
		
		Measurement[] muestras = eeg.getMeasurements();
		Measurement[] muestrasFiltradas = new Measurement[muestras.length];   //array donde voy a meter las medidas filtradas (aquellas que tienen los canales validos)
		
		for(int i  = 0; i < muestras.length; i++) {
			float[] canalesFiltrados = new float[validChannels.length];    //array donde guardo los valores de los canales filtrados
			
			Measurement muestra = muestras[i];                           //i=0, medida1, su array [x,x,x,x] x: valor del canal (j=0, 1,2,3)
			for(int j = 0; j < validChannels.length; j++) { 
				int canalValido = validChannels[j];   //j=0 -> validChannels[0] = 1, el canal 1 es un canal valido
				canalesFiltrados[j] = muestra.getChannel(canalValido);
				
			}
			
			muestrasFiltradas[i] = new Measurement(canalesFiltrados); 
		}
		EEGModel eegFiltrado = new EEGModel(muestrasFiltradas);
		
		return eegFiltrado;
	}

}
