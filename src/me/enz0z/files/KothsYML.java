package me.enz0z.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.enz0z.Core;

public class KothsYML {

	File p;
	FileConfiguration pFileConfig;
	String fileName = "koths.yml";

	public KothsYML() {
		p = new File("plugins/" + Core.getPluginName() + "/" + fileName);
		pFileConfig = YamlConfiguration.loadConfiguration(p);
	}

	public void create() {
		if (!p.exists()) {
			try {
				p.createNewFile();
				pFileConfig.save(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public FileConfiguration get() {
		return pFileConfig;
	}

	public void save() {
		try {
			pFileConfig.save(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
