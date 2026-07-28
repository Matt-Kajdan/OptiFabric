package me.modmuss50.optifabric.patcher;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Arrays;

import org.junit.Test;

public class ClassCacheTest {
	@Test
	public void readsCompleteCache() throws Exception {
		File directory = Files.createTempDirectory("optifabric-class-cache").toFile();
		File output = new File(directory, "classes.gz");
		byte[] hash = {1, 2, 3};
		byte[] bytes = {4, 5, 6};
		ClassCache cache = new ClassCache(hash);
		cache.addClass("example/Test", bytes);

		cache.save(output);
		ClassCache restored = ClassCache.read(output);

		assertArrayEquals(hash, restored.getHash());
		assertArrayEquals(bytes, restored.getClass("example/Test"));
	}

	@Test
	public void rejectsEmptyCache() throws Exception {
		File output = Files.createTempFile("optifabric-empty-cache", ".gz").toFile();

		assertNull(ClassCache.read(output).getHash());
	}

	@Test
	public void rejectsTruncatedCache() throws Exception {
		File directory = Files.createTempDirectory("optifabric-class-cache").toFile();
		File complete = new File(directory, "complete.gz");
		File truncated = new File(directory, "truncated.gz");
		ClassCache cache = new ClassCache(new byte[] {1, 2, 3});
		cache.addClass("example/Test", new byte[] {4, 5, 6});
		cache.save(complete);
		byte[] bytes = Files.readAllBytes(complete.toPath());

		try (FileOutputStream output = new FileOutputStream(truncated)) {
			output.write(Arrays.copyOf(bytes, bytes.length / 2));
		}

		assertNull(ClassCache.read(truncated).getHash());
	}
}
