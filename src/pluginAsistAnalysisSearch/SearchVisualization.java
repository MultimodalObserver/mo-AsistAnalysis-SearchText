/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pluginAsistAnalysisSearch;


/**
 *
 * @author Lathy
 */
public class SearchVisualization {
    
    private String filePath;
	private String configuration;

	public SearchVisualization(String filePath, String configuration) {
		this.filePath = filePath;
		this.configuration = configuration;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public String getConfiguration() {
		return this.configuration;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;

		if(other == this)
			return true;

		if(!(other instanceof SearchVisualization))
			return false;

		SearchVisualization asistAnalysisVisualization = (SearchVisualization) other;
		if (!this.filePath.equals(asistAnalysisVisualization.getFilePath()) || !this.configuration.equals(asistAnalysisVisualization.getConfiguration())) {
			return false;
		}

		return true;
	}
    
    
}
