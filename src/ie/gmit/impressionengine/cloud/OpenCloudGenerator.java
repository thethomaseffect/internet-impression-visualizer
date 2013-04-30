package ie.gmit.impressionengine.cloud;

import java.awt.Font;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

public class OpenCloudGenerator {

	private final List<String> wordsList;

	public OpenCloudGenerator(final List<String> wordsList) {
		this.wordsList = wordsList;
	}

	private void initUI() {
		JFrame frame = new JFrame("Internet Impression Cloud Generator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		Cloud cloud = new Cloud();
		for (int i = 0; i < 20; i++) {
			Tag wordTag = new Tag(wordsList.get(i), 30 - i);
			cloud.addTag(wordTag);
		}
		for (Tag tag : cloud.tags()) {
			final JLabel label = new JLabel(tag.getName());
			label.setOpaque(false);
			Font font = new Font("Word", Font.BOLD, (int) tag.getScore() + 30);
			label.setFont(font);
			panel.add(label);
		}
		frame.add(panel);
		frame.setSize(800, 600);
		frame.setVisible(true);
	}

	public void showTagWindow() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new OpenCloudGenerator(wordsList).initUI();
			}
		});
	}

}