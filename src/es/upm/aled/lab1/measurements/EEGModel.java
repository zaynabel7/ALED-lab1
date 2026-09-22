package es.upm.aled.lab1.measurements;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import es.upm.aled.lab1.gui.EEG_GUI;

/**
 * Model of the data retrieved by an OpenBCI EEG source. Each model is a List of
 * Measurements. All Measurements of the same EEGModel must contain the same
 * number of channels. This class provides methods to plot the data using the
 * classes in the es.upm.aled.lab1.gui package.
 * 
 * @author mmiguel, rgarciacarmona
 *
 */
public class EEGModel {

	protected List<Measurement> measurements = new ArrayList<Measurement>();  //esta lista esta formada por objetos tipo Measurement, en cada posicion de la lista hay un measurement cuyo atributo es un array de floats
	protected EEG_GUI gui;
	/**
	 * Builds an empty EEGModel.
	 */
	public EEGModel() {
	}

	/**
	 * Builds a new EEGModel from a file.
	 * 
	 * @param file Source file containing a measurement session in the OpenBCI
	 *             format.
	 */
	public EEGModel(String file) {    //cuando se hace el loadFile se añaden las medidas 
		try {
			loadFile(file);
		} catch (IOException e) {
			System.out.println("Error reading from file. Is the format correct?");
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Builds an EEGModel from an array of Measurements.
	 * 
	 * @param measurements The Measurements that make up the EEGModel.
	 */
	public EEGModel(Measurement[] measurements) {  //constructor de EEGModel recibe array con objetos tipo Measurements como parametro
		// TODO
		this.measurements = Arrays.asList(measurements);  //para obtener una lista a partir de un array.
	}

	/**
	 * Adds a Measurement to the end of the EEGModel. If the GUI is running, the new
	 * Measurement is also plotted.
	 * 
	 * @param measurement The Measurement to be added.
	 */
	public void addMeasurement(Measurement measurement) {
		measurements.add(measurement);
		if (gui != null)
			gui.plotMeasurement(measurement);
	}

	/**
	 * Returns an array of the EEGModel measurements.
	 * 
	 * @return Array of Measurements.
	 */
	public Measurement[] getMeasurements() {
		Measurement[] am = new Measurement[measurements.size()];
		return measurements.toArray(am);
	}

	/**
	 * Applies a Filter over the EEGModel, returning a new filtered EEGModel.
	 * 
	 * @param filter Filter to be applied over the EEGModel.
	 * @return The new EEGModel.
	 */
	public EEGModel filter(Filter filter) {
		// TODO
		
		return filter.applyFilter(this);       // filter de EEGModel que permite aplicar un filtro sobre sí mismo.
	}

	/**
	 * Fills the measurements from the contents of an OpenBCI file, a CSV file in
	 * which each line represents a measurement. The first column is an index modulo
	 * 256, and the remaining columns are the values of the samples obtained by
	 * each of the channels. All lines must have the same number of columns. "%" at
	 * the beginning of a line indicates a comment.
	 * 
	 * @param fileName Path to the OpenBCI file.
	 * @throws IOException Thrown if the file can't be read.
	 */
	public void loadFile(String fileName) throws IOException { //no devuelve nada y puede lanzar una excepcion por error de lectura
		
		File f = new File(fileName); //se crea el objeto file, que representa el archivo (no lo estamos leyendo)
		
		FileInputStream fis = new FileInputStream(f);  //Aquí sí estamos abriendo el archivo para poder leer sus datos, FileInputStream es como un canal de entrada de datos:
		DataInput fid = new DataInputStream(fis);      //Aquí se crea otro objeto que nos permite leer los datos del archivo.
		String line;                                   //guardar una línea del archivo cada vez.
		while ((line = fid.readLine()) != null) {      //mientras que la linea que se lea no sea nula, el while va a ser true, cuando readLine llega al final del archivo devuelve null y se sale del bucle
			// Removes the comments
			if (line.startsWith("%"))                   //comprueba si empieza por %
				continue;                               //ontinue significa deja de ejecutar esta vuelta del while y pasa directamente a la siguiente línea.
			
			
			// Separates by commas and extracts the channels from each measurement
			
			String[] columns = line.split(",");      //split(",") divide el texto cada vez que encuentra una coma. Ejemplo: line = "1,2.5,3.7,4.1", columns[0] = "1", columns[1] = "2.5, columns[2] = "3.7", columns[3] = "4.1"
			float[] channels = new float[columns.length - 1];       //i=0 va el numero de la muestra, por tanto si tengo 10 columnas, 9 son canales
			for (int i = 1; i < columns.length; i++)                //ignoro la posicion i=0 de columns 
				channels[i - 1] = Float.parseFloat(columns[i]);     //dentro de  columns[i] hay un string pero yo necesito que en channels[i-1] haya un float, lo que hace Float.parseFloat es convertir un string en un float p.ej Float.parseFloar("2.5") -> 2.5
			
			//Despues de extraer el valor dentro de cada canal, entonces creo una nueva medida (objeto measurement) y le paso el array de canales
			Measurement m = new Measurement(channels);
			addMeasurement(m);              //Measurement(float[] channels), añade el objeto measurement  a la lista measurement, measurement tiene como parametro un array de canales (cada canal tiene su valor) 
		}
		fis.close();
	}

	/**
	 * Stores the EEGModel in a text file, following the OpenBCI format.
	 * 
	 * @param fileName Path to the OpenBCI file to be created.
	 * @throws IOException Thrown if the file can't be written.
	 */
	public void saveFile(String fileName) throws IOException {
		// TODO
	
		File f = new File(fileName);
		FileOutputStream fos = new FileOutputStream(f);
		PrintStream ps = new PrintStream(fos);
		
		int index = 0;
		for(Measurement m : measurements) {    //recorro la lista (tipo nombre : lista), para cada measurement m de la lista measurements
			String line = index + ", ";
			for(int i = 0; i < m.numChannels(); i++) {  //cada measurement tiene su array de floats y necesito imprimir cada valor con el formato   x, xxx, xxx, xxx
				
				float valor = m.getChannel(i);          //valor del canal i
				line = line + valor;        // line = index + ", " + valor
				
				if(i < m.numChannels()-1) {  //para no poner coma en la ultima posicion, solo entre numeros
					line = line + ", ";
				}
			}
			ps.println(line);
			index++; 
		}
		
		ps.close();
		}
	 //Ejemplo [ (4,2,3,5), (7,6,8,1), (6, 4, 4, 1), ... ] LISTA
	//1º for: estoy en measurement 1, index = 0; line = 0   (el index va a representar el numero de muestra
	//2º for: i = 0, valor = 4, line = 0, 4 -> i = 1, valor = 2, line = 0, 4, 2 -> i = 2, valor = 3, line = 0, 4, 2, 3,  -> i = 3, valor = 5, line = 0, 4, 2, 3, 5 
	//Cuando i = 3, se sale del 2º bucle for y se imprime la line y se incrementa el indice y asi hasta obtener algo asi (EN CADA VUELTA SE AÑADEN LOS VALORES DE LOS CHANNELS):
	//   0, 4, 2, 3, 5
	//   1, 7, 6, 8, 1
	//   2, 6, 4, 4, 1
	//  ...
	//se vuelve a entrar en el 1º bucle para pasar a measurement 2, ahora line = index + ", " con index = 1, y se vuelve a hacer el proceso de antes para la measurement 2
	
	

	/**
	 * Plots the data of the EEGModel using the classes in the es.upm.aled.lab1.gui
	 * package. The max and min values of each channel area calculated so the window
	 * is properly scaled. Assumes a sampling frequency of 250 Hz.
	 */
	public void plotData() {
		if (measurements.size() == 0)
			return;
		int numChannels = measurements.get(0).numChannels();
		float[] minY = new float[numChannels];
		float[] maxY = new float[numChannels];
		for (int i = 0; i < numChannels; i++) {
			// Sets the initial max and min values for each channel from the first
			// measurement
			minY[i] = measurements.get(0).getChannel(i);
			maxY[i] = measurements.get(0).getChannel(i);
			// Iterates over all measurements
			for (Measurement m : measurements) {
				float y = m.getChannel(i);
				// If the value is higher than max, set as new max
				if (y > maxY[i])
					maxY[i] = y;
				// If the value is lower than min, set as new min
				if (y < minY[i])
					minY[i] = y;
			}
		}
		// Plots the data assuming a sampling frequency of 250 Hz
		initGUI(minY, maxY, numChannels, 4);
		for (Measurement m : measurements)
			gui.plotMeasurement(m);
	}

	/**
	 * Initializes the GUI for plotting the measurements. Supports an arbitrary
	 * number of channels, but each max an min value for each channel must be
	 * specified, to properly scale the GUI.
	 * 
	 * @param minY      Array of min values for each channel.
	 * @param maxY      Array of max values for each channel.
	 * @param nChannels Number of channels. Length of minY and maxY must mach this
	 *                  value.
	 * @param sRate     Rate at which the samples are plotted (in ms).
	 */
	protected void initGUI(float[] minY, float[] maxY, int nChannels, int sRate) {
		gui = new EEG_GUI(minY, maxY, nChannels, 250.0F, sRate);
	}

	private Random random = new Random();
	private final float sine_freq_Hz = 10.0f;

	/**
	 * Creates a random synthetic collection of measurements, for testing purposes.
	 * Assumes 4 channels and a sampling frequency of 250 Hz.
	 * 
	 * @param numMeasurements Amount of Measurements to be created.
	 */
	public void createSyntheticData(int numMeasurements) {
		// Min and max values of the synthetic data
		float[] min = { -200.0f, -200.0f, -200.0f, -200.0f };
		float[] max = { 200.0f, 200.0f, 200.0f, 200.0f };
		// Starts the GUI, so every time a new Measurement is created, it can be plotted
		initGUI(min, max, 4, 0);
		// Adds the Measurements and plots them
		for (int i = 0; i < numMeasurements; i++) {
			long startTime = System.currentTimeMillis();
			Measurement m = createSyntheticMeasurement(4, 250, 1.0f);
			addMeasurement(m);
			try {
				long diff = 1000 / 250 - (System.currentTimeMillis() - startTime);
				Thread.sleep(diff > 0 ? diff : 0);
			} catch (Exception e) {
			}
		}
	}

	private Measurement createSyntheticMeasurement(int nchan, float fs_Hz, float scale_fac_uVolts_per_count) {
		double val_uV;
		double[] sine_phase_rad = new double[nchan];
		float[] curDataPacket_values = new float[nchan];
		for (int ichan = 0; ichan < nchan; ichan++) {
			// Ensures that it has amplitude of one unit per sqrt(Hz) of signal bandwidth
			val_uV = random.nextGaussian() * Math.sqrt(fs_Hz / 2.0f);
			// Scale one channel higher
			if (ichan == 0)
				val_uV *= 10;
			if (ichan == 1) {
				// Add sine wave at 10 Hz at 10 uVrms
				sine_phase_rad[ichan] += 2.0f * Math.PI * sine_freq_Hz / fs_Hz;
				if (sine_phase_rad[ichan] > 2.0f * Math.PI)
					sine_phase_rad[ichan] -= 2.0f * Math.PI;
				val_uV += 10.0f * Math.sqrt(2.0) * Math.sin(sine_phase_rad[ichan]);
			} else if (ichan == 2) {
				// 50 Hz interference at 50 uVrms
				sine_phase_rad[ichan] += 2.0f * Math.PI * 50.0f / fs_Hz; // 60 Hz
				if (sine_phase_rad[ichan] > 2.0f * Math.PI)
					sine_phase_rad[ichan] -= 2.0f * Math.PI;
				val_uV += 50.0f * Math.sqrt(2.0) * Math.sin(sine_phase_rad[ichan]); // 20 uVrms
			} else if (ichan == 3) {
				// 60 Hz interference at 50 uVrms
				sine_phase_rad[ichan] += 2.0f * Math.PI * 60.0f / fs_Hz; // 50 Hz
				if (sine_phase_rad[ichan] > 2.0f * Math.PI)
					sine_phase_rad[ichan] -= 2.0f * Math.PI;
				val_uV += 50.0f * Math.sqrt(2.0) * Math.sin(sine_phase_rad[ichan]); // 20 uVrms
			}
			// Convert to counts, the 0.5 is to ensure rounding
			curDataPacket_values[ichan] = (int) (0.5f + val_uV / scale_fac_uVolts_per_count);
		}
		return new Measurement(curDataPacket_values);
	}

	public static void main(String[] args) {
		if (args.length > 0) {
			
			EEGModel eeg = new EEGModel(args[0]);  //Recupera las muestras de una sesión de EEG almacenada en un archivo.
			eeg.plotData();
			int[] validChannels = {8,9,10}; //11 canales, escogemos los 3 ultimos (8,9,10)
			Filter filtroCanal = new FilterExtractChannels(validChannels);
			int min = 2750;
			int max = 5750;
			Filter filtroIntervalo =  new FilterExtractPeriod(min,max);
			EEGModel eegFiltrado = eeg.filter(filtroIntervalo).filter(filtroCanal);
			eegFiltrado.plotData();
			try {
				eegFiltrado.saveFile("Filtrado.txt");
			}catch(IOException e) {
				System.out.println("Error");
				e.printStackTrace();}
				
		} else {
			
			EEGModel eeg = new EEGModel();      //Crea una nueva sesión de EEG sintética con muestras aleatorias.
			eeg.createSyntheticData(1000);
		
			try {
				eeg.saveFile("Synthetic.txt");
				System.out.println("Se ha guardado el archivo");
				System.out.println(Path.of("Synthetic.txt").toAbsolutePath());
			} catch (IOException e) {
				System.out.println("Error");
				e.printStackTrace();
			}
		}
		
//		float[] miArray = {1f, 2.4f,4f,6f};
//		Measurement m = new Measurement(miArray);
		
	
}}
