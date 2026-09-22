package es.upm.aled.lab1.measurements;

public class FilterExtractChannelsVideo implements Filter {


	private int[] validChannels;
	
	public FilterExtractChannelsVideo(int[] validChannels) {       //constructor
		
		this.validChannels = validChannels;
	}

	@Override
	public EEGModel applyFilter(EEGModel eeg) {
		// TODO Auto-generated method stub
		return null;
	}

}
