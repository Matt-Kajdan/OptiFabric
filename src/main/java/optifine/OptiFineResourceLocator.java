package optifine;

import java.io.IOException;
import java.io.InputStream;

import me.modmuss50.optifabric.mod.OptifineResources;

public final class OptiFineResourceLocator {
	private OptiFineResourceLocator() {
	}

	public static InputStream getOptiFineResourceStream(String path) throws IOException {
		return OptifineResources.INSTANCE.getResource(path);
	}
}
